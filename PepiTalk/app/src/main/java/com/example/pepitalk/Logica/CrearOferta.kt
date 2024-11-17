package com.example.pepitalk.Logica

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataOferta
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CrearOferta : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference



    private lateinit var dia : EditText
    private lateinit var idioma : EditText
    private lateinit var horaInicio : EditText
    private lateinit var horaFinal : EditText
    private lateinit var recompensa : EditText
    private lateinit var lugar : EditText
    private lateinit var descripcion : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_oferta)
        val botonCrearOferta = findViewById<Button>(R.id.buttonCrearOferta)
        val menuPrincipal = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        botonCrearOferta.setOnClickListener(){
            validarCampos()
        }
        menuPrincipal.setOnClickListener(){
            irPrincipal()
        }
        perfil.setOnClickListener(){
            startActivity(Intent(this, Perfil::class.java))
        }

        dia = findViewById(R.id.editTextDiaOferta)
        idioma = findViewById(R.id.editTextIdiomaOferta)
        horaInicio = findViewById(R.id.editTextHoraInicio)
        horaFinal = findViewById(R.id.editTextHoraFinal)
        recompensa = findViewById(R.id.editTextRecompensa)
        lugar = findViewById(R.id.editTextLugarOferta)
        descripcion = findViewById(R.id.editTextDescripcionOferta)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
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

    private fun validarCampos(){
        val dia = findViewById<EditText>(R.id.editTextDiaOferta)
        val idioma = findViewById<EditText>(R.id.editTextIdiomaOferta)
        val horaInicio = findViewById<EditText>(R.id.editTextHoraInicio)
        val horaFinal = findViewById<EditText>(R.id.editTextHoraFinal)
        val recompensa = findViewById<EditText>(R.id.editTextRecompensa)
        val lugar = findViewById<EditText>(R.id.editTextLugarOferta)
        val descripcion = findViewById<EditText>(R.id.editTextDescripcionOferta)

        if(dia.text.toString().isEmpty() || idioma.text.toString().isEmpty() || horaInicio.text.toString().isEmpty()|| horaFinal.text.toString().isEmpty()|| recompensa.text.toString().isEmpty() || lugar.text.toString().isEmpty()|| descripcion.text.toString().isEmpty()){
            Toast.makeText(this,"Por favor complete todos los campos" , Toast.LENGTH_SHORT).show()
        }
        else{
            validarRegistro(dia.text.toString(), idioma.text.toString(), horaInicio.text.toString(),horaFinal.text.toString(), recompensa.text.toString(), lugar.text.toString(), descripcion.text.toString())
        }
    }

    private fun validarRegistro(dia: String, idioma: String, horaInicio: String, horaFinal: String, recompensa: String, lugar: String, descripcion: String){
        //recorrer arreglo con los usuarios

        val diaFormateado = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val horaFormateado = DateTimeFormatter.ofPattern("HH:mm")
        val inicioTiempo = LocalTime.parse(horaInicio, horaFormateado)
        val finalTiempo = LocalTime.parse(horaFinal, horaFormateado)
        val diaTiempo = LocalDate.parse(dia, diaFormateado)
        if(inicioTiempo < finalTiempo){
            Data.listaOfertas.add(DataOferta())
            crearOferta(idioma, dia, horaInicio, horaFinal, recompensa, lugar, descripcion)
            var ofertaCreado = Intent(this, Oferta::class.java)
            startActivity(ofertaCreado)
            Toast.makeText(this,"Se ha creado su oferta correctamente" , Toast.LENGTH_SHORT).show()


        }
    }

    private fun crearOferta(idioma: String, fecha: String, horaInicio: String,
                            horaFinal: String, recompensa: String, lugar: String,
                            descripcion: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val dueno = user.uid // obtener el id del dueño
            val ofertaId = database.child("ofertas").push().key
            if (ofertaId != null) {
                val oferta = DataOferta(
                    // Guardando los datos de la oferta en la base de datos
                    idioma = idioma, fecha = fecha, horaInicio = horaInicio, horaFinal = horaFinal,
                    recompensa = recompensa, lugar = lugar, descripcion = descripcion, dueno = dueno,
                    trabajador = "", aceptado = false)
                    // Guardar la oferta en la base de datos
                database.child("ofertas").child(ofertaId).setValue(oferta)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "Oferta creada exitosamente.", Toast.LENGTH_SHORT).show()
                            updateUI()
                        } else {
                            Toast.makeText(baseContext, "Database update failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } else {
            Toast.makeText(baseContext, "User not authenticated.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        val intent = Intent(this, MenuCliente::class.java)
        startActivity(intent)
    }




}