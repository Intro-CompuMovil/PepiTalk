package com.example.pepitalk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VerReunion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_reunion)

        initializeTextViews()
        setupButtonListeners()
    }

    private fun initializeTextViews() {
        val nombre = intent.getStringExtra("nombre")
        val dia = intent.getStringExtra("dia")
        val hora = intent.getStringExtra("hora")
        val idioma = intent.getStringExtra("idioma")
        val nivel = intent.getStringExtra("nivel")
        val lugar = intent.getStringExtra("lugar")
        val descripcion = intent.getStringExtra("descripcion")

        findViewById<TextView>(R.id.nombre).text = nombre
        findViewById<TextView>(R.id.dia).text = dia
        findViewById<TextView>(R.id.hora).text = hora
        findViewById<TextView>(R.id.idioma).text = idioma
        findViewById<TextView>(R.id.nivel).text = nivel
        findViewById<TextView>(R.id.lugar).text = lugar
        findViewById<TextView>(R.id.descripcion).text = descripcion
    }

    private fun setupButtonListeners() {
        val unirse = findViewById<Button>(R.id.btnUnirse)
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        val ruta = findViewById<Button>(R.id.btnRuta)
        val calificar = findViewById<Button>(R.id.btnCalificarReunion)
        val salir = findViewById<Button>(R.id.btnSalirReunion)

        unirse.setOnClickListener {
            unirse.visibility = View.GONE
            findViewById<LinearLayout>(R.id.btnGroup).visibility = View.VISIBLE
        }

        inicio.setOnClickListener {
            if (/*es cliente*/true) {
                val peticion = Intent(this, MenuCliente::class.java)
                startActivity(peticion)
            } else {
                val peticion = Intent(this, MenuTraductor::class.java)
                startActivity(peticion)
            }
        }

        perfil.setOnClickListener {
            val peticion = Intent(this, Perfil::class.java)
            startActivity(peticion)
        }

        ruta.setOnClickListener {
            val peticion = Intent(this, Ruta::class.java)
            startActivity(peticion)
        }

        calificar.setOnClickListener {
            val peticion = Intent(this, Calificar::class.java)
            startActivity(peticion)
        }

        salir.setOnClickListener {
            val peticion = Intent(this, VerReuniones::class.java)
            startActivity(peticion)
        }
    }
}