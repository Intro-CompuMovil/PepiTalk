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

class Grupo  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupo)

        val buttonVerMisGrupos = findViewById<Button>(R.id.buttonVerMisGrupos)
        val buttonVerGruposParaUnirme = findViewById<Button>(R.id.buttonVerGruposParaUnirme)
        val buttonCrearGrupo = findViewById<Button>(R.id.buttonCrearGrupo)
        val menuInicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        verMisGrupos(buttonVerMisGrupos)
        verGruposParaUnirme(buttonVerGruposParaUnirme)
        crearGrupo(buttonCrearGrupo)
        irPerfil(perfil, this)
        menuPrincipal(menuInicio, this)
    }

    fun verMisGrupos( buttonVerMisGrupos : Button ){
        val irAMisGrupos = Intent(this, VerGrupos::class.java).apply{
            putExtra("tipo", "misGrupos")
        }
        buttonVerMisGrupos.setOnClickListener {
            startActivity(irAMisGrupos)
        }
    }
    fun verGruposParaUnirme( buttonVerGruposParaUnirme : Button ){

        val irAGruposParaUnirme = Intent(this, VerGrupos::class.java).apply {
            putExtra("tipo", "gruposParaUnirme")
        }
        buttonVerGruposParaUnirme.setOnClickListener {
            startActivity(irAGruposParaUnirme)
        }
    }
    fun crearGrupo( buttonCrearGrupo : Button ){
        val irACrearGrupo = Intent(this, CrearGrupo::class.java)
        buttonCrearGrupo.setOnClickListener {
            startActivity(irACrearGrupo)
        }
    }
    fun menuPrincipal(menuInicio: ImageButton, context: Context){
        menuInicio.setOnClickListener {
            if(Data.personaLog.tipo == "Cliente"){
                val peticion = Intent(this, MenuCliente::class.java)
                startActivity(peticion)
            }else{
                val peticion = Intent(this, MenuTraductor::class.java)
                startActivity(peticion)
            }
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