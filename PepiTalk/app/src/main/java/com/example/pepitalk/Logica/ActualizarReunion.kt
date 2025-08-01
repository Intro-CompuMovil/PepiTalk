package com.example.pepitalk.Logica

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class ActualizarReunion : AppCompatActivity() {

    private lateinit var currentPhotoPath: String
    private lateinit var auth: FirebaseAuth
    private lateinit var idReunion: String
    private var imageUri: Uri? = null
    private val PATH_USERS = "users/"
    private val PATH_REUNIONES = "reuniones/"
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_reunion)

        auth = Firebase.auth

        var nombreReunion = intent.getStringExtra("nombre")

        obtenerIdReunionPorNombre(nombreReunion!!) { reunionId ->
            if (reunionId != null) {
                idReunion = reunionId
                setUserPhoto()
            }
        }

        val botonActualizarReunion = findViewById<Button>(R.id.buttonActualizarReunion)
        val botonImagen = findViewById<ImageButton>(R.id.imageButton6)
        val menuPrincipal = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        botonImagen.isEnabled = false
        pedirPermiso(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), "Se necesita este permiso", Data.MY_PERMISSION_REQUEST_CAMERA)
        botonActualizarReunion.setOnClickListener(){
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

    private fun obtenerIdReunionPorNombre(nombreGrupo: String, callback: (String?) -> Unit) {
        val grupoRef = database.getReference(PATH_REUNIONES)
        grupoRef.orderByChild("nombre").equalTo(nombreGrupo).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (singleSnapshot in dataSnapshot.children) {
                        val grupoId = singleSnapshot.key
                        callback(grupoId)
                        return
                    }
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

    fun setUserPhoto() {
        val imageUser = findViewById<ImageButton>(R.id.butPerfil)
        val imageCam = findViewById<ImageButton>(R.id.imageButton6)
        var imageUrl1 = ""
        var reunionImageUrl = ""

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

        if (::idReunion.isInitialized) {
            val groupRef = database.getReference(PATH_REUNIONES).child(idReunion)
            groupRef.child("imageUrl").get().addOnSuccessListener { dataSnapshot ->
                reunionImageUrl = dataSnapshot.value?.toString() ?: ""
                if (reunionImageUrl.isNotEmpty()) {
                    Glide.with(this)
                        .load(reunionImageUrl)
                        .into(imageCam)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al cargar la imagen del grupo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun irPrincipal(){
        val peticion = Intent(this, MenuCliente::class.java)
        startActivity(peticion)

    }

    private fun validarCampos() {
        val dia = findViewById<EditText>(R.id.editTextDiaReunionAct)
        val hora = findViewById<EditText>(R.id.editTextHoraReunionAct)
        val nivel = findViewById<EditText>(R.id.editTextNivelReunionAct)
        val lugar = findViewById<EditText>(R.id.editTextLugarReunionAct)
        val descripcion = findViewById<EditText>(R.id.editTextDescripcionReunionAct)

        if (dia.text.toString().isEmpty() && hora.text.toString().isEmpty() && nivel.text.toString().isEmpty() && lugar.text.toString().isEmpty() && descripcion.text.toString().isEmpty() && imageUri == null) {
            Toast.makeText(this, "Por favor complete al menos un campo o cargue una foto", Toast.LENGTH_SHORT).show()
        } else {
            validarRegistro(dia.text.toString(), hora.text.toString(), nivel.text.toString(), lugar.text.toString(), descripcion.text.toString())
        }
    }

    private fun validarRegistro(dia: String, hora: String, nivel: String, lugar: String, descripcion: String) {
        val updates = mutableMapOf<String, Any>()
        if (dia.isNotEmpty()) updates["dia"] = dia
        if (hora.isNotEmpty()) updates["hora"] = hora
        if (nivel.isNotEmpty()) updates["nivel"] = nivel
        if (lugar.isNotEmpty()) updates["lugar"] = lugar
        if (descripcion.isNotEmpty()) updates["descripcion"] = descripcion

        if (imageUri != null) {
            val storageRef = Firebase.storage.reference.child("images/reuniones/${idReunion}.jpg")
            val uploadTask = storageRef.putFile(imageUri!!)

            uploadTask.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    updates["imageUrl"] = uri.toString()
                    updateReunionDatabase(updates)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
            }
        } else {
            updateReunionDatabase(updates)
        }
    }

    private fun updateReunionDatabase(updates: Map<String, Any>) {
        val reunionRef = Firebase.database.getReference(PATH_REUNIONES).child(idReunion)
        reunionRef.updateChildren(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Se ha actualizado su reunión correctamente", Toast.LENGTH_SHORT).show()
                val reunionCreado = Intent(this, Reunion::class.java)
                startActivity(reunionCreado)
            } else {
                Toast.makeText(this, "Error al actualizar la reunión", Toast.LENGTH_SHORT).show()
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
        val botonImagen = findViewById<ImageButton>(R.id.imageButton6)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Data.MY_PERMISSION_REQUEST_CAMERA -> {

                    val file = File(currentPhotoPath)
                    if (file.exists()) {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                        botonImagen.setImageBitmap(bitmap)
                        imageUri = Uri.fromFile(file)
                    }

                }

                Data.MY_PERMISSION_REQUEST_GALLERY -> {
                    val selectedImageUri = data?.data
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
            val botonImagen = findViewById<ImageButton>(R.id.imageButton6)
            botonImagen.isEnabled = true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (arePermissionsGranted(grantResults)) {
            when (requestCode) {
                Data.MY_PERMISSION_REQUEST_CAMERA -> {
                    Toast.makeText(this, "Permisos para la cámara concedidos", Toast.LENGTH_SHORT).show()
                    val botonImagen = findViewById<ImageButton>(R.id.imageButton6)
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