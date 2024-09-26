package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Persona
import com.example.pepitalk.R

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val botonIS = findViewById<Button>(R.id.buttonLogin)
        val botonRegistrar = findViewById<Button>(R.id.buttonRegistrateLogin)
        var registrar = Intent(this, Registro::class.java)
        botonIS.setOnClickListener(){
            validarCampos()
        }
        botonRegistrar.setOnClickListener(){
            startActivity(registrar)
        }
    }

    private fun validarCampos(){
        val username = findViewById<EditText>(R.id.editTextUsuario)
        val contrasena = findViewById<EditText>(R.id.editTextPassword)

        if(username.text.toString().isEmpty() || contrasena.text.toString().isEmpty()){
            Toast.makeText(this,"Por favor complete todos los campos" , Toast.LENGTH_SHORT).show()
        }
        else{
            validarLogin(username.text.toString(), contrasena.text.toString())
        }
    }

    private fun validarLogin(usuario: String, contrasena: String){
        //recorrer arreglo con los usuarios
        var found = false

        for( i in 0 until Persona.personas.size){
            if(usuario == Persona.personas[i].usuario && contrasena == Persona.personas[i].contrasena){
                found = true
                Persona.personaLog.tipo = Persona.personas[i].tipo
                Persona.personaLog.nombre = Persona.personas[i].nombre
                Persona.personaLog.usuario = Persona.personas[i].usuario
                Persona.personaLog.contrasena = Persona.personas[i].contrasena
                Persona.personaLog.correo = Persona.personas[i].correo
                if(Persona.personas[i].tipo == "Cliente"){
                    var clienteLoggedIn = Intent(this, MenuCliente::class.java)
                    clienteLoggedIn.putExtra("usuario", usuario)
                    startActivity(clienteLoggedIn)
                }
                else{
                    var traductorLoggedIn = Intent(this, MenuTraductor::class.java)
                    traductorLoggedIn.putExtra("usuario", usuario)
                    startActivity(traductorLoggedIn)
                }
            }

        }
        if(!found){
            Toast.makeText(this,"No se pudo iniciar sesión, revise el usuario y la contraseña" , Toast.LENGTH_SHORT).show()

        }

    }
}