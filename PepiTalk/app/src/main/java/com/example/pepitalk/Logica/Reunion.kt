package com.example.pepitalk.Logica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Reunion : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val PATH_USERS = "users/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reunion)

        val ButtonVerMisReuniones = findViewById<Button>(R.id.buttonVerMisReuniones)
        val ButtonVerReunionesParaUnirme = findViewById<Button>(R.id.buttonVerReunionesParaUnirme)
        val ButtonCrearReunion = findViewById<Button>(R.id.buttonCrearReunion)
        val menuInicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        setUserPhoto()
        VerMisReuniones(ButtonVerMisReuniones)
        VerReunionesParaUnirme(ButtonVerReunionesParaUnirme)
        CrearReunion(ButtonCrearReunion)
        irPerfil(perfil, this)
        menuPrincipal(menuInicio, this)
    }

    fun VerMisReuniones( ButtonVerMisReuniones : Button ){
        val irAMisReuniones = Intent(this, VerReuniones::class.java).apply {
            putExtra("tipo", "misReuniones")
        }
        ButtonVerMisReuniones.setOnClickListener {
            startActivity(irAMisReuniones)
        }
    }
    fun VerReunionesParaUnirme( ButtonVerReunionesParaUnirme : Button ){
        val irAReunionesParaUnirme = Intent(this, VerReuniones::class.java).apply {
            putExtra("tipo", "reunionesParaUnirme")
        }
        ButtonVerReunionesParaUnirme.setOnClickListener {
            startActivity(irAReunionesParaUnirme)
        }
    }
    fun CrearReunion( ButtonCrearReunion : Button ){
        val irACrearReuniones = Intent(this, CrearReunion::class.java)
        ButtonCrearReunion.setOnClickListener {
            startActivity(irACrearReuniones)
        }
    }
    fun menuPrincipal(menuInicio: ImageButton, context: Context){
        menuInicio.setOnClickListener {
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
    }
    fun irPerfil(perfil : ImageButton, context : Context){
        val irAPerfil = Intent(this, Perfil::class.java)
        perfil.setOnClickListener {
            startActivity(irAPerfil)
            Toast.makeText(this,"¡Tu perfil!", Toast. LENGTH_LONG).show()
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