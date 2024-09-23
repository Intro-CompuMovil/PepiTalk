package com.example.pepitalk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AceptarTraductor : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aceptar_traductor)

        val aceptar = findViewById<Button>(R.id.buttonAceptar)
        val rechazar = findViewById<Button>(R.id.buttonRechazar)

        aceptarTraductor(aceptar, this)
        rechazarTraductor(rechazar, this)

    }
    fun aceptarTraductor(aceptar : Button, context: Context ){
        val irAMenuCliente = Intent(this, MenuCliente::class.java)
        aceptar.setOnClickListener {
            startActivity(irAMenuCliente)
            Toast.makeText(this,"se ha Aceptado el traductor", Toast. LENGTH_LONG).show()
        }
    }
    fun rechazarTraductor(rechazar : Button, context : Context){
        val irAMenuCliente = Intent(this, MenuCliente::class.java)
        rechazar.setOnClickListener {
            startActivity(irAMenuCliente)
            Toast.makeText(this,"se ha Rechazado el traductor", Toast. LENGTH_LONG).show()
        }
    }
}