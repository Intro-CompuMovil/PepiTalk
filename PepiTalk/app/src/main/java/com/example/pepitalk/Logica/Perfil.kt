package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataGrupo
import com.example.pepitalk.Datos.DataOferta
import com.example.pepitalk.Datos.DataReunion
import com.example.pepitalk.Datos.Persona
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class Perfil :  AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference
    private val PATH_USERS = "users/"
    private val PATH_GRUPOS = "grupos/"
    private val PATH_REUNIONES = "reuniones/"
    private val PATH_OFERTAS = "ofertas/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        setUserPhoto()
        setupLoggedName()
        setupButtonListeners()
    }

    private fun setupLoggedName(){
        val nombre = findViewById<TextView>(R.id.nombre)
        val correo = findViewById<TextView>(R.id.correo)
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        var name = ""
        var correoE = ""
        if(userId != null){
            val userRef = database.getReference(PATH_USERS).child(userId)
            userRef.child("nombre").get().addOnSuccessListener { dataSnapshot ->
                name = dataSnapshot.value.toString()
                nombre.setText(name)

            }
            userRef.child("correo").get().addOnSuccessListener { dataSnapshot ->
                correoE = dataSnapshot.value.toString()
                correo.setText(correoE)
            }
        }

    }


    fun setUserPhoto(){
        val imageUser = findViewById<ImageView>(R.id.imagenPerfil)
        var imageUrl = ""

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if(userId != null){
            val userRef = database.getReference(PATH_USERS).child(userId)
            userRef.child("imageUrl").get().addOnSuccessListener { dataSnapshot ->
                imageUrl = dataSnapshot.value.toString()
                Glide.with(this)
                    .load(imageUrl)  // Carga la URL de descarga de Firebase
                    // .placeholder(R.drawable.placeholder)  // Imagen de marcador de posición mientras carga
                    //  .error(R.drawable.error)  // Imagen de error si falla la carga
                    .into(imageUser)
            }
        }

    }

    private fun setupButtonListeners() {
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val actualizar = findViewById<Button>(R.id.btnActualizar)
        val eliminar = findViewById<Button>(R.id.btnEliminar)
        val cerrar = findViewById<Button>(R.id.btnCerrarSesion)

        inicio.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid
            if(userId != null) {
                val userRef = database.getReference(PATH_USERS).child(userId)
                userRef.child("tipo").get().addOnSuccessListener { dataSnapshot ->
                    val tipo = dataSnapshot.value.toString()
                    if (tipo == "Cliente") {
                        val peticion = Intent(this, MenuCliente::class.java)
                        startActivity(peticion)
                    } else {
                        val peticion = Intent(this, MenuTraductor::class.java)
                        startActivity(peticion)
                    }
                }
            }
        }

        actualizar.setOnClickListener {
            val peticion = Intent(this, ActualizarCuenta::class.java)
            startActivity(peticion)
        }

        eliminar.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val userRef = database.getReference(PATH_USERS).child(userId)

                userRef.child("imageUrl").get().addOnSuccessListener { dataSnapshot ->
                    val imageUrl = dataSnapshot.value.toString()
                    if (imageUrl.isNotEmpty()) {
                        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
                        storageRef.delete().addOnSuccessListener {
                            userRef.removeValue().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    handleUserGroups(userId)
                                    handleUserMeetings(userId)
                                    handleUserOffers(userId)
                                    auth.currentUser?.delete()?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_LONG).show()
                                            val peticion = Intent(this, MainActivity::class.java)
                                            startActivity(peticion)
                                        } else {
                                            Toast.makeText(this, "Error al eliminar la cuenta", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(this, "Error al eliminar los datos del usuario", Toast.LENGTH_LONG).show()
                                }
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "Error al eliminar la imagen", Toast.LENGTH_LONG).show()
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al obtener la URL de la imagen", Toast.LENGTH_LONG).show()
                }
            }
        }

        cerrar.setOnClickListener {
            Toast.makeText(this, "Log out exitoso ", Toast.LENGTH_LONG).show()
            auth.signOut()
            val peticion = Intent(this, MainActivity::class.java)
            peticion.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(peticion)
            finish()
        }
    }

    private fun handleUserGroups(userId: String) {
        val grupoRef = database.getReference(PATH_GRUPOS)
        grupoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    val myGroup = singleSnapshot.getValue(DataGrupo::class.java)
                    if (myGroup != null) {
                        val grupoId = singleSnapshot.key
                        if (myGroup.dueno == userId) {
                            deleteUserGroup(grupoId)
                        } else if (myGroup.integrantes.contains(userId)) {
                            removeUserFromGroup(grupoId, userId)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun deleteUserGroup(groupId: String?) {
        if (groupId != null) {
            val grupoRef = database.getReference(PATH_GRUPOS).child(groupId)
            grupoRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Grupo eliminado", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error al eliminar grupo", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun removeUserFromGroup(grupoId: String?, userId: String) {
        if (grupoId != null) {
            val grupoRef = database.getReference(PATH_GRUPOS).child(grupoId).child("integrantes")
            grupoRef.get().addOnSuccessListener { snapshot ->
                val integrantes = snapshot.value as? List<String>?
                if (integrantes != null) {
                    val nuevosIntegrantes = integrantes.filter { it != userId }
                    grupoRef.setValue(nuevosIntegrantes).addOnSuccessListener {
                        Log.d("FirebaseDB", "Integrante eliminado correctamente")
                    }.addOnFailureListener { exception ->
                        Log.e("FirebaseDB", "Error al eliminar el integrante", exception)
                    }
                }
            }
        }
    }

    private fun handleUserMeetings(userId: String) {
        val reunionRef = database.getReference(PATH_REUNIONES)
        reunionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    val myMeeting = singleSnapshot.getValue(DataReunion::class.java)
                    if (myMeeting != null) {
                        val reunionId = singleSnapshot.key
                        if (myMeeting.dueno == userId) {
                            // User is the owner, delete the meeting
                            deleteMeeting(reunionId)
                        } else if (myMeeting.integrantes.contains(userId)) {
                            removeUserFromMeeting(reunionId, userId)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }



    private fun deleteMeeting(reunionId: String?) {
        if (reunionId != null) {
            val reunionRef = database.getReference(PATH_REUNIONES).child(reunionId)
            reunionRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Reunión eliminada", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error al eliminar la reunión", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun removeUserFromMeeting(reunionId: String?, userId: String) {
        if (reunionId != null) {
            val reunionRef = database.getReference(PATH_REUNIONES).child(reunionId).child("integrantes")
            reunionRef.get().addOnSuccessListener { snapshot ->
                val integrantes = snapshot.value as? List<String>?
                if (integrantes != null) {
                    val nuevosIntegrantes = integrantes.filter { it != userId }
                    reunionRef.setValue(nuevosIntegrantes).addOnSuccessListener {
                        Log.d("FirebaseDB", "Integrante eliminado correctamente de la reunión")
                    }.addOnFailureListener { exception ->
                        Log.e("FirebaseDB", "Error al eliminar el integrante de la reunión", exception)
                    }
                }
            }
        }
    }

    private fun handleUserOffers(userId: String) {
        val ofertaRef = database.getReference(PATH_OFERTAS)
        ofertaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    val myOffer = singleSnapshot.getValue(DataOferta::class.java)
                    if (myOffer != null) {
                        if (myOffer.dueno == userId) {
                            singleSnapshot.ref.removeValue().addOnSuccessListener {
                                Log.d("FirebaseDB", "Oferta eliminada correctamente")
                            }.addOnFailureListener { exception ->
                                Log.e("FirebaseDB", "Error al eliminar la oferta", exception)
                            }
                        } else if (myOffer.trabajador == userId) {
                            singleSnapshot.ref.child("trabajador").setValue("").addOnSuccessListener {
                                Log.d("FirebaseDB", "Atributo trabajador vaciado correctamente")
                                singleSnapshot.ref.child("aceptado").setValue(false).addOnSuccessListener {
                                    Log.d("FirebaseDB", "Atributo aceptado establecido en false correctamente")
                                }.addOnFailureListener { exception ->
                                    Log.e("FirebaseDB", "Error al establecer el atributo aceptado en false", exception)
                                }
                            }.addOnFailureListener { exception ->
                                Log.e("FirebaseDB", "Error al vaciar el atributo trabajador", exception)
                            }
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

}