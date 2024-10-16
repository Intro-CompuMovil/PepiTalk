package com.example.pepitalk.Logica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pepitalk.R
import org.json.JSONObject
import org.json.JSONArray
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataGrupo
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val IniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)
        val Registrarse = findViewById<Button>(R.id.buttonRegistrarse)

        IniciarSesion.setOnClickListener {
            val intent = Intent(this, com.example.pepitalk.Logica.Login::class.java)
            startActivity(intent)
        }
        Registrarse.setOnClickListener {
            val intent = Intent(this, com.example.pepitalk.Logica.Registro::class.java)
            startActivity(intent)
        }

        cargarDatosDesdeJSON()

    }


    private fun cargarDatosDesdeJSON() {
        // Cargar Grupos
        Data.loadGruposFromJson(this)

        // Cargar Personas
        Data.loadPersonasFromJson(this)

        // Cargar Ofertas
        Data.loadOfertasFromJson(this)

        // Cargar Reuniones
        Data.loadReunionesFromJson(this)

    }

}