package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pepitalk.R



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
    }
}