package com.example.pepitalk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Grupo  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupo)

        val buttonVerMisGrupos = findViewById<Button>(R.id.buttonVerMisGrupos)
        val buttonVerGruposParaUnirme = findViewById<Button>(R.id.buttonVerGruposParaUnirme)
        val buttonCrearGrupo = findViewById<Button>(R.id.buttonCrearGrupo)

        verMisGrupos(buttonVerMisGrupos)
        verGruposParaUnirme(buttonVerGruposParaUnirme)
        crearGrupo(buttonCrearGrupo)
    }

    fun verMisGrupos( buttonVerMisGrupos : Button ){
        val irAMisGrupos = Intent(this, VerGrupos::class.java)
        buttonVerMisGrupos.setOnClickListener {
            startActivity(irAMisGrupos)
        }
    }
    fun verGruposParaUnirme( buttonVerGruposParaUnirme : Button ){
        val irAGruposParaUnirme = Intent(this, VerGrupos::class.java) // a que pantalla va este ??
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

}