package com.example.pepitalk.Logica

import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataGrupo
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class VerGrupos : AppCompatActivity(){

    var mCursor: Cursor? = null
    var mGruposAdapter: GruposAdapter? = null
    var mlista: ListView? = null
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference
    private val PATH_USERS = "users/"
    private val PATH_GROUPS = "grupos/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_grupos)

        val tipo = intent.getStringExtra("tipo")

        initView(tipo)
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        inicio.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid
            if(userId != null) {
                val userRef = database.getReference(PATH_USERS).child(userId)
                userRef.child("tipo").get().addOnSuccessListener { dataSnapshot ->
                    val tipo = dataSnapshot.value.toString()
                    if (tipo == "Cliente") {
                        val peticion = Intent(this, MenuCliente::class.java)
                        startActivity(peticion)
                    } else {
                        val peticion = Intent(this, MenuTraductor::class.java)
                        startActivity(peticion)
                    }
                }
            }

        }

        perfil.setOnClickListener {
            val peticion = Intent(this, Perfil::class.java)
            startActivity(peticion)
        }
    }

    private fun createCursor(tipo: String?, callback: CursorCallback) {
        val cursor = MatrixCursor(arrayOf("_id", "nombre", "idioma", "nivel", "descripcion", "dueno", "integrantes", "calificaciones", "imagen"))
        myRef = database.getReference(PATH_GROUPS)
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if(userId != null){
            var i = 0
            if (tipo == "misGrupos") {
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (singleSnapshot in dataSnapshot.children) {
                            val myGroup = singleSnapshot.getValue(DataGrupo::class.java)
                            if (myGroup != null) {
                                val llave = singleSnapshot.key
                                if (myGroup.dueno == userId || myGroup.integrantes.contains(userId)) {
                                    Log.d("VerGrupos", "Encontró grupo: " + myGroup.nombre)
                                    cursor.addRow(arrayOf(
                                        i,
                                        myGroup.nombre,
                                        myGroup.idioma,
                                        myGroup.nivel,
                                        myGroup.descripcion,
                                        myGroup.dueno,
                                        myGroup.integrantes.joinToString(","),
                                        myGroup.calificaciones.joinToString(",") { "DataCalificaciones(nota=${it.nota}, comentario=${it.comentario})" },
                                        myGroup.imageUrl
                                    ))
                                    i++
                                }
                            }
                        }
                        callback.onCursorReady(cursor)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // Si ocurre un error
                    }
                })

            } else if (tipo == "gruposParaUnirme") {
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (singleSnapshot in dataSnapshot.children) {
                            val myGroup = singleSnapshot.getValue(DataGrupo::class.java)
                            if (myGroup != null) {
                                if (!myGroup.integrantes.contains(userId)) {
                                    cursor.addRow(arrayOf(
                                        i,
                                        myGroup.nombre,
                                        myGroup.idioma,
                                        myGroup.nivel,
                                        myGroup.descripcion,
                                        myGroup.dueno,
                                        myGroup.integrantes.joinToString(","),
                                        myGroup.calificaciones.joinToString(",") { "DataCalificaciones(nota=${it.nota}, comentario=${it.comentario})" },
                                        myGroup.imageUrl
                                    ))
                                    i++
                                }
                            }
                        }
                        callback.onCursorReady(cursor)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // Si ocurre un error
                    }
                })
            }
        }
    }

    fun initView( tipo: String? ) {

        setUserPhoto()
        mlista = findViewById(R.id.grupos1)
        createCursor(tipo, object : CursorCallback {
            override fun onCursorReady(cursor: MatrixCursor) {
                mCursor = cursor
                mGruposAdapter = GruposAdapter(this@VerGrupos, mCursor!!)
                mlista?.adapter = mGruposAdapter
            }
        })

    }

    fun setUserPhoto(){
        val imageUser = findViewById<ImageButton>(R.id.butPerfil)
        var imageUrl = ""

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if(userId != null){
            val userRef = database.getReference(PATH_USERS).child(userId)
            userRef.child("imageUrl").get().addOnSuccessListener { dataSnapshot ->
                imageUrl = dataSnapshot.value.toString()
                Glide.with(this)
                    .load(imageUrl)  // Carga la URL de descarga de Firebase
                    // .placeholder(R.drawable.placeholder)  // Imagen de marcador de posición mientras carga
                    //  .error(R.drawable.error)  // Imagen de error si falla la carga
                    .into(imageUser)
            }
        }
    }

    interface CursorCallback {
        fun onCursorReady(cursor: MatrixCursor)
    }

    private fun checkInside(path: String): Boolean{


        return false
    }

}