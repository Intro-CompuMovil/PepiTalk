package com.example.pepitalk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Oferta : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oferta)

        val BotonVerOfertas= findViewById<Button>(R.id.buttonVerOfertas)
        val BotonCrearOfertas = findViewById<Button>(R.id.buttonCrearOfertas)

        VerOfertas(BotonVerOfertas)
        CrearOfertas(BotonCrearOfertas)
    }

    fun VerOfertas( VerOfertas : Button){
        val irAVerOfertas = Intent(this, VerOfertas::class.java)
        VerOfertas.setOnClickListener{
            startActivity(irAVerOfertas)
        }
    }
    fun CrearOfertas( CrearOfertas : Button){
        val irACrearOfertas = Intent(this, CrearOfertas::class.java)
        CrearOfertas.setOnClickListener{
            startActivity(irACrearOfertas)
        }
    }
}