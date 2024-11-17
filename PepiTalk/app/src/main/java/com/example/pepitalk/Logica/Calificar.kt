package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataCalificaciones
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Calificar : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference
    private val PATH_USERS = "users/"
    private val PATH_REUNION = "reuniones/"
    private val PATH_GROUPS = "grupos/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calificar)

        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.onItemSelectedListener = this
        val editText = findViewById<EditText>(R.id.editTextComentario)
        val buttonCalificar = findViewById<Button>(R.id.buttonCalificar)
        val menuPrincipal = findViewById<ImageButton>(R.id.butInicio)
        val calificando = findViewById<TextView>(R.id.nombreCalificado)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        setUpView()
        setUserPhoto()
        buttonCalificar.setOnClickListener(){
            if(editText.text.toString().isEmpty()){
                Toast.makeText(this, "Por favor, ingrese un comentario", Toast.LENGTH_SHORT).show()
            }
            else{
                subirCalificacion()

                irPrincipal()
            }
        }
        menuPrincipal.setOnClickListener(){
            irPrincipal()
        }
        perfil.setOnClickListener(){
            startActivity(Intent(this, Perfil::class.java))
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


    }
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
    private fun irPrincipal(){
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

    fun setUserPhoto(){
        val imageUser = findViewById<ImageButton>(R.id.butPerfil)
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

    private fun setUpView(){
        val calificando = findViewById<TextView>(R.id.nombreCalificado)
        val tipo = intent.getStringExtra("tipo")
        val llave = intent.getStringExtra("llave")
        if(tipo == "traductor"){
            val userRef = database.getReference(PATH_USERS).child(llave.toString())
            userRef.child("nombre").get().addOnSuccessListener { dataSnapshot ->
                calificando.text = "Calificando a:" + dataSnapshot.value.toString()
            }
        }
        else if (tipo == "grupo"){
            val grupoRef = database.getReference(PATH_GROUPS).child(llave.toString())
            grupoRef.child("nombre").get().addOnSuccessListener { dataSnapshot ->
                calificando.text = "Calificando a:" + dataSnapshot.value.toString()
            }
        }
        else if(tipo == "reunion"){
            val reunionRef = database.getReference(PATH_REUNION).child(llave.toString())
            reunionRef.child("nombre").get().addOnSuccessListener { dataSnapshot ->
                calificando.text = "Calificando a:" + dataSnapshot.value.toString()
            }
        }
    }

    private fun subirCalificacion(){
        val tipo = intent.getStringExtra("tipo")
        val llave = intent.getStringExtra("llave")
        if(tipo == "traductor"){
            val userRef = database.getReference(PATH_USERS+ llave.toString() + "/calificaciones")
            userRef.get().addOnSuccessListener { dataSnapshot ->
                val currentSize = dataSnapshot.childrenCount.toInt() // Obtiene el tamaño actual
                val valoración = findViewById<Spinner>(R.id.spinner).selectedItem.toString()
                val comentario = findViewById<EditText>(R.id.editTextComentario).text.toString()
                val calificacion = DataCalificaciones(valoración.toDouble(), comentario)
                // Agrega el meetingId en la posición actual
                userRef.child(currentSize.toString()).setValue(calificacion)
                    .addOnSuccessListener {
                        // La actualización fue exitosa
                        println("Meeting ID agregado correctamente en reuniones.")
                    }
                    .addOnFailureListener { exception ->
                        // Hubo un error al actualizar
                        println("Error al agregar el Meeting ID: ${exception.message}")
                    }
            }.addOnFailureListener { exception ->
                // Maneja errores al intentar obtener la lista de reuniones
                println("Error al obtener la lista de reuniones: ${exception.message}")
            }
        }
        else if (tipo == "grupo"){
            val grupoRef = database.getReference(PATH_GROUPS + llave.toString() + "/calificaciones")
            grupoRef.get().addOnSuccessListener { dataSnapshot ->
                val currentSize = dataSnapshot.childrenCount.toInt() // Obtiene el tamaño actual
                val valoración = findViewById<Spinner>(R.id.spinner).selectedItem.toString()
                val comentario = findViewById<EditText>(R.id.editTextComentario).text.toString()
                val calificacion = DataCalificaciones(valoración.toDouble(), comentario)
                // Agrega el meetingId en la posición actual
                grupoRef.child(currentSize.toString()).setValue(calificacion)
                    .addOnSuccessListener {
                        // La actualización fue exitosa
                        println("Meeting ID agregado correctamente en reuniones.")
                    }
                    .addOnFailureListener { exception ->
                        // Hubo un error al actualizar
                        println("Error al agregar el Meeting ID: ${exception.message}")
                    }
            }.addOnFailureListener { exception ->
                // Maneja errores al intentar obtener la lista de reuniones
                println("Error al obtener la lista de reuniones: ${exception.message}")
            }

        }
        else if(tipo == "reunion"){
            val reunionRef = database.getReference(PATH_REUNION + llave.toString() + "/calificaciones")
            reunionRef.get().addOnSuccessListener { dataSnapshot ->
                val currentSize = dataSnapshot.childrenCount.toInt() // Obtiene el tamaño actual
                val valoración = findViewById<Spinner>(R.id.spinner).selectedItem.toString()
                val comentario = findViewById<EditText>(R.id.editTextComentario).text.toString()
                val calificacion = DataCalificaciones(valoración.toDouble(), comentario)
                // Agrega el meetingId en la posición actual
                reunionRef.child(currentSize.toString()).setValue(calificacion)
                    .addOnSuccessListener {
                        // La actualización fue exitosa
                        println("Meeting ID agregado correctamente en reuniones.")
                    }
                    .addOnFailureListener { exception ->
                        // Hubo un error al actualizar
                        println("Error al agregar el Meeting ID: ${exception.message}")
                    }
            }.addOnFailureListener { exception ->
                // Maneja errores al intentar obtener la lista de reuniones
                println("Error al obtener la lista de reuniones: ${exception.message}")
            }
        }
    }
}