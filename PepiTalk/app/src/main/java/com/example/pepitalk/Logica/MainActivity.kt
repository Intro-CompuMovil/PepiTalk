package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        auth.signOut()

        val IniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)
        val Registrarse = findViewById<Button>(R.id.buttonRegistrarse)

        IniciarSesion.setOnClickListener {
            val intent = Intent(this, com.example.pepitalk.Logica.Login::class.java)
            startActivity(intent)
        }
        Registrarse.setOnClickListener {
            val intent = Intent(this, com.example.pepitalk.Logica.Registro::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, MenuCliente::class.java)
            startActivity(intent)
            finish()
        }
    }

}