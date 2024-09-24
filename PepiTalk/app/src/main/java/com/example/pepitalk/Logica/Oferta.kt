package com.example.pepitalk.Logica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.R

class Oferta : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oferta)

        val BotonVerOfertas= findViewById<Button>(R.id.buttonVerOfertas)
        val BotonCrearOfertas = findViewById<Button>(R.id.buttonCrearOfertas)
        val menuInicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        VerOfertas(BotonVerOfertas)
        CrearOfertas(BotonCrearOfertas)
        irPerfil(perfil, this)
        menuPrincipal(menuInicio, this)
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

    fun menuPrincipal(menuInicio: ImageButton, context: Context){
        val irAMenuPrincipal = Intent(this, MenuCliente::class.java)
        menuInicio.setOnClickListener {
            startActivity(irAMenuPrincipal)
            Toast.makeText(this,"Yendo al menú", Toast. LENGTH_LONG).show()
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