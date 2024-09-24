package com.example.pepitalk.Logica

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.Persona
import com.example.pepitalk.R


class Registro : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        val botonRegistro = findViewById<Button>(R.id.buttonRegistrar)
        val botonImagen = findViewById<ImageButton>(R.id.imageButton)
        botonImagen.isEnabled = false
        pedirPermiso(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), "Se necesita este permiso", Data.MY_PERMISSION_REQUEST_CAMERA)
        botonRegistro.setOnClickListener(){
            validarCampos()
        }
        botonImagen.setOnClickListener(){
            escogerImagen(botonImagen)
        }
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {



    }
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun validarCampos(){
        val tipo = findViewById<Spinner>(R.id.spinner)
        tipo.onItemSelectedListener = this
        val nombre = findViewById<EditText>(R.id.editTextNombreRegistro)
        val username = findViewById<EditText>(R.id.editTextUsuarioRegistro)
        val contrasena = findViewById<EditText>(R.id.editTextPasswordRegistro)
        val confContrasena = findViewById<EditText>(R.id.editTextConfirmarPasswordRegistro)
        val correo = findViewById<EditText>(R.id.editTextCorreoRegistro)

        if(nombre.text.toString().isEmpty() ||username.text.toString().isEmpty() || contrasena.text.toString().isEmpty() || confContrasena.text.toString().isEmpty() || correo.text.toString().isEmpty()){
            Toast.makeText(this,"Por favor complete todos los campos" , Toast.LENGTH_SHORT).show()
        }
        else{
            validarRegistro(nombre.text.toString(), username.text.toString(), tipo.selectedItem.toString(), contrasena.text.toString(), confContrasena.text.toString(), correo.text.toString())
        }
    }

    private fun validarRegistro(nombre: String, usuario: String, tipo: String, contrasena: String, confContrasena: String, correo: String){
        //recorrer arreglo con los usuarios
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        var found = false

        for( i in 0 until Persona.personas.size){
            if(usuario == Persona.personas[i].usuario|| correo == Persona.personas[i].correo){
                found = true
                Toast.makeText(this,"El usuario ya existe o el correo ya se ha utilizado" , Toast.LENGTH_SHORT).show()
            }
        }
        if(!found){
            if(contrasena == confContrasena){
                if(emailRegex.matches(correo)){
                    var newUser = Persona(tipo, usuario, nombre,contrasena, correo)
                    Persona.personas.add(newUser)
                    Persona.personaLog.tipo = tipo
                    Persona.personaLog.usuario = usuario
                    Persona.personaLog.nombre = nombre
                    Persona.personaLog.contrasena = contrasena
                    Persona.personaLog.correo = correo
                    if(tipo == "Cliente"){
                        var clienteRegistrado = Intent(this, MenuCliente::class.java)
                        startActivity(clienteRegistrado)
                        Toast.makeText(this,"Se ha registrado correctamente" , Toast.LENGTH_SHORT).show()
                    }
                    else{
                        var traductorRegistrado = Intent(this, MenuTraductor::class.java)
                        startActivity(traductorRegistrado)
                        Toast.makeText(this,"Se ha registrado correctamente" , Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this,"El correo no es válido" , Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Las contraseñas no coinciden" , Toast.LENGTH_SHORT).show()
            }
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
        val botonImagen = findViewById<ImageButton>(R.id.imageButton)
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
            val botonImagen = findViewById<ImageButton>(R.id.imageButton)
            botonImagen.isEnabled = true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (arePermissionsGranted(grantResults)) {
            when (requestCode) {
                Data.MY_PERMISSION_REQUEST_CAMERA -> {
                    Toast.makeText(this, "Permisos para la cámara concedidos", Toast.LENGTH_SHORT).show()
                    val botonImagen = findViewById<ImageButton>(R.id.imageButton)
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