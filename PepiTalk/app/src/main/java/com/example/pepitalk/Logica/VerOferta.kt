package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.R


class VerOferta : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_oferta)
        initializeTextViews()
        setupButtonListeners()
        if(/*dueño*/true){
            botonesDueno()
        }else if(/*acepto el trabajo*/!true){
            botonesTraductor()
        }
    }

    private fun botonesDueno(){
        val aceptar = findViewById<Button>(R.id.btnAceptar)
        val calificar = findViewById<Button>(R.id.btnCalificarTraductor)
        val actualizar = findViewById<Button>(R.id.btnActualizarContrato)
        val eliminar = findViewById<Button>(R.id.btnEliminarContrato)

        aceptar.visibility = View.GONE
        calificar.visibility = View.VISIBLE
        actualizar.visibility = View.VISIBLE
        eliminar.visibility = View.VISIBLE
    }

    private fun botonesTraductor(){
        val aceptar = findViewById<Button>(R.id.btnAceptar)
        val rechazar = findViewById<Button>(R.id.btnCancelarContrato)
        val ruta = findViewById<Button>(R.id.btnRuta)

        aceptar.visibility = View.GONE
        rechazar.visibility = View.VISIBLE
        ruta.visibility = View.VISIBLE
    }

    private fun initializeTextViews() {
        val language = intent.getStringExtra("idioma")
        val date = intent.getStringExtra("fecha")
        val start = intent.getStringExtra("horaInicio")
        val final = intent.getStringExtra("horaFin")
        val place = intent.getStringExtra("lugar")
        val descripcion = intent.getStringExtra("descripcion")

        findViewById<TextView>(R.id.idiomaOfe).text = language
        findViewById<TextView>(R.id.fechaOfe).text = date
        findViewById<TextView>(R.id.horaInicioOfe).text = start
        findViewById<TextView>(R.id.horaFinOfe).text = final
        findViewById<TextView>(R.id.lugarOfe).text = place
        findViewById<TextView>(R.id.descripcionOfe).text = descripcion

    }

    private fun setupButtonListeners() {
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        val aceptar = findViewById<Button>(R.id.btnAceptar)
        val rechazar = findViewById<Button>(R.id.btnCancelarContrato)
        val calificar = findViewById<Button>(R.id.btnCalificarTraductor)
        val ruta = findViewById<Button>(R.id.btnRuta)
        val actualizarOferta = findViewById<Button>(R.id.btnActualizarContrato)
        val eliminarOferta = findViewById<Button>(R.id.btnEliminarContrato)

        aceptar.setOnClickListener {
            aceptar.visibility = View.GONE
            ruta.visibility= View.VISIBLE
            rechazar.visibility= View.VISIBLE
        }

        actualizarOferta.setOnClickListener {
            val peticion = Intent(this, ActualizarOferta::class.java)
            startActivity(peticion)
        }

        eliminarOferta.setOnClickListener {
            //eliminar del json
            Toast.makeText(this, "Oferta eliminada", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, VerOfertas::class.java)
            startActivity(peticion)
        }

        inicio.setOnClickListener {
            if(Data.personaLog.tipo == "cliente"){
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

        rechazar.setOnClickListener {
            Toast.makeText(this, "Oferta rechazada", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, VerOfertas::class.java)
            startActivity(peticion)
        }

        calificar.setOnClickListener {
            val peticion = Intent(this, Calificar::class.java)
            startActivity(peticion)
        }

        ruta.setOnClickListener {
            val peticion = Intent(this, Ruta::class.java)
            startActivity(peticion)
        }
    }

}