package com.example.pepitalk.Logica

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class CrearGrupo : AppCompatActivity() {

    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_grupo)
        val botonCrearGrupo = findViewById<Button>(R.id.buttonCrearGrupo)
        val botonImagen = findViewById<ImageButton>(R.id.imageButton3)
        val menuPrincipal = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        botonImagen.isEnabled = false
        pedirPermiso(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), "Se necesita este permiso", Data.MY_PERMISSION_REQUEST_CAMERA)
        botonCrearGrupo.setOnClickListener(){
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
        if(Data.personaLog.tipo == "Cliente"){
            val peticion = Intent(this, MenuCliente::class.java)
            startActivity(peticion)
        }else{
            val peticion = Intent(this, MenuTraductor::class.java)
            startActivity(peticion)
        }
    }

    private fun validarCampos(){
        val nombre = findViewById<EditText>(R.id.editTextNombreGrupo)
        val idioma = findViewById<EditText>(R.id.editTextIdiomaGrupo)
        val nivel = findViewById<EditText>(R.id.editTextNivelGrupo)
        val lugar = findViewById<EditText>(R.id.editTextLugarGrupo)
        val descripcion = findViewById<EditText>(R.id.editTextDescripcionGrupo)

        if(nombre.text.toString().isEmpty() || idioma.text.toString().isEmpty() || nivel.text.toString().isEmpty() || lugar.text.toString().isEmpty()|| descripcion.text.toString().isEmpty()){
            Toast.makeText(this,"Por favor complete todos los campos" , Toast.LENGTH_SHORT).show()
        }
        else{
            validarRegistro(nombre.text.toString(), idioma.text.toString(), nivel.text.toString(), lugar.text.toString(), descripcion.text.toString())
        }
    }

    private fun validarRegistro(nombre: String, idioma: String, nivel: String, lugar: String, descripcion: String){
        //recorrer arreglo con los usuarios
        val nombreG = "grupo1234"

        if(nombreG != nombre){

            var grupoCreado = Intent(this, Grupo::class.java)
            startActivity(grupoCreado)
            Toast.makeText(this,"Se ha creado su grupo correctamente" , Toast.LENGTH_SHORT).show()


        }
        else{
            Toast.makeText(this,"Ya existe un grupo con ese nombre" , Toast.LENGTH_SHORT).show()
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
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.pepitalk.fileprovider",
                it
            )
            galleryAddPic()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, Data.MY_PERMISSION_REQUEST_CAMERA)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            sendBroadcast(mediaScanIntent)
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Data.MY_PERMISSION_REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val botonImagen = findViewById<ImageButton>(R.id.imageButton3)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Data.MY_PERMISSION_REQUEST_CAMERA -> {

                    val file = File(currentPhotoPath)
                    if (file.exists()) {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                        botonImagen.setImageBitmap(bitmap)
                    }

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
            val botonImagen = findViewById<ImageButton>(R.id.imageButton3)
            botonImagen.isEnabled = true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (arePermissionsGranted(grantResults)) {
            when (requestCode) {
                Data.MY_PERMISSION_REQUEST_CAMERA -> {
                    Toast.makeText(this, "Permisos para la cámara concedidos", Toast.LENGTH_SHORT).show()
                    val botonImagen = findViewById<ImageButton>(R.id.imageButton3)
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