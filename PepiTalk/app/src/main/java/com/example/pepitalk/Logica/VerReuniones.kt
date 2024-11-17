package com.example.pepitalk.Logica

import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataReunion
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class VerReuniones : AppCompatActivity(){

    var mCursor: Cursor? = null
    var mReuniones: ReunionAdapter? = null
    var mlista: ListView? = null

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference
    private val PATH_USERS = "users/"
    private val PATH_REUNION = "reuniones/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_reuniones)

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
        val cursor = MatrixCursor(arrayOf("_id", "nombre", "dia", "hora", "idioma", "nivel", "lugar", "descripcion", "dueno", "integrantes", "calificaciones", "imagen", "llave"))

        myRef = database.getReference(PATH_REUNION)
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            var i = 0
            if (tipo == "misReuniones") {
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (singleSnapshot in dataSnapshot.children) {
                            val myReunion = singleSnapshot.getValue(DataReunion::class.java)
                            val llave = singleSnapshot.key
                            if (myReunion != null && (myReunion.dueno == userId || myReunion.integrantes.contains(userId))) {
                                cursor.addRow(arrayOf(
                                    i,
                                    myReunion.nombre,
                                    myReunion.dia,
                                    myReunion.hora,
                                    myReunion.idioma,
                                    myReunion.nivel,
                                    myReunion.lugar,
                                    myReunion.descripcion,
                                    myReunion.dueno,
                                    myReunion.integrantes.joinToString(","),
                                    myReunion.calificaciones.joinToString(",") { "DataCalificaciones(nota=${it.nota}, comentario=${it.comentario})" },
                                    myReunion.imageUrl,
                                    llave
                                ))
                                i++
                            }
                        }
                        callback.onCursorReady(cursor) // Call the callback once the cursor is ready
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })
            } else if (tipo == "reunionesParaUnirme") {
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (singleSnapshot in dataSnapshot.children) {
                            val myReunion = singleSnapshot.getValue(DataReunion::class.java)
                            val llave = singleSnapshot.key
                            if (myReunion != null && !myReunion.integrantes.contains(userId)) {
                                cursor.addRow(arrayOf(
                                    i,
                                    myReunion.nombre,
                                    myReunion.dia,
                                    myReunion.hora,
                                    myReunion.idioma,
                                    myReunion.nivel,
                                    myReunion.lugar,
                                    myReunion.descripcion,
                                    myReunion.dueno,
                                    myReunion.integrantes.joinToString(","),
                                    myReunion.calificaciones.joinToString(",") { "DataCalificaciones(nota=${it.nota}, comentario=${it.comentario})" },
                                    myReunion.imageUrl,
                                    llave
                                ))
                                i++
                            }
                        }
                        callback.onCursorReady(cursor) // Call the callback once the cursor is ready
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })
            }
        }
    }

    fun initView(tipo : String?) {
        setUserPhoto()
        mlista = findViewById(R.id.reuniones)
        createCursor(tipo, object : CursorCallback {
            override fun onCursorReady(cursor: MatrixCursor) {
                mCursor = cursor
                mReuniones = ReunionAdapter(this@VerReuniones, mCursor!!)
                mlista?.adapter = mReuniones
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
}