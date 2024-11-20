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
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataCalificaciones
import com.example.pepitalk.Datos.Persona
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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


class Registro : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var currentPhotoPath: String
    private var imageUri: Uri? = null

    private lateinit var nombre : EditText
    private lateinit var username : EditText
    private lateinit var contrasena : EditText
    private lateinit var confContrasena : EditText
    private lateinit var correo : EditText
    private lateinit var tipo : Spinner
    private lateinit var calificaciones : MutableList<DataCalificaciones>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        nombre = findViewById(R.id.editTextNombreRegistro)
        username = findViewById(R.id.editTextUsuarioRegistro)
        contrasena = findViewById(R.id.editTextPasswordRegistro)
        confContrasena = findViewById(R.id.editTextConfirmarPasswordRegistro)
        correo = findViewById(R.id.editTextCorreoRegistro)
        calificaciones = mutableListOf()
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {



    }
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun validarCampos(){
        val tipo = findViewById<Spinner>(R.id.spinner)
        tipo.onItemSelectedListener = this
        //guardar valor del spinner en una variable
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

        for( i in 0 until Data.personas.size){
            if(usuario == Data.personas[i].usuario|| correo == Data.personas[i].correo){
                found = true
                Toast.makeText(this,"El usuario ya existe o el correo ya se ha utilizado" , Toast.LENGTH_SHORT).show()
            }
        }
        if(!found){
            if(contrasena == confContrasena){
                if(emailRegex.matches(correo)){
                    var newUser = Persona(tipo, usuario, nombre, contrasena, correo, mutableListOf(), "")

                    createUserWithImage(tipo, correo, contrasena, nombre, usuario)  // Crear usuario en Firebase

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
        val botonImagen = findViewById<ImageButton>(R.id.imageButton)
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

    private fun createUserWithImage(tipo: String, email: String, password: String, nombre: String, username: String) {
        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener por lo menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        var isAuthSuccessful = false
        var isProfileUpdateSuccessful = false
        var isImageUploadSuccessful = false
        var isDatabaseUpdateSuccessful = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseRegister", "createUserWithEmail:success")
                    isAuthSuccessful = true
                    val user = auth.currentUser
                    if (user != null) {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(nombre)
                            .build()
                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    Log.d("FirebaseRegister", "updateProfile:success")
                                    isProfileUpdateSuccessful = true
                                    val imageRef = storageRef.child("images/${user.uid}.jpg")
                                    imageUri?.let {
                                        imageRef.putFile(it)
                                            .addOnSuccessListener {
                                                Log.d("FirebaseRegister", "imageUpload:success")
                                                isImageUploadSuccessful = true
                                                imageRef.downloadUrl.addOnSuccessListener { uri ->
                                                    Log.d("FirebaseRegister", "getDownloadUrl:success")
                                                    val usuario = Persona(tipo, username, nombre, password, email, mutableListOf(), uri.toString())
                                                    database.child("users").child(user.uid).setValue(usuario)
                                                        .addOnCompleteListener { dbTask ->
                                                            if (dbTask.isSuccessful) {
                                                                Log.d("FirebaseRegister", "databaseUpdate:success")
                                                                isDatabaseUpdateSuccessful = true
                                                                if (isAuthSuccessful && isProfileUpdateSuccessful && isImageUploadSuccessful && isDatabaseUpdateSuccessful) {
                                                                    Toast.makeText(baseContext, "Creación de usuario exitosa.", Toast.LENGTH_SHORT).show()
                                                                    updateUI(user, tipo)
                                                                }else{
                                                                    Toast.makeText(baseContext, "Errores en la creación de usuario, intente con otro correo.", Toast.LENGTH_SHORT).show()
                                                                }
                                                            } else {
                                                                Log.e("FirebaseRegister", "databaseUpdate:failure", dbTask.exception)
                                                                Toast.makeText(baseContext, "Database update failed.", Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                }.addOnFailureListener { exception ->
                                                    Log.e("FirebaseRegister", "getDownloadUrl:failure", exception)
                                                    Toast.makeText(baseContext, "Failed to get image URL.", Toast.LENGTH_SHORT).show()
                                                }
                                            }.addOnFailureListener { exception ->
                                                Log.e("FirebaseRegister", "imageUpload:failure", exception)
                                                Toast.makeText(baseContext, "Image upload failed.", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                } else {
                                    Log.e("FirebaseRegister", "updateProfile:failure", profileTask.exception)
                                    Toast.makeText(baseContext, "Profile update failed.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Log.e("FirebaseRegister", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?, tipo: String) {

        if (currentUser != null) {
            if(tipo == "Cliente"){
                val intent = Intent(this, MenuCliente::class.java)
                intent.putExtra("user", currentUser.email)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, MenuTraductor::class.java)
                intent.putExtra("user", currentUser.email)
                startActivity(intent)
            }
        } else {
            nombre.setText("")
            username.setText("")
            correo.setText("")
            contrasena.setText("")
            confContrasena.setText("")
            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
        }
    }
}