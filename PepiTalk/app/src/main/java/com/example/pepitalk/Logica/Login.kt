package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Data
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

        for( i in 0 until Data.personas.size){
            if(usuario == Data.personas[i].usuario && contrasena == Data.personas[i].contrasena){
                found = true
                Data.personaLog.tipo = Data.personas[i].tipo
                Data.personaLog.nombre = Data.personas[i].nombre
                Data.personaLog.usuario = Data.personas[i].usuario
                Data.personaLog.contrasena = Data.personas[i].contrasena
                Data.personaLog.correo = Data.personas[i].correo
                Data.personaLog.calificaciones = Data.personas[i].calificaciones
                if(Data.personas[i].tipo == "Cliente"){
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