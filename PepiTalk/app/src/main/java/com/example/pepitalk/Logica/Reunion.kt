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

class Reunion : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reunion)

        val ButtonVerMisReuniones = findViewById<Button>(R.id.buttonVerMisReuniones)
        val ButtonVerReunionesParaUnirme = findViewById<Button>(R.id.buttonVerReunionesParaUnirme)
        val ButtonCrearReunion = findViewById<Button>(R.id.buttonCrearReunion)
        val menuInicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        VerMisReuniones(ButtonVerMisReuniones)
        VerReunionesParaUnirme(ButtonVerReunionesParaUnirme)
        CrearReunion(ButtonCrearReunion)
        irPerfil(perfil, this)
        menuPrincipal(menuInicio, this)
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
    fun menuPrincipal(menuInicio: ImageButton, context: Context){
        if(Data.personaLog.tipo == "Cliente"){
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