package com.example.pepitalk.Logica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Oferta : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val PATH_USERS = "users/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oferta)

        val BotonVerOfertas= findViewById<Button>(R.id.buttonVerOfertas)
        val BotonCrearOfertas = findViewById<Button>(R.id.buttonCrearOfertas)
        val menuInicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        VerOfertas(BotonVerOfertas)
        CrearOfertas(BotonCrearOfertas)
        irPerfil(perfil, this)
        menuPrincipal(menuInicio, this)
    }

    fun VerOfertas( VerOferta : Button){
        val irAVerOfertas = Intent(this, VerOfertas::class.java).apply {
            putExtra("tipo", "misOfertas")
        }
        VerOferta.setOnClickListener{
            startActivity(irAVerOfertas)
        }
    }
    fun CrearOfertas( CrearOfertas : Button){
        val irACrearOfertas = Intent(this, CrearOferta::class.java)
        CrearOfertas.setOnClickListener{
            startActivity(irACrearOfertas)
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
}