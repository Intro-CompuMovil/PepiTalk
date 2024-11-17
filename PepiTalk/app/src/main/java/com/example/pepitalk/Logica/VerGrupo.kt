package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataCalificaciones
import com.example.pepitalk.Datos.DataGrupo
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class VerGrupo : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference
    private val PATH_USERS = "users/"
    private val PATH_GRUPOS = "grupos/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_grupo)
        setUserPhoto()
        initializeTextViews()
        setupButtonListeners()
    }

    private fun roles(dueno: String?, integrantes: String?){
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if(userId != null){
            if(dueno == userId){
                botonesDueno()
            }else if (integrantes != null) {
                if(integrantes.contains(userId)){
                    botonesMiembros()
                }else{

                }
            }
        }
    }

    private fun botonesDueno(){
        val reuniones = findViewById<Button>(R.id.btnVerReuniones)
        val verCali = findViewById<Button>(R.id.btnVerCalificaciones)
        val crearReunion = findViewById<Button>(R.id.btnCrearReunion)
        val actualizarGrupo = findViewById<Button>(R.id.btnActualizarGrupo)
        val eliminarGrupo = findViewById<Button>(R.id.btnEliminarGrupo)
        val unirse = findViewById<Button>(R.id.btnUnirse)

        unirse.visibility = View.GONE
        reuniones.visibility = View.VISIBLE
        verCali.visibility = View.VISIBLE
        crearReunion.visibility = View.VISIBLE
        actualizarGrupo.visibility = View.VISIBLE
        eliminarGrupo.visibility = View.VISIBLE
    }

    private fun botonesMiembros(){
        val reuniones = findViewById<Button>(R.id.btnVerReuniones)
        val verCali = findViewById<Button>(R.id.btnVerCalificaciones)
        val calificar = findViewById<Button>(R.id.btnCalificarGrupo)
        val salir = findViewById<Button>(R.id.btnSalirGrupo)
        val unirse = findViewById<Button>(R.id.btnUnirse)

        unirse.visibility = View.GONE
        reuniones.visibility = View.VISIBLE
        verCali.visibility = View.VISIBLE
        calificar.visibility = View.VISIBLE
        salir.visibility = View.VISIBLE
    }

    private fun initializeTextViews() {
        val nombre = intent.getStringExtra("nombre")
        val idioma = intent.getStringExtra("idioma")
        val nivel = intent.getStringExtra("nivel")
        val descripcion = intent.getStringExtra("descripcion")
        val calificacionesString = intent.getStringExtra("calificaciones")
        val integrantes = intent.getStringExtra("integrantes")
        val dueno = intent.getStringExtra("dueno")
        val imageUrl = intent.getStringExtra("imageUrl")


        val calificaciones = parseCalificaciones(calificacionesString)
        val promedio = calcularPromedio(calificaciones)

        findViewById<TextView>(R.id.nombre).text = nombre
        findViewById<TextView>(R.id.textNomIdioma).text = idioma
        findViewById<TextView>(R.id.textNomNivel).text = nivel
        findViewById<TextView>(R.id.textDescri).text = descripcion
        findViewById<TextView>(R.id.textCali).text = promedio.toString()
        val imagenGrupo = findViewById<ImageView>(R.id.imagenGrupo)

        Glide.with(this)
            .load(imageUrl)  // Carga la URL de descarga de Firebase
            // .placeholder(R.drawable.placeholder)  // Imagen de marcador de posición mientras carga
            //  .error(R.drawable.error)  // Imagen de error si falla la carga
            .into(imagenGrupo)

        roles(dueno, integrantes)
    }

    private fun parseCalificaciones(calificacionesString: String?): List<DataCalificaciones> {
        if (calificacionesString.isNullOrEmpty()) return emptyList()

        return calificacionesString
            .removeSurrounding("[", "]")
            .split("),")
            .mapNotNull {
                val parts = it.removeSurrounding("DataCalificaciones(", ")").split(", comentario=")
                if (parts.size == 2) {
                    val nota = parts[0].split("=")[1].toDoubleOrNull()
                    val comentario = parts[1]
                    if (nota != null) DataCalificaciones(nota, comentario) else null
                } else {
                    null
                }
            }
    }

    private fun calcularPromedio(calificaciones: List<DataCalificaciones>): String {
        val notas = calificaciones.map { it.nota }
        return if (notas.isNotEmpty()) {
            String.format("%.1f", notas.average())
        } else {
            "0.0"
        }
    }

    private fun setupButtonListeners() {
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        val verCali = findViewById<Button>(R.id.btnVerCalificaciones)
        val unirse = findViewById<Button>(R.id.btnUnirse)
        val reuniones = findViewById<Button>(R.id.btnVerReuniones)
        val calificar = findViewById<Button>(R.id.btnCalificarGrupo)
        val salir = findViewById<Button>(R.id.btnSalirGrupo)
        val crearReunion = findViewById<Button>(R.id.btnCrearReunion)
        val actualizarGrupo = findViewById<Button>(R.id.btnActualizarGrupo)
        val eliminarGrupo = findViewById<Button>(R.id.btnEliminarGrupo)

        verCali.setOnClickListener {

            val peticion = Intent(this, VerCalificaciones::class.java)
            val bundle = Bundle()
            val nombre = intent.getStringExtra("nombre")!!
            bundle.putString("nombre",nombre)
            bundle.putString("tipo", "grupo")
            peticion.putExtra("bundle", bundle)
            startActivity(peticion)
        }

        unirse.setOnClickListener {
            unirseAGrupo()
            unirse.visibility = View.GONE
            calificar.visibility = View.VISIBLE
            salir.visibility = View.VISIBLE
            reuniones.visibility = View.VISIBLE
        }

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

        perfil.setOnClickListener {
            val peticion = Intent(this, Perfil::class.java)
            startActivity(peticion)
        }

        reuniones.setOnClickListener {
            val peticion = Intent(this, VerReuniones::class.java)
            startActivity(peticion)
        }

        crearReunion.setOnClickListener {
            val peticion = Intent(this, CrearReunion::class.java)
            startActivity(peticion)
        }

        calificar.setOnClickListener {
            val peticion = Intent(this, Calificar::class.java)
            startActivity(peticion)
        }

        salir.setOnClickListener {
            val peticion = Intent(this, Grupo::class.java)
            startActivity(peticion)
        }

        actualizarGrupo.setOnClickListener {
            val peticion = Intent(this, ActualizarGrupo::class.java)
            startActivity(peticion)
        }

        eliminarGrupo.setOnClickListener {
            Toast.makeText(this, "Grupo eliminado", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, Grupo::class.java)
            startActivity(peticion)
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

    private fun unirseAGrupo(){
        val userId = auth.currentUser?.uid
        val nombre = intent.getStringExtra("nombre")!!
        val grupoRef = database.getReference(PATH_GRUPOS)

        grupoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (singleSnapshot in dataSnapshot.children) {
                    val myGroup = singleSnapshot.getValue(DataGrupo::class.java)
                    if (myGroup != null && myGroup.nombre == nombre) {
                        val grupoId = singleSnapshot.key
                        val newRef = grupoRef.child(grupoId!!).child("integrantes")
                        val tam = myGroup.integrantes.size
                        val nuevoIntegrante = mapOf(tam.toString() to userId)
                        newRef.updateChildren(nuevoIntegrante)
                            .addOnSuccessListener {
                                Log.d("FirebaseDB", "ID del usuario añadido correctamente")
                            }
                            .addOnFailureListener { exception ->
                                Log.e("FirebaseDB", "Error al añadir el ID del usuario", exception)
                            }
                    }

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Si ocurre un error
            }
        })

    }
}
