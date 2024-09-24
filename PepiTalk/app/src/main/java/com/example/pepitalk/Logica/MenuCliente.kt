package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.R

class MenuCliente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cliente)

        val Grupos = findViewById<Button>(R.id.buttonGrupos)
        val Reuniones = findViewById<Button>(R.id.buttonReuniones)
        val Servicios = findViewById<Button>(R.id.buttonServicios)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        AccionBotonGrupos(Grupos)
        AccionBotonReuniones(Reuniones)
        AccionBotonServicios(Servicios)
        AccionBotonPerfil(perfil)

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
    fun AccionBotonPerfil( perfil : ImageButton) {
        val irAPerfil = Intent(this, Perfil::class.java)
        perfil.setOnClickListener{
            startActivity(irAPerfil)
        }
    }
}