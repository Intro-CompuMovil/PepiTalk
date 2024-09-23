package com.example.pepitalk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Reunion : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reunion)

        val ButtonVerMisReuniones = findViewById<Button>(R.id.buttonVerMisReuniones)
        val ButtonVerReunionesParaUnirme = findViewById<Button>(R.id.buttonVerReunionesParaUnirme)
        val ButtonCrearReunion = findViewById<Button>(R.id.buttonCrearReunion)

        VerMisReuniones(ButtonVerMisReuniones)
        VerReunionesParaUnirme(ButtonVerReunionesParaUnirme)
        CrearReunion(ButtonCrearReunion)
    }

    fun VerMisReuniones( ButtonVerMisReuniones : Button ){
        val irAMisReuniones = Intent(this, VerReuniones::class.java)
        ButtonVerMisReuniones.setOnClickListener {
            startActivity(irAMisReuniones)
        }
    }
    fun VerReunionesParaUnirme( ButtonVerReunionesParaUnirme : Button ){
        val irAReunionesParaUnirme = Intent(this, VerReuniones::class.java) // a que pantalla va este ??
        ButtonVerReunionesParaUnirme.setOnClickListener {
            startActivity(irAReunionesParaUnirme)
        }
    }
    fun CrearReunion( ButtonCrearReunion : Button ){
        val irACrearReuniones = Intent(this, CrearReunion::class.java)
        ButtonCrearReunion.setOnClickListener {
            startActivity(irACrearReuniones)
        }
    }

}