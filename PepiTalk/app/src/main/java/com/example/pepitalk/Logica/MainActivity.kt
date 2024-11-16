package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.R
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Firebase App Check
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
    }
}