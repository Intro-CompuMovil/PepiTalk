package com.example.pepitalk.Logica

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.example.pepitalk.Datos.Persona
import com.example.pepitalk.R

class Ruta : AppCompatActivity() {

    private val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1001
    private lateinit var imageViewGPS: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruta)
        val foto = findViewById<ImageView>(R.id.imageViewGPS)
        imageViewGPS = foto
        val menuInicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        val reunion = findViewById<Button>(R.id.btnDevolverReunion)
        // Solicita el permiso de acceso a la ubicación
        pedirPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Se necesita este permiso para acceder a tu ubicación.", PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
        irPerfil(perfil, this)
        menuPrincipal(menuInicio, this)
        devolverReunion(reunion, this)
    }

    fun devolverReunion(reunion: Button, context: Context){
        val irAReunion = Intent(this, VerReunion::class.java)
        reunion.setOnClickListener {
            startActivity(irAReunion)
        }
    }

    private fun pedirPermiso(permiso: String, justificacion: String, idCode: Int) {

        if (ContextCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {
                // Mostrar una justificación si es necesario

            }
            // Solicita el permiso
            ActivityCompat.requestPermissions(this, arrayOf(permiso), idCode)
            imageViewGPS.visibility = ImageView.GONE
        } else {
            // Si ya tiene el permiso, muestra un mensaje y accede a la ubicación
            //initLocationAccess()
            imageViewGPS.visibility = ImageView.VISIBLE
        }
    }

    // Gestionar la respuesta del usuario
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Si el permiso fue concedido, accede a la ubicación
                    //initLocationAccess()
                    imageViewGPS.visibility = ImageView.VISIBLE
                } else {
                    // Si el permiso fue denegado, muestra un mensaje
                    imageViewGPS.visibility = ImageView.GONE
                    Toast.makeText(this, "Funcionalidades limitadas por falta de acceso a la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // Ignorar otras solicitudes de permisos.
            }
        }
    }
    fun menuPrincipal(menuInicio: ImageButton, context: Context){
        menuInicio.setOnClickListener {
            if(Persona.personaLog.tipo == "Cliente"){
                startActivity(Intent(this, MenuCliente::class.java))
            }
            else{
                startActivity(Intent(this, MenuTraductor::class.java))
            }
            Toast.makeText(this,"Yendo al menú", Toast. LENGTH_LONG).show()
        }
    }
    fun irPerfil(perfil : ImageButton, context : Context){
        val irAPerfil = Intent(this, Perfil::class.java)
        perfil.setOnClickListener {
            startActivity(irAPerfil)
            Toast.makeText(this,"¡Tu perfil!", Toast. LENGTH_LONG).show()
        }
    }
}