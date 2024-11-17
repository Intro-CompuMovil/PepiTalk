package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ActualizarOferta : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private var idOferta: String = ""
    private val PATH_USERS = "users/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_oferta)

        auth = Firebase.auth

        val idOferta1 = intent.getStringExtra("id")
        if (idOferta1 != null) {
            idOferta = idOferta1
        }
        setUserPhoto()

        val botonActualizarOferta = findViewById<Button>(R.id.buttonActualizarOferta)
        val menuPrincipal = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        botonActualizarOferta.setOnClickListener(){
            validarCampos()
        }
        menuPrincipal.setOnClickListener(){
            irPrincipal()
        }
        perfil.setOnClickListener(){
            startActivity(Intent(this, Perfil::class.java))
        }
    }

    fun setUserPhoto() {
        val imageUser = findViewById<ImageButton>(R.id.butPerfil)
        var imageUrl1 = ""

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = database.getReference(PATH_USERS).child(userId)
            userRef.child("imageUrl").get().addOnSuccessListener { dataSnapshot ->
                imageUrl1 = dataSnapshot.value?.toString() ?: ""
                if (imageUrl1.isNotEmpty()) {
                    Glide.with(this)
                        .load(imageUrl1)
                        .into(imageUser)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al cargar la imagen del usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun irPrincipal(){
        if(Data.personaLog.tipo == "Cliente"){
            val peticion = Intent(this, MenuCliente::class.java)
            startActivity(peticion)
        }else{
            val peticion = Intent(this, MenuTraductor::class.java)
            startActivity(peticion)
        }
    }

    private fun validarCampos() {
        val dia = findViewById<EditText>(R.id.editTextDiaOfertaAct)
        val horaInicio = findViewById<EditText>(R.id.editTextHoraInicioAct)
        val horaFinal = findViewById<EditText>(R.id.editTextHoraFinalAct)
        val recompensa = findViewById<EditText>(R.id.editTextRecompensaAct)
        val lugar = findViewById<EditText>(R.id.editTextLugarOfertaAct)
        val descripcion = findViewById<EditText>(R.id.editTextDescripcionOfertaAct)

        if (dia.text.toString().isEmpty() && horaInicio.text.toString().isEmpty() && horaFinal.text.toString().isEmpty() && recompensa.text.toString().isEmpty() && lugar.text.toString().isEmpty() && descripcion.text.toString().isEmpty()) {
            Toast.makeText(this, "Por favor complete al menos un campo", Toast.LENGTH_SHORT).show()
        } else if (horaInicio.text.toString().isNotEmpty() && horaFinal.text.toString().isEmpty()) {
            Toast.makeText(this, "Por favor complete la hora final", Toast.LENGTH_SHORT).show()
        } else if (horaInicio.text.toString().isEmpty() && horaFinal.text.toString().isNotEmpty()) {
            Toast.makeText(this, "Por favor complete la hora inicial", Toast.LENGTH_SHORT).show()
        }
        else{
            validarRegistro(
                dia.text.toString(),
                horaInicio.text.toString(),
                horaFinal.text.toString(),
                recompensa.text.toString(),
                lugar.text.toString(),
                descripcion.text.toString()
            )
        }
    }

    private fun validarRegistro(dia: String, horaInicio: String, horaFinal: String, recompensa: String, lugar: String, descripcion: String) {
        val updates = mutableMapOf<String, Any>()

        if (dia.isNotEmpty()) {
            updates["fecha"] = dia
        }

        if (horaInicio.isNotEmpty() && horaFinal.isNotEmpty()) {
            val horaFormateado = DateTimeFormatter.ofPattern("HH:mm")
            val inicioTiempo = LocalTime.parse(horaInicio, horaFormateado)
            val finalTiempo = LocalTime.parse(horaFinal, horaFormateado)
            if (inicioTiempo < finalTiempo) {
                updates["horaInicio"] = inicioTiempo.toString()
                updates["horaFinal"] = finalTiempo.toString()
            } else {
                Toast.makeText(this, "La hora de inicio debe ser antes de la hora final", Toast.LENGTH_SHORT).show()
                return
            }
        }

        if (recompensa.isNotEmpty()) {
            updates["recompensa"] = recompensa
        }

        if (lugar.isNotEmpty()) {
            updates["lugar"] = lugar
        }

        if (descripcion.isNotEmpty()) {
            updates["descripcion"] = descripcion
        }

        if (updates.isNotEmpty()) {
            val ref = FirebaseDatabase.getInstance().getReference("ofertas/").child(idOferta)
            ref.updateChildren(updates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Se ha actualizado su oferta correctamente", Toast.LENGTH_SHORT).show()
                    val ofertaCreado = Intent(this, Oferta::class.java)
                    startActivity(ofertaCreado)
                } else {
                    Toast.makeText(this, "Error al actualizar la oferta", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "No hay campos para actualizar", Toast.LENGTH_SHORT).show()
        }
    }
}