package com.example.pepitalk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VerGrupo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_grupo)
        initializeTextViews()
        setupButtonListeners()
    }

    private fun initializeTextViews() {
        val nombre = intent.getStringExtra("nombre")
        val idioma = intent.getStringExtra("idioma")
        val nivel = intent.getStringExtra("nivel")
        val descripcion = intent.getStringExtra("descripcion")

        findViewById<TextView>(R.id.nombre).text = nombre
        findViewById<TextView>(R.id.textNomIdioma).text = idioma
        findViewById<TextView>(R.id.textNomNivel).text = nivel
        findViewById<TextView>(R.id.textDescri).text = descripcion
    }

    private fun setupButtonListeners() {
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        val unirse = findViewById<Button>(R.id.btnUnirse)
        val reuniones = findViewById<Button>(R.id.btnVerReuniones)
        val crearReunion = findViewById<Button>(R.id.btnCrearReunion)
        val calificar = findViewById<Button>(R.id.btnCalificarGrupo)
        val salir = findViewById<Button>(R.id.btnSalirGrupo)

        unirse.setOnClickListener {
            unirse.visibility = View.GONE
            findViewById<LinearLayout>(R.id.btnGroup).visibility = View.VISIBLE
        }

        inicio.setOnClickListener {
            if(/*es cliente*/true){
                val peticion = Intent(this, MenuCliente::class.java)
                startActivity(peticion)
            }else{
                val peticion = Intent(this, MenuTraductor::class.java)
                startActivity(peticion)
            }

        }

        perfil.setOnClickListener {
            val peticion = Intent(this, Perfil::class.java)
            startActivity(peticion)
        }

        reuniones.setOnClickListener {
            val peticion = Intent(this, VerReuniones::class.java)
            startActivity(peticion)
        }

        crearReunion.setOnClickListener {
            val peticion = Intent(this, CrearReunion::class.java)
            startActivity(peticion)
        }

        calificar.setOnClickListener {
            val peticion = Intent(this, Calificar::class.java)
            startActivity(peticion)
        }

        salir.setOnClickListener {
            val peticion = Intent(this, VerGrupos::class.java)
            startActivity(peticion)
        }
    }
}
