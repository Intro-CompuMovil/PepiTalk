package com.example.pepitalk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MenuTraductor : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_traductor)

        val Trabajos = findViewById<Button>(R.id.buttonTrabajos)
        val Ofertas = findViewById<Button>(R.id.buttonOfertas)
        val Perfil = findViewById<ImageButton>(R.id.imageButtonPerfil)

        AccionBotonTrabajos(Trabajos)
        AccionBotonOfertas(Ofertas)
        AccionBotonPerfil(Perfil)

    }
    fun AccionBotonTrabajos( Trabajos : Button){
        val irATrabajos = Intent(this, Grupo::class.java)
        Trabajos.setOnClickListener {
            startActivity(irATrabajos)
        }
    }
    fun AccionBotonOfertas( Ofertas : Button){
        val irAOfertas = Intent(this, Reunion::class.java)
        Ofertas.setOnClickListener{
            startActivity(irAOfertas)
        }
    }

    fun AccionBotonPerfil( Perfil : ImageButton) {
        val irAPerfil = Intent(this, Perfil::class.java)
        Perfil.setOnClickListener{
            startActivity(irAPerfil)
        }
    }
}