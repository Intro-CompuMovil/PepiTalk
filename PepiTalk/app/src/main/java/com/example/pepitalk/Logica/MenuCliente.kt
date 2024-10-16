package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.R

class MenuCliente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cliente)

        val GruposButton = findViewById<Button>(R.id.buttonGrupos)
        val ReunionesButton = findViewById<Button>(R.id.buttonReuniones)
        val ServiciosButton = findViewById<Button>(R.id.buttonServicios)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        AccionBotonGrupos(GruposButton)
        AccionBotonReuniones(ReunionesButton)
        AccionBotonServicios(ServiciosButton)
        AccionBotonPerfil(perfil)

    }
    fun AccionBotonGrupos( GruposButton : Button){
        val irAGrupos = Intent(this, Grupo::class.java)
        GruposButton.setOnClickListener {
            startActivity(irAGrupos)
        }
    }
    fun AccionBotonReuniones( ReunionesButton : Button){
        val irAReuniones = Intent(this, Reunion::class.java)
        ReunionesButton.setOnClickListener {
            startActivity(irAReuniones)
        }
    }
    fun AccionBotonServicios( ServiciosButton : Button) {
        val irAServicios = Intent(this, Oferta::class.java)
        ServiciosButton.setOnClickListener {
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