package com.example.pepitalk.Logica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CrearOferta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    }

    private fun irPrincipal(){

        startActivity(Intent(this, MenuCliente::class.java))
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

            var ofertaCreado = Intent(this, Oferta::class.java)
            startActivity(ofertaCreado)
            Toast.makeText(this,"Se ha creado su oferta correctamente" , Toast.LENGTH_SHORT).show()


        }

    }
}