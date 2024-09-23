package com.example.pepitalk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ActualizarOferta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_oferta)
        val botonActualizarOferta = findViewById<Button>(R.id.buttonActualizarOferta)
        botonActualizarOferta.setOnClickListener(){
            validarCampos()
        }
    }

    private fun validarCampos(){
        val dia = findViewById<EditText>(R.id.editTextDiaOfertaAct)
        val horaInicio = findViewById<EditText>(R.id.editTextHoraInicioAct)
        val horaFinal = findViewById<EditText>(R.id.editTextHoraFinalAct)
        val recompensa = findViewById<EditText>(R.id.editTextRecompensaAct)
        val lugar = findViewById<EditText>(R.id.editTextLugarOfertaAct)
        val descripcion = findViewById<EditText>(R.id.editTextDescripcionOfertaAct)

        if(dia.text.toString().isEmpty() || horaInicio.text.toString().isEmpty()|| horaFinal.text.toString().isEmpty()|| recompensa.text.toString().isEmpty() || lugar.text.toString().isEmpty()|| descripcion.text.toString().isEmpty()){
            Toast.makeText(this,"Por favor complete todos los campos" , Toast.LENGTH_SHORT).show()
        }
        else{
            validarRegistro(dia.text.toString(), horaInicio.text.toString(),horaFinal.text.toString(), recompensa.text.toString(), lugar.text.toString(), descripcion.text.toString())
        }
    }

    private fun validarRegistro(dia: String, horaInicio: String, horaFinal: String, recompensa: String, lugar: String, descripcion: String){
        //recorrer arreglo con los usuarios

        val diaFormateado = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val horaFormateado = DateTimeFormatter.ofPattern("HH:mm")
        val inicioTiempo = LocalTime.parse(horaInicio, horaFormateado)
        val finalTiempo = LocalTime.parse(horaFinal, horaFormateado)
        val diaTiempo = LocalDate.parse(dia, diaFormateado)
        if(inicioTiempo < finalTiempo){

            var ofertaCreado = Intent(this, Oferta::class.java)
            startActivity(ofertaCreado)
            Toast.makeText(this,"Se ha actualizado su oferta correctamente" , Toast.LENGTH_SHORT).show()


        }

    }
}