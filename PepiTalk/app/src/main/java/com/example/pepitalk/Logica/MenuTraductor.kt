package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MenuTraductor : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val PATH_USERS = "users/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_traductor)

        val Trabajos = findViewById<Button>(R.id.buttonTrabajos)
        val Ofertas = findViewById<Button>(R.id.buttonOfertas)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)


        setUserPhoto()
        AccionBotonTrabajos(Trabajos)
        AccionBotonOfertas(Ofertas)
        AccionBotonPerfil(perfil)

    }
    fun AccionBotonTrabajos( Trabajos : Button){
        val irATrabajos = Intent(this, VerOfertas::class.java).apply {
            putExtra("tipo", "misTrabajos")
        }
        Trabajos.setOnClickListener {
            startActivity(irATrabajos)
        }
    }
    fun AccionBotonOfertas( Ofertas : Button){
        val irAOfertas = Intent(this, VerOfertas::class.java).apply {
            putExtra("tipo", "aceptarOfertas")
        }
        Ofertas.setOnClickListener{
            startActivity(irAOfertas)
        }
    }

    fun AccionBotonPerfil( perfil : ImageButton) {
        val irAPerfil = Intent(this, Perfil::class.java)
        perfil.setOnClickListener{
            startActivity(irAPerfil)
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