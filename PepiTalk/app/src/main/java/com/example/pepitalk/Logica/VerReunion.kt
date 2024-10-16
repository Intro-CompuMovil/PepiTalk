package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.R


class VerReunion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_reunion)

        initializeTextViews()
        setupButtonListeners()

        if(/*dueño*/true){
            botonesDueno()
        }else if(/*unido*/!true){
            botonesMiembros()
        }else{
            //sin unir
        }
    }

    private fun botonesDueno(){
        val verCali = findViewById<Button>(R.id.btnVerCalificacionesReuniones)
        val actualizarGrupo = findViewById<Button>(R.id.btnActualizarReunion)
        val eliminarGrupo = findViewById<Button>(R.id.btnEliminarReunion)
        val unirse = findViewById<Button>(R.id.btnUnirse)
        val ruta = findViewById<Button>(R.id.btnRuta)

        unirse.visibility = View.GONE

        verCali.visibility = View.VISIBLE
        actualizarGrupo.visibility = View.VISIBLE
        eliminarGrupo.visibility = View.VISIBLE
        ruta.visibility = View.VISIBLE
    }

    private fun botonesMiembros(){
        val calificar = findViewById<Button>(R.id.btnCalificarReunion)
        val verCali = findViewById<Button>(R.id.btnVerCalificacionesReuniones)
        val unirse = findViewById<Button>(R.id.btnUnirse)
        val salir = findViewById<Button>(R.id.btnSalirReunion)
        val ruta = findViewById<Button>(R.id.btnRuta)

        unirse.visibility = View.GONE

        verCali.visibility = View.VISIBLE
        calificar.visibility = View.VISIBLE
        salir.visibility = View.VISIBLE
        ruta.visibility = View.VISIBLE
    }

    private fun initializeTextViews() {
        val nombre = intent.getStringExtra("nombre")
        val dia = intent.getStringExtra("dia")
        val hora = intent.getStringExtra("hora")
        val idioma = intent.getStringExtra("idioma")
        val nivel = intent.getStringExtra("nivel")
        val lugar = intent.getStringExtra("lugar")
        val descripcion = intent.getStringExtra("descripcion")
        //inicializar calificacion

        findViewById<TextView>(R.id.nombre).text = nombre
        findViewById<TextView>(R.id.dia).text = dia
        findViewById<TextView>(R.id.hora).text = hora
        findViewById<TextView>(R.id.idioma).text = idioma
        findViewById<TextView>(R.id.nivel).text = nivel
        findViewById<TextView>(R.id.lugar).text = lugar
        findViewById<TextView>(R.id.descripcion).text = descripcion
    }

    private fun setupButtonListeners() {
        val verCali = findViewById<Button>(R.id.btnVerCalificacionesReuniones)
        val unirse = findViewById<Button>(R.id.btnUnirse)
        val ruta = findViewById<Button>(R.id.btnRuta)
        val calificar = findViewById<Button>(R.id.btnCalificarReunion)
        val salir = findViewById<Button>(R.id.btnSalirReunion)
        val actualizarReunion = findViewById<Button>(R.id.btnActualizarReunion)
        val eliminarReunion = findViewById<Button>(R.id.btnEliminarReunion)
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        unirse.setOnClickListener {
            unirse.visibility = View.GONE
            calificar.visibility = View.VISIBLE
            salir.visibility = View.VISIBLE
            ruta.visibility = View.VISIBLE
        }

        actualizarReunion.setOnClickListener {
            val peticion = Intent(this, ActualizarReunion::class.java)
            startActivity(peticion)
        }

        eliminarReunion.setOnClickListener {
            //eliminar reuniones del json
            val peticion = Intent(this, VerReuniones::class.java)
            startActivity(peticion)
        }

        verCali.setOnClickListener {
            val peticion = Intent(this, VerCalificaciones::class.java)
            startActivity(peticion)
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