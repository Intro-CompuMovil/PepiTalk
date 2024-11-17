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
import com.example.pepitalk.Datos.Persona
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Perfil :  AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference
    private val PATH_USERS = "users/"

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
            Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, com.example.pepitalk.Logica.MainActivity::class.java)
            startActivity(peticion)
        }

        cerrar.setOnClickListener {
            Toast.makeText(this, "Log out exitoso ", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, MainActivity::class.java)
            startActivity(peticion)
        }
    }

}