package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.R

class VerGrupo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_grupo)
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
        val reuniones = findViewById<Button>(R.id.btnVerReuniones)
        val verCali = findViewById<Button>(R.id.btnVerCalificaciones)
        val crearReunion = findViewById<Button>(R.id.btnCrearReunion)
        val actualizarGrupo = findViewById<Button>(R.id.btnActualizarGrupo)
        val eliminarGrupo = findViewById<Button>(R.id.btnEliminarGrupo)
        val unirse = findViewById<Button>(R.id.btnUnirse)

        unirse.visibility = View.GONE

        reuniones.visibility = View.VISIBLE
        verCali.visibility = View.VISIBLE
        crearReunion.visibility = View.VISIBLE
        actualizarGrupo.visibility = View.VISIBLE
        eliminarGrupo.visibility = View.VISIBLE
    }

    private fun botonesMiembros(){
        val reuniones = findViewById<Button>(R.id.btnVerReuniones)
        val verCali = findViewById<Button>(R.id.btnVerCalificaciones)
        val calificar = findViewById<Button>(R.id.btnCalificarGrupo)
        val salir = findViewById<Button>(R.id.btnSalirGrupo)
        val unirse = findViewById<Button>(R.id.btnUnirse)

        unirse.visibility = View.GONE

        reuniones.visibility = View.VISIBLE
        verCali.visibility = View.VISIBLE
        calificar.visibility = View.VISIBLE
        salir.visibility = View.VISIBLE
    }

    private fun initializeTextViews() {
        val nombre = intent.getStringExtra("nombre")
        val idioma = intent.getStringExtra("idioma")
        val nivel = intent.getStringExtra("nivel")
        val descripcion = intent.getStringExtra("descripcion")
        //inicializar calificación

        findViewById<TextView>(R.id.nombre).text = nombre
        findViewById<TextView>(R.id.textNomIdioma).text = idioma
        findViewById<TextView>(R.id.textNomNivel).text = nivel
        findViewById<TextView>(R.id.textDescri).text = descripcion
    }

    private fun setupButtonListeners() {
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        val verCali = findViewById<Button>(R.id.btnVerCalificaciones)
        val unirse = findViewById<Button>(R.id.btnUnirse)
        val reuniones = findViewById<Button>(R.id.btnVerReuniones)
        val calificar = findViewById<Button>(R.id.btnCalificarGrupo)
        val salir = findViewById<Button>(R.id.btnSalirGrupo)
        val crearReunion = findViewById<Button>(R.id.btnCrearReunion)
        val actualizarGrupo = findViewById<Button>(R.id.btnActualizarGrupo)
        val eliminarGrupo = findViewById<Button>(R.id.btnEliminarGrupo)

        verCali.setOnClickListener {
            val peticion = Intent(this, VerCalificaciones::class.java)
            startActivity(peticion)
        }

        unirse.setOnClickListener {
            unirse.visibility = View.GONE
            calificar.visibility = View.VISIBLE
            salir.visibility = View.VISIBLE
            reuniones.visibility = View.VISIBLE
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

        actualizarGrupo.setOnClickListener {
            val peticion = Intent(this, ActualizarGrupo::class.java)
            startActivity(peticion)
        }

        eliminarGrupo.setOnClickListener {
            Toast.makeText(this, "Grupo eliminado", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, VerGrupos::class.java)
            startActivity(peticion)
        }
    }
}
