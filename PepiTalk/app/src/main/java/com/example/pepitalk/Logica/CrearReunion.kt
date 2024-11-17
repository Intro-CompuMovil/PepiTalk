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
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataCalificaciones
import com.example.pepitalk.Datos.DataReunion
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class CrearReunion : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var currentPhotoPath: String
    private var imageUri: Uri? = null

    private lateinit var nombre : EditText
    private lateinit var dia : EditText
    private lateinit var hora : EditText
    private lateinit var idioma : EditText
    private lateinit var nivel : EditText
    private lateinit var lugar : EditText
    private lateinit var descripcion : EditText
    private lateinit var calificaciones : MutableList<DataCalificaciones>
    private lateinit var integrantes : MutableList<String>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_reunion)
        val botonCrearReunion = findViewById<Button>(R.id.buttonCrearReunion)
        val botonImagen = findViewById<ImageButton>(R.id.imageButton5)
        val menuPrincipal = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        botonImagen.isEnabled = false
        pedirPermiso(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), "Se necesita este permiso", Data.MY_PERMISSION_REQUEST_CAMERA)
        botonCrearReunion.setOnClickListener(){
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

        nombre = findViewById(R.id.editTextNombreReunion)
        dia = findViewById(R.id.editTextDiaReunion)
        hora = findViewById(R.id.editTextHoraReunion)
        idioma = findViewById(R.id.editTextIdiomaReunion)
        nivel = findViewById(R.id.editTextNivelReunion)
        lugar = findViewById(R.id.editTextLugarReunion)
        descripcion = findViewById(R.id.editTextDescripcionReunion)
        calificaciones = mutableListOf()
        integrantes = mutableListOf()

        auth = FirebaseAuth.getInstance()
        database =  FirebaseDatabase.getInstance().reference
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
        val nombre = findViewById<EditText>(R.id.editTextNombreReunion)
        val dia = findViewById<EditText>(R.id.editTextDiaReunion)
        val hora = findViewById<EditText>(R.id.editTextHoraReunion)
        val idioma = findViewById<EditText>(R.id.editTextIdiomaReunion)
        val nivel = findViewById<EditText>(R.id.editTextNivelReunion)
        val lugar = findViewById<EditText>(R.id.editTextLugarReunion)
        val descripcion = findViewById<EditText>(R.id.editTextDescripcionReunion)

        if(nombre.text.toString().isEmpty() ||dia.text.toString().isEmpty() ||hora.text.toString().isEmpty() || idioma.text.toString().isEmpty() || nivel.text.toString().isEmpty() || lugar.text.toString().isEmpty()|| descripcion.text.toString().isEmpty()){
            Toast.makeText(this,"Por favor complete todos los campos" , Toast.LENGTH_SHORT).show()
        }
        else{
            validarRegistro(nombre.text.toString(),dia.text.toString(),hora.text.toString(), idioma.text.toString(), nivel.text.toString(), lugar.text.toString(), descripcion.text.toString())
        }
    }

    private fun validarRegistro(nombre: String, dia: String, hora: String, idioma: String, nivel: String, lugar: String, descripcion: String){
        //recorrer arreglo con los usuarios
        val nombreR = "grupo1234"

        if(nombreR != nombre){
            Data.listaReuniones.add(DataReunion())
            createReunion(nombre, dia, hora, idioma, nivel, lugar, descripcion, imageUri)
            var reunionCreado = Intent(this, Reunion::class.java)
            startActivity(reunionCreado)
            Toast.makeText(this,"Se ha creado su reunion correctamente" , Toast.LENGTH_SHORT).show()


        }
        else{
            Toast.makeText(this,"Ya existe una reunion con ese nombre" , Toast.LENGTH_SHORT).show()
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
        val botonImagen = findViewById<ImageButton>(R.id.imageButton5)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Data.MY_PERMISSION_REQUEST_CAMERA -> {

                    val file = File(currentPhotoPath)
                    if (file.exists()) {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                        botonImagen.setImageBitmap(bitmap)
                        imageUri = Uri.fromFile(file)
                    }

                }
                Data.MY_PERMISSION_REQUEST_GALLERY -> {
                    val selectedImageUri = data?.data
                    // Muestra la imagen seleccionada o guárdala
                    botonImagen.setImageURI(selectedImageUri)
                    if (selectedImageUri != null) {
                        botonImagen.setImageURI(selectedImageUri)
                        imageUri = selectedImageUri
                        // Muestra la imagen seleccionada o guárdala
                        botonImagen.setImageURI(selectedImageUri)
                    }
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
            val botonImagen = findViewById<ImageButton>(R.id.imageButton5)
            botonImagen.isEnabled = true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (arePermissionsGranted(grantResults)) {
            when (requestCode) {
                Data.MY_PERMISSION_REQUEST_CAMERA -> {
                    Toast.makeText(this, "Permisos para la cámara concedidos", Toast.LENGTH_SHORT).show()
                    val botonImagen = findViewById<ImageButton>(R.id.imageButton5)
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

    private fun createReunion(nombre: String, dia: String, hora: String, idioma: String, nivel: String,
        lugar: String, descripcion: String, imageUri: Uri?
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null ) {
            val dueno = user.uid // obtener el id del dueño
            val integrantes = mutableListOf(dueno) // añadir al dueño de una vez a la lista de integrantes
            val meetingId = database.child("reuniones").push().key
            if (meetingId != null && imageUri != null) {
                val imageRef = storageRef.child("images/reuniones/${meetingId}.jpg")
                imageRef.putFile(imageUri)
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            // Guardar los datos de la reunión en la base de datos
                            val meeting = DataReunion(
                                nombre = nombre, dia = dia, hora = hora, idioma = idioma,
                                nivel = nivel, lugar = lugar, descripcion = descripcion, dueno = dueno,
                                integrantes = integrantes, calificaciones = mutableListOf(), imageUrl = uri.toString()
                            )

                            database.child("reuniones").child(meetingId).setValue(meeting)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(baseContext, "Reunión creada exitosamente.", Toast.LENGTH_SHORT).show()
                                        updateUI()
                                    } else {
                                        Toast.makeText(baseContext, "Error al actualizar la base de datos.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FirebaseRegister", "Error al subir el archivo: ${exception.message}", exception)
                    }
            } else {
                Toast.makeText(baseContext, "Se requiere una imagen para crear la reunión.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(baseContext, "Usuario no autenticado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        val intent = Intent(this, MenuCliente::class.java)
        startActivity(intent)
    }
}