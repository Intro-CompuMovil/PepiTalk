package com.example.pepitalk.Logica

import android.content.Context
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
import com.example.pepitalk.Datos.DataCalificaciones
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AceptarTraductor : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val PATH_USERS = "users/"
    private val PATH_OFFERS = "ofertas/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aceptar_traductor)

        setUpVista()
        setUserPhoto()


        val aceptar = findViewById<Button>(R.id.buttonAceptar)
        val rechazar = findViewById<Button>(R.id.buttonRechazar)
        val menuInicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        aceptarTraductor(aceptar, this)
        rechazarTraductor(rechazar, this)
        irPerfil(perfil, this)
        menuPrincipal(menuInicio, this)

    }
    fun aceptarTraductor(aceptar : Button, context: Context ){
        val irAMenuCliente = Intent(this, MenuCliente::class.java)
        aceptar.setOnClickListener {
            val ofertaId = intent.getStringExtra("llave")
            val ofertaRef = database.getReference(PATH_OFFERS + ofertaId + "/aceptado")
            ofertaRef.setValue(true)
                .addOnSuccessListener {
                    Log.d("FirebaseDB", "El valor de 'aceptado' se actualizó a true.")
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseDB", "Error al actualizar el valor de 'aceptado'", exception)
                }

            startActivity(irAMenuCliente)
            Toast.makeText(this,"se ha Aceptado el traductor", Toast. LENGTH_LONG).show()
        }
    }
    fun rechazarTraductor(rechazar : Button, context : Context){
        val irAMenuCliente = Intent(this, MenuCliente::class.java)
        rechazar.setOnClickListener {
            val ofertaId = intent.getStringExtra("llave")
            val ofertaRef = database.getReference(PATH_OFFERS + ofertaId + "/trabajador")

            ofertaRef.setValue("")
                .addOnSuccessListener {
                    Log.d("FirebaseDB", "El valor de 'trabajador' se actualizó a vacío.")
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseDB", "Error al actualizar el valor de 'trabajador'", exception)
                }

            startActivity(irAMenuCliente)
            Toast.makeText(this,"se ha Rechazado el traductor", Toast. LENGTH_LONG).show()
        }
    }
    fun menuPrincipal(menuInicio: ImageButton, context: Context){
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        menuInicio.setOnClickListener(){
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

    }
    fun irPerfil(perfil : ImageButton, context : Context){
        val irAPerfil = Intent(this, Perfil::class.java)
        perfil.setOnClickListener {
            startActivity(irAPerfil)
            Toast.makeText(this,"¡Tu perfil!", Toast. LENGTH_LONG).show()
        }
    }

    fun setUpVista(){
        val nombreT = findViewById<TextView>(R.id.NombreTraductor)
        val calificacionT = findViewById<TextView>(R.id.CalificacionTraductor)
        val imagenT = findViewById<ImageView>(R.id.imageView)

        val traductorId = intent.getStringExtra("trabajador")
        val traductorRef = database.getReference(PATH_USERS).child(traductorId.toString())

        traductorRef.child("nombre").get().addOnSuccessListener { dataSnapshot ->
            val nombre = dataSnapshot.value.toString()
            nombreT.text = nombre
        }

        traductorRef.child("imageUrl").get().addOnSuccessListener { dataSnapshot ->
            val imageUrl = dataSnapshot.value.toString()
            Glide.with(this)
                .load(imageUrl)  // Carga la URL de descarga de Firebase
                // .placeholder(R.drawable.placeholder)  // Imagen de marcador de posición mientras carga
                //  .error(R.drawable.error)  // Imagen de error si falla la carga
                .into(imagenT)
        }

        traductorRef.child("calificaciones").get().addOnSuccessListener { dataSnapshot ->
            val calificacionesString = dataSnapshot.value.toString()
            val calificaciones = parseCalificaciones(calificacionesString)
            val promedio = calcularPromedio(calificaciones)
            calificacionT.text = "Calificación: ${promedio}"
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
}