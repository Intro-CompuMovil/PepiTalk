package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.R

class MenuTraductor : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_traductor)

        val Trabajos = findViewById<Button>(R.id.buttonTrabajos)
        val Ofertas = findViewById<Button>(R.id.buttonOfertas)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        AccionBotonTrabajos(Trabajos)
        AccionBotonOfertas(Ofertas)
        AccionBotonPerfil(perfil)

    }
    fun AccionBotonTrabajos( Trabajos : Button){
        val irATrabajos = Intent(this, VerOfertas::class.java).apply {
            putExtra("tipo", "misTrabajos")
        }
        Trabajos.setOnClickListener {
            startActivity(irATrabajos)
        }
    }
    fun AccionBotonOfertas( Ofertas : Button){
        val irAOfertas = Intent(this, VerOfertas::class.java).apply {
            putExtra("tipo", "aceptarOfertas")
        }
        Ofertas.setOnClickListener{
            startActivity(irAOfertas)
        }
    }

    fun AccionBotonPerfil( perfil : ImageButton) {
        val irAPerfil = Intent(this, Perfil::class.java)
        perfil.setOnClickListener{
            startActivity(irAPerfil)
        }
    }
}