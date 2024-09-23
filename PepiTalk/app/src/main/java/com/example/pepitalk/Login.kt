package com.example.pepitalk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
        val userTemp = "gabriel13"
        val claveTemp = "clave123"
        val tipoTemp = "Cliente"


        if(usuario == userTemp && claveTemp == contrasena){
            if(tipoTemp == "Cliente"){
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
        else{
            Toast.makeText(this,"No se pudo iniciar sesión, revise el usuario y la contraseña" , Toast.LENGTH_SHORT).show()
        }
    }
}