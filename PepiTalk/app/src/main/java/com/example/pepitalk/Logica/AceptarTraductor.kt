package com.example.pepitalk.Logica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.R

class AceptarTraductor : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aceptar_traductor)

        val aceptar = findViewById<Button>(R.id.buttonAceptar)
        val rechazar = findViewById<Button>(R.id.buttonRechazar)
        val menuInicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        aceptarTraductor(aceptar, this)
        rechazarTraductor(rechazar, this)
        irPerfil(perfil, this)
        menuPrincipal(menuInicio, this)

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
    fun menuPrincipal(menuInicio: ImageButton, context: Context){
        if(Data.personaLog.tipo == "cliente"){
            val peticion = Intent(this, MenuCliente::class.java)
            startActivity(peticion)
        }else{
            val peticion = Intent(this, MenuTraductor::class.java)
            startActivity(peticion)
        }
    }
    fun irPerfil(perfil : ImageButton, context : Context){
        val irAPerfil = Intent(this, Perfil::class.java)
        perfil.setOnClickListener {
            startActivity(irAPerfil)
            Toast.makeText(this,"¡Tu perfil!", Toast. LENGTH_LONG).show()
        }
    }

}