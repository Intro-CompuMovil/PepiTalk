package com.example.pepitalk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Perfil :  AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val actualizar = findViewById<Button>(R.id.btnActualizar)
        val eliminar = findViewById<Button>(R.id.btnEliminar)
        val cerrar = findViewById<Button>(R.id.btnCerrarSesion)

        inicio.setOnClickListener {
            if (/*es cliente*/true) {
                val peticion = Intent(this, MenuCliente::class.java)
                startActivity(peticion)
            } else {
                val peticion = Intent(this, MenuTraductor::class.java)
                startActivity(peticion)
            }
        }

        actualizar.setOnClickListener {
            val peticion = Intent(this, ActualizarCuenta::class.java)
            startActivity(peticion)
        }

        eliminar.setOnClickListener {
            Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, MainActivity::class.java)
            startActivity(peticion)
        }

        cerrar.setOnClickListener {
            Toast.makeText(this, "Log out exitoso ", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, MainActivity::class.java)
            startActivity(peticion)
        }
    }

}