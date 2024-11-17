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
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class ActualizarCuenta : AppCompatActivity() {

    private var currentPhotoPath: String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var tipo: String
    private lateinit var otroTemp: String
    private lateinit var claveTemp: String
    private lateinit var fotoTemp: String
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_cuenta)

        auth = Firebase.auth

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
            revisarTipo(auth.currentUser?.uid ?: "")
        }
        perfil.setOnClickListener(){
            startActivity(Intent(this, Perfil::class.java))
        }
    }

    private fun revisarTipo(userId: String) {
        val database = Firebase.database
        val userRef = database.getReference("users").child(userId)

        userRef.get().addOnSuccessListener { dataSnapshot ->
            val userType = dataSnapshot.child("tipo").getValue(String::class.java)
            if (userType != null) {
                tipo = userType
                navigateToMenu(userType)
            } else {
                Toast.makeText(baseContext, "Tipo no encontrado", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(baseContext, "Failed to retrieve user type.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMenu(userType: String) {
        if (userType == "Cliente") {
            val clienteLoggedIn = Intent(this, MenuCliente::class.java)
            startActivity(clienteLoggedIn)
        } else {
            val traductorLoggedIn = Intent(this, MenuTraductor::class.java)
            startActivity(traductorLoggedIn)
        }
    }

    private fun validarCampos() {
        val username = findViewById<EditText>(R.id.editTextUsuarioActualizar)
        val contrasena = findViewById<EditText>(R.id.editTextPasswordActualizar)
        val confContrasena = findViewById<EditText>(R.id.editTextConfirmarPasswordActualizar)
        val contrasenaAnt = findViewById<EditText>(R.id.editTextPasswordAnterior)

        if (username.text.toString().isEmpty() && contrasena.text.toString().isEmpty() && confContrasena.text.toString().isEmpty() && contrasenaAnt.text.toString().isEmpty() && imageUri == null) {
            Toast.makeText(this, "Por favor complete al menos un campo o cargue una foto", Toast.LENGTH_SHORT).show()
        } else {
            obtenerDatosUsuario(username.text.toString(), contrasena, confContrasena, contrasenaAnt)
        }
    }

    private fun obtenerDatosUsuario(usuario: String, contrasena: EditText, confContrasena: EditText, contrasenaAnt: EditText) {
        val database = Firebase.database
        val userRef = database.getReference("users").child(auth.currentUser?.uid ?: "")

        userRef.get().addOnSuccessListener { dataSnapshot ->
            otroTemp = dataSnapshot.child("usuario").getValue(String::class.java) ?: ""
            claveTemp = dataSnapshot.child("contrasena").getValue(String::class.java) ?: ""
            fotoTemp = dataSnapshot.child("imageUrl").getValue(String::class.java) ?: ""
            validarRegistro(usuario, contrasena.text.toString(), confContrasena.text.toString(), contrasenaAnt.text.toString())
        }.addOnFailureListener {
            Toast.makeText(baseContext, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validarRegistro(usuario: String, contrasena: String, confContrasena: String, contrasenaAnt: String) {
        if (usuario.isNotEmpty() && otroTemp == usuario) {
            Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena.isNotEmpty() || confContrasena.isNotEmpty() || contrasenaAnt.isNotEmpty()) {
            if (claveTemp != contrasenaAnt) {
                Toast.makeText(this, "Contraseña Anterior incorrecta " + claveTemp + " " + contrasenaAnt, Toast.LENGTH_SHORT).show()
                return
            }
            if (contrasena != confContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return
            }
        }

        actualizarDatos(usuario, contrasena)
    }

    private fun actualizarDatos(usuario: String, contrasena: String) {
        val user = auth.currentUser
        val userRef = Firebase.database.getReference("users").child(user?.uid ?: "")

        val updates = mutableMapOf<String, Any>()
        if (usuario.isNotEmpty()) updates["usuario"] = usuario

        if (imageUri != null && imageUri.toString() != fotoTemp) {
            val storageRef = Firebase.storage.reference.child("images/${user?.uid}.jpg")
            val uploadTask = storageRef.putFile(imageUri!!)

            uploadTask.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    updates["imageUrl"] = uri.toString()
                    updateUserDatabase(userRef, updates, contrasena)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
            }
        } else {
            updateUserDatabase(userRef, updates, contrasena)
        }
    }

    private fun updateUserDatabase(userRef: DatabaseReference, updates: Map<String, Any>, contrasena: String) {
        userRef.updateChildren(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (contrasena.isNotEmpty()) {
                    auth.currentUser?.updatePassword(contrasena)?.addOnCompleteListener { passwordTask ->
                        if (passwordTask.isSuccessful) {
                            userRef.child("contrasena").setValue(contrasena).addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(this, "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show()
                                    navigateToMenu(tipo)
                                } else {
                                    Toast.makeText(this, "Error al actualizar la contraseña en la base de datos", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Error al actualizar la contraseña en la autenticación", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show()
                    navigateToMenu(tipo)
                }
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
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
        val botonImagen = findViewById<ImageButton>(R.id.imageButton2)
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