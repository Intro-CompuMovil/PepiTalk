package com.example.pepitalk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MenuCliente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cliente)

        val Grupos = findViewById<Button>(R.id.buttonGrupos)
        val Reuniones = findViewById<Button>(R.id.buttonReuniones)
        val Servicios = findViewById<Button>(R.id.buttonServicios)
        val Perfil = findViewById<ImageButton>(R.id.imageButtonPerfil)

        AccionBotonGrupos(Grupos)
        AccionBotonReuniones(Reuniones)
        AccionBotonServicios(Servicios)
        AccionBotonPerfil(Perfil)




    }
    fun AccionBotonGrupos( Grupos : Button){
        val irAGrupos = Intent(this, Grupo::class.java)
        Grupos.setOnClickListener {
            startActivity(irAGrupos)
        }
    }
    fun AccionBotonReuniones( Reuniones : Button){
        val irAReuniones = Intent(this, Reunion::class.java)
        Reuniones.setOnClickListener {
            startActivity(irAReuniones)
        }
    }
    fun AccionBotonServicios( Servicios : Button) {
        val irAServicios = Intent(this, Oferta::class.java)
        Servicios.setOnClickListener {
            startActivity(irAServicios)
        }
    }
    fun AccionBotonPerfil( Perfil : ImageButton) {
        val irAPerfil = Intent(this, Perfil::class.java)
        Perfil.setOnClickListener{
            startActivity(irAPerfil)
        }
    }
}