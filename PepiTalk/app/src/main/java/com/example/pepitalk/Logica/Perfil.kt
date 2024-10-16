package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.Persona
import com.example.pepitalk.R

class Perfil :  AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        setupLoggedName()
        setupButtonListeners()
    }

    private fun setupLoggedName(){
        val nombre = findViewById<TextView>(R.id.nombre)
        val correo = findViewById<TextView>(R.id.correo)
        nombre.setText(Data.personaLog.nombre)
        correo.setText(Data.personaLog.correo)
    }

    private fun setupButtonListeners() {
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val actualizar = findViewById<Button>(R.id.btnActualizar)
        val eliminar = findViewById<Button>(R.id.btnEliminar)
        val cerrar = findViewById<Button>(R.id.btnCerrarSesion)

        inicio.setOnClickListener {
            if(Data.personaLog.tipo == "cliente"){
                val peticion = Intent(this, MenuCliente::class.java)
                startActivity(peticion)
            }else{
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
            val peticion = Intent(this, com.example.pepitalk.Logica.MainActivity::class.java)
            startActivity(peticion)
        }

        cerrar.setOnClickListener {
            Toast.makeText(this, "Log out exitoso ", Toast.LENGTH_LONG).show()
            val peticion = Intent(this, MainActivity::class.java)
            startActivity(peticion)
        }
    }

}