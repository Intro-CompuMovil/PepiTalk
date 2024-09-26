package com.example.pepitalk.Logica

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.Persona
import com.example.pepitalk.R

class ActualizarCuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_cuenta)
        val botonActualizar = findViewById<Button>(R.id.buttonActualizar)
        val botonImagen = findViewById<ImageButton>(R.id.imageButton2)
        val menuPrincipal = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        botonImagen.isEnabled = false
        pedirPermiso(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), "Se necesita este permiso", Data.MY_PERMISSION_REQUEST_CAMERA)
        botonActualizar.setOnClickListener(){
            validarCampos()
        }
        botonImagen.setOnClickListener(){
            escogerImagen(botonImagen)
        }
        menuPrincipal.setOnClickListener(){
            irPrincipal()
        }
        perfil.setOnClickListener(){
            startActivity(Intent(this, Perfil::class.java))
        }
    }
    private fun irPrincipal(){
        //falta diferenciar por tipo
        if(Persona.personaLog.tipo == "Cliente"){
            startActivity(Intent(this, MenuCliente::class.java))
        }
        else{
            startActivity(Intent(this, MenuTraductor::class.java))
        }

    }

    private fun validarCampos(){
        val username = findViewById<EditText>(R.id.editTextUsuarioActualizar)
        val contrasena = findViewById<EditText>(R.id.editTextPasswordActualizar)
        val confContrasena = findViewById<EditText>(R.id.editTextConfirmarPasswordActualizar)
        val contrasenaAnt = findViewById<EditText>(R.id.editTextPasswordAnterior)

        if(username.text.toString().isEmpty() || contrasena.text.toString().isEmpty() || confContrasena.text.toString().isEmpty() || contrasenaAnt.text.toString().isEmpty()){
            Toast.makeText(this,"Por favor complete todos los campos" , Toast.LENGTH_SHORT).show()
        }
        else{
            validarRegistro(username.text.toString(), contrasena.text.toString(), confContrasena.text.toString(), contrasenaAnt.text.toString())
        }
    }

    private fun validarRegistro(usuario: String, contrasena: String, confContrasena: String, contrasenaAnt: String){
        //recorrer arreglo con los usuarios
        val usuTemp = "gabriel13"
        val otroTemp = "juafra1"
        val claveTemp = "clave123"
        val tipo = "Cliente"

        if(otroTemp == usuario){
            if(claveTemp == contrasenaAnt){
                if(contrasena == confContrasena){
                    if(tipo == "Cliente"){

                        var clienteRegistrado = Intent(this, MenuCliente::class.java)
                        clienteRegistrado.putExtra("usuario", usuario)
                        startActivity(clienteRegistrado)
                        Toast.makeText(this,"Se ha actualizado correctamente" , Toast.LENGTH_SHORT).show()
                    }
                    else{
                        var traductorRegistrado = Intent(this, MenuTraductor::class.java)
                        traductorRegistrado.putExtra("usuario", usuario)
                        startActivity(traductorRegistrado)
                        Toast.makeText(this,"Se ha actualizado correctamente" , Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this,"Las contraseñas no coinciden" , Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Contraseña Anterior incorrecta" , Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,"El usuario ya existe" , Toast.LENGTH_SHORT).show()
        }

    }

    private fun escogerImagen(botonImagen: ImageButton){
        val options = arrayOf("Tomar foto", "Seleccionar de galería")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar opción")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera() // Abrir cámara
                1 -> openGallery() // Abrir galería
            }
        }
        builder.show()
    }

    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, Data.MY_PERMISSION_REQUEST_CAMERA)
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Data.MY_PERMISSION_REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val botonImagen = findViewById<ImageButton>(R.id.imageButton2)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Data.MY_PERMISSION_REQUEST_CAMERA -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap

                    // Muestra la imagen o guárdala
                    botonImagen.setImageBitmap(imageBitmap)
                }
                Data.MY_PERMISSION_REQUEST_GALLERY -> {
                    val selectedImageUri = data?.data
                    // Muestra la imagen seleccionada o guárdala
                    botonImagen.setImageURI(selectedImageUri)
                }
            }
        }
    }
    private fun pedirPermiso(context: Activity, permisos: Array<String>, justificacion: String, idCode: Int) {

        val permisosNoConcedidos = permisos.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permisosNoConcedidos.isNotEmpty()) {
            if (permisosNoConcedidos.any { ActivityCompat.shouldShowRequestPermissionRationale(context, it) }) {
                // Mostrar justificación adicional si es necesario.
            }
            ActivityCompat.requestPermissions(context, permisosNoConcedidos.toTypedArray(), idCode)
        } else {
            Toast.makeText(this,"¡Ahora puede agregar una foto a su perfil, grupos o reuniones!" , Toast.LENGTH_SHORT).show()
            val botonImagen = findViewById<ImageButton>(R.id.imageButton2)
            botonImagen.isEnabled = true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (arePermissionsGranted(grantResults)) {
            when (requestCode) {
                Data.MY_PERMISSION_REQUEST_CAMERA -> {
                    Toast.makeText(this, "Permisos para la cámara concedidos", Toast.LENGTH_SHORT).show()
                    val botonImagen = findViewById<ImageButton>(R.id.imageButton2)
                    botonImagen.isEnabled = true
                }
                Data.MY_PERMISSION_REQUEST_GALLERY -> {
                    Toast.makeText(this, "Permisos para la galería concedidos", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Otros códigos de permiso
                }
            }
        } else {
            when (requestCode) {
                Data.MY_PERMISSION_REQUEST_CAMERA -> {
                    Toast.makeText(this, "Permisos para la cámara denegados", Toast.LENGTH_SHORT).show()
                }
                Data.MY_PERMISSION_REQUEST_GALLERY -> {
                    Toast.makeText(this, "Permisos para la galería denegados", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Otros permisos denegados
                }
            }
        }
    }

    private fun arePermissionsGranted(grantResults: IntArray): Boolean {
        return grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    }


}