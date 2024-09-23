package com.example.pepitalk

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest

class Ruta : AppCompatActivity() {

    private val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruta)
        // Solicita el permiso de acceso a la ubicación
        pedirPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Se necesita este permiso para acceder a tu ubicación.", PERMISSION_REQUEST_ACCESS_FINE_LOCATION)

    }

    private fun pedirPermiso(permiso: String, justificacion: String, idCode: Int) {

        if (ContextCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {
                // Mostrar una justificación si es necesario

            }
            // Solicita el permiso
            ActivityCompat.requestPermissions(this, arrayOf(permiso), idCode)
        } else {
            // Si ya tiene el permiso, muestra un mensaje y accede a la ubicación
            //initLocationAccess()
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
                } else {
                    // Si el permiso fue denegado, muestra un mensaje
                    Toast.makeText(this, "Funcionalidades limitadas por falta de acceso a la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // Ignorar otras solicitudes de permisos.
            }
        }
    }

}