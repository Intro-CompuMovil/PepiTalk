package com.example.pepitalk.Logica

import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataGrupo
import com.example.pepitalk.Datos.DataReunion
import com.example.pepitalk.Datos.Persona
import com.example.pepitalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerCalificaciones : AppCompatActivity() {

    var mCursor: Cursor? = null
    var mCalificacionesAdapter: CalificacionesAdapter? = null
    var mlista: ListView? = null
    var tipo: String = ""
    var identificador: String = ""

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val PATH_USERS = "users/"
    private val PATH_GROUPS = "grupos/"
    private val PATH_REUNION = "reuniones/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_calificaciones)


        initView()
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

    private fun createCursor(callback: CursorCallback){
        val cursor = MatrixCursor(arrayOf("_id","nota", "comentario"))
        var bundle = intent.getBundleExtra("bundle")!!
        tipo = bundle.getString("tipo")!!
        identificador = bundle.getString("nombre")!!
        val nombre = findViewById<TextView>(R.id.nombre)
        nombre.setText(identificador)
        if(tipo == "grupo"){
            val groupRef = database.getReference(PATH_GROUPS)
            groupRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (singleSnapshot in dataSnapshot.children) {
                        val myGroup = singleSnapshot.getValue(DataGrupo::class.java)
                        if (myGroup != null && myGroup.nombre == identificador) {
                            for (i in 0 until myGroup.calificaciones.size) {
                                cursor.addRow(arrayOf(
                                    i,
                                    myGroup.calificaciones[i].nota,
                                    myGroup.calificaciones[i].comentario
                                ))
                            }
                        }
                    }
                    callback.onCursorReady(cursor)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

        }
        else if(tipo == "traductor"){
            val userRef = database.getReference(PATH_USERS)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (singleSnapshot in dataSnapshot.children) {
                        val myUser = singleSnapshot.getValue(Persona::class.java)
                        if (myUser != null && myUser.nombre == identificador) {
                            for (i in 0 until myUser.calificaciones.size) {
                                cursor.addRow(arrayOf(
                                    i,
                                    myUser.calificaciones[i].nota,
                                    myUser.calificaciones[i].comentario
                                ))
                            }
                        }
                    }
                    callback.onCursorReady(cursor)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

        }
        else if(tipo == "reunion"){
            val reunionRef = database.getReference(PATH_REUNION)
            reunionRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (singleSnapshot in dataSnapshot.children) {
                        val myReunion = singleSnapshot.getValue(DataReunion::class.java)
                        if (myReunion != null && myReunion.nombre == identificador) {
                            for (i in 0 until myReunion.calificaciones.size) {
                                cursor.addRow(arrayOf(
                                    i,
                                    myReunion.calificaciones[i].nota,
                                    myReunion.calificaciones[i].comentario
                                ))
                            }
                        }
                    }
                    callback.onCursorReady(cursor)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

        }

    }
    fun initView() {
        setUserPhoto()
        mlista = findViewById(R.id.calificaciones1)
        createCursor(object : CursorCallback {
            override fun onCursorReady(cursor: MatrixCursor) {
                mCursor = cursor
                mCalificacionesAdapter = CalificacionesAdapter(this@VerCalificaciones, mCursor!!)
                mlista?.adapter = mCalificacionesAdapter
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