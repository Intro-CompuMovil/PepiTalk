package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var username: EditText
    private lateinit var contrasena: EditText
    private lateinit var botonRegistrar: Button
    private lateinit var botonIS: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        username = findViewById<EditText>(R.id.editTextUsuario)
        contrasena = findViewById<EditText>(R.id.editTextPassword)
        botonIS = findViewById<Button>(R.id.buttonLogin)
        botonRegistrar = findViewById<Button>(R.id.buttonRegistrateLogin)

        var registrar = Intent(this, Registro::class.java)

        botonIS.setOnClickListener {
            if (validarCampos()) {
                if (isEmailValid(username.text.toString())) {
                    signIn(username.text.toString(), contrasena.text.toString())
                } else {
                    Toast.makeText(baseContext, "Correo invalido.", Toast.LENGTH_SHORT).show()
                    username.setText("")
                    contrasena.setText("")
                }
            }
        }

        botonRegistrar.setOnClickListener(){
            startActivity(registrar)
        }
    }

    private fun validarCampos(): Boolean {
        var valid = true

        username = findViewById<EditText>(R.id.editTextUsuario)
        contrasena = findViewById<EditText>(R.id.editTextPassword)

        val email = username.text.toString()
        if (TextUtils.isEmpty(email)) {
            username.error = "Required."
            valid = false
        } else {
            username.error = null
        }
        val password = contrasena.text.toString()
        if (TextUtils.isEmpty(password)) {
            contrasena.error = "Required."
            valid = false
        } else {
            contrasena.error = null
        }

        if (!valid){
            Toast.makeText(this,"Por favor complete todos los campos" , Toast.LENGTH_SHORT).show()

        }
        return valid
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, MenuCliente::class.java) // Arreglar esto
            intent.putExtra("user", currentUser.email)
            startActivity(intent)
        } else {
            username.setText("")
            contrasena.setText("")
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseLogin", "signInWithEmail:success")
                    val user = auth.currentUser
                    user?.let {
                        revisarTipo(it.uid)
                    }
                } else {
                    Log.w("FirebaseLogin", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun revisarTipo(userId: String) {
        val database = Firebase.database
        val userRef = database.getReference("users").child(userId)

        userRef.get().addOnSuccessListener { dataSnapshot ->
            val userType = dataSnapshot.child("tipo").getValue(String::class.java)
            if (userType != null) {
                navigateToMenu(userType)
            } else {
                Toast.makeText(baseContext, "Tipo no encontrado", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(baseContext, "Failed to retrieve user type.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMenu(userType: String) {
        if (userType == "Cliente") {
            val clienteLoggedIn = Intent(this, MenuCliente::class.java)
            startActivity(clienteLoggedIn)
        } else {
            val traductorLoggedIn = Intent(this, MenuTraductor::class.java)
            startActivity(traductorLoggedIn)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        if (!email.contains("@") ||
            !email.contains(".") ||
            email.length < 5)
            return false
        return true
    }

}