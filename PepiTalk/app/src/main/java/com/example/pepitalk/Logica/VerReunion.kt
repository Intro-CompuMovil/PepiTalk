package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataCalificaciones
import com.example.pepitalk.R


class VerReunion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_reunion)
        initializeTextViews()
        setupButtonListeners()
    }

    private fun roles(dueno: String?, integrantes: String?){
        if(dueno == Data.personaLog.usuario){
            botonesDueno()
        }else if (integrantes != null) {
            if(integrantes.contains(Data.personaLog.usuario)){
                botonesMiembros()
            }else{

            }
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
        val calificacionesString = intent.getStringExtra("calificaciones")
        val integrantes = intent.getStringExtra("integrantes")
        val dueno = intent.getStringExtra("dueno")

        val calificaciones = parseCalificaciones(calificacionesString)
        val promedio = calcularPromedio(calificaciones)

        findViewById<TextView>(R.id.nombre).text = nombre
        findViewById<TextView>(R.id.dia).text = dia
        findViewById<TextView>(R.id.hora).text = hora
        findViewById<TextView>(R.id.idioma).text = idioma
        findViewById<TextView>(R.id.nivel).text = nivel
        findViewById<TextView>(R.id.lugar).text = lugar
        findViewById<TextView>(R.id.descripcion).text = descripcion
        findViewById<TextView>(R.id.textCali).text = promedio.toString()

        roles(dueno, integrantes)
    }

    private fun parseCalificaciones(calificacionesString: String?): List<DataCalificaciones> {
        if (calificacionesString.isNullOrEmpty()) return emptyList()

        return calificacionesString
            .removeSurrounding("[", "]")
            .split("),")
            .mapNotNull {
                val parts = it.removeSurrounding("DataCalificaciones(", ")").split(", comentario=")
                if (parts.size == 2) {
                    val nota = parts[0].split("=")[1].toDoubleOrNull()
                    val comentario = parts[1]
                    if (nota != null) DataCalificaciones(nota, comentario) else null
                } else {
                    null
                }
            }
    }

    private fun calcularPromedio(calificaciones: List<DataCalificaciones>): String {
        val notas = calificaciones.map { it.nota }
        return if (notas.isNotEmpty()) {
            String.format("%.1f", notas.average())
        } else {
            "0.0"
        }
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
            val peticion = Intent(this, Reunion::class.java)
            startActivity(peticion)
        }

        verCali.setOnClickListener {
            val peticion = Intent(this, VerCalificaciones::class.java)
            val bundle = Bundle()
            val nombre = intent.getStringExtra("nombre")!!
            bundle.putString("nombre",nombre)
            bundle.putString("tipo", "reunion")
            peticion.putExtra("bundle", bundle)
            startActivity(peticion)
        }

        inicio.setOnClickListener {
            if(Data.personaLog.tipo == "Cliente"){
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

        ruta.setOnClickListener {
            val lugar = intent.getStringExtra("lugar")
            val peticion = Intent(this, Ruta::class.java)
            peticion.putExtra("destino", lugar )
            startActivity(peticion)
        }

        calificar.setOnClickListener {
            val peticion = Intent(this, Calificar::class.java)
            startActivity(peticion)
        }

        salir.setOnClickListener {
            val peticion = Intent(this, Reunion::class.java)
            startActivity(peticion)
        }
    }
}