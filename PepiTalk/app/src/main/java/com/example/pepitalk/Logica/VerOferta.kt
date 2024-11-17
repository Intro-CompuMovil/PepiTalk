package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class VerOferta : AppCompatActivity()  {
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference
    private val PATH_USERS = "users/"
    private val PATH_OFFERS = "ofertas/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_oferta)
        setUserPhoto()
        initializeTextViews()
        setupButtonListeners()
    }

    private fun roles(dueno: String?, trabajador: String?){
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if(userId != null){
            if(dueno == userId){
                botonesDueno()
            }else if (userId == trabajador) {
                botonesTraductor()
            }else{

            }
        }

    }

    private fun botonesDueno(){
        val aceptar = findViewById<Button>(R.id.btnAceptar)
        val calificar = findViewById<Button>(R.id.btnCalificarTraductor)
        val actualizar = findViewById<Button>(R.id.btnActualizarContrato)
        val eliminar = findViewById<Button>(R.id.btnEliminarContrato)
        val revisar = findViewById<Button>(R.id.btnAceptarTraductor)

        val tipo = intent.getStringExtra("tipo")

        aceptar.visibility = View.GONE

        if (tipo == "misOfertas"){
            calificar.visibility = View.VISIBLE
            actualizar.visibility = View.VISIBLE
            eliminar.visibility = View.VISIBLE
        }
        else if (tipo == "AceptarTraductor"){
            revisar.visibility = View.VISIBLE
        }

    }

    private fun botonesTraductor(){
        val aceptar = findViewById<Button>(R.id.btnAceptar)
        val rechazar = findViewById<Button>(R.id.btnCancelarContrato)
        val ruta = findViewById<Button>(R.id.btnRuta)

        aceptar.visibility = View.GONE
        rechazar.visibility = View.VISIBLE
        ruta.visibility = View.VISIBLE
    }

    private fun initializeTextViews() {
        val language = intent.getStringExtra("idioma")
        val date = intent.getStringExtra("fecha")
        val start = intent.getStringExtra("horaInicio")
        val final = intent.getStringExtra("horaFinal")
        val place = intent.getStringExtra("lugar")
        val descripcion = intent.getStringExtra("descripcion")
        val dueno = intent.getStringExtra("dueno")
        val trabajador = intent.getStringExtra("trabajador")

        findViewById<TextView>(R.id.idiomaOfe).text = language
        findViewById<TextView>(R.id.fechaOfe).text = date
        findViewById<TextView>(R.id.horaInicioOfe).text = start
        findViewById<TextView>(R.id.horaFinOfe).text = final
        findViewById<TextView>(R.id.lugarOfe).text = place
        findViewById<TextView>(R.id.descripcionOfe).text = descripcion

        roles(dueno, trabajador)
    }

    private fun setupButtonListeners() {
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        val aceptar = findViewById<Button>(R.id.btnAceptar)
        val rechazar = findViewById<Button>(R.id.btnCancelarContrato)
        val calificar = findViewById<Button>(R.id.btnCalificarTraductor)
        val ruta = findViewById<Button>(R.id.btnRuta)
        val actualizarOferta = findViewById<Button>(R.id.btnActualizarContrato)
        val eliminarOferta = findViewById<Button>(R.id.btnEliminarContrato)
        val revisar = findViewById<Button>(R.id.btnAceptarTraductor)

        aceptar.setOnClickListener {
            aceptar.visibility = View.GONE
            val ofertaId = intent.getStringExtra("llave")
            val ofertaRef = database.getReference(PATH_OFFERS + ofertaId + "/trabajador")

            auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid
            if(userId != null){
                ofertaRef.setValue(userId)
                    .addOnSuccessListener {
                        Log.d("FirebaseDB", "El valor de 'trabajador' se actualizó a $userId.")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FirebaseDB", "Error al actualizar el valor de 'trabajador'", exception)
                    }
            }
            Toast.makeText(this, "Esperando confirmación del Cliente", Toast.LENGTH_LONG).show()
        }

        actualizarOferta.setOnClickListener {
            val peticion = Intent(this, ActualizarOferta::class.java)
            val ofertaId = intent.getStringExtra("llave")
            peticion.putExtra("id", ofertaId)
            startActivity(peticion)
        }

        eliminarOferta.setOnClickListener {
            //eliminar del json
            Toast.makeText(this, "Oferta eliminada", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, Oferta::class.java)
            startActivity(peticion)
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

        rechazar.setOnClickListener {

            val ofertaId = intent.getStringExtra("llave")
            val ofertaRef = database.getReference(PATH_OFFERS + ofertaId + "/trabajador")
            val aceptarRef = database.getReference(PATH_OFFERS + ofertaId + "/aceptado")

                ofertaRef.setValue("")
                    .addOnSuccessListener {
                        Log.d("FirebaseDB", "El valor de 'trabajador' se actualizó a '' ")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FirebaseDB", "Error al actualizar el valor de 'trabajador'", exception)
                    }

            aceptarRef.setValue(false)
                .addOnSuccessListener {
                    Log.d("FirebaseDB", "El valor de 'aceptado' se actualizó a false.")
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseDB", "Error al actualizar el valor de 'aceptado'", exception)
                }


            Toast.makeText(this, "Oferta rechazada", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, MenuTraductor::class.java)
            startActivity(peticion)
        }

        calificar.setOnClickListener {
            val peticion = Intent(this, Calificar::class.java)
            startActivity(peticion)
        }

        ruta.setOnClickListener {
            val peticion = Intent(this, Ruta::class.java)
            val place = intent.getStringExtra("lugar")
            peticion.putExtra("destino", place )
            startActivity(peticion)
        }

        revisar.setOnClickListener {
            val peticion = Intent(this, AceptarTraductor::class.java)
            val trabajador = intent.getStringExtra("trabajador")
            val llave = intent.getStringExtra("llave")
            peticion.putExtra("trabajador", trabajador)
            peticion.putExtra("llave", llave)
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
}