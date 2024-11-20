package com.example.pepitalk.Logica

import android.content.ContentValues.TAG
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
import com.example.pepitalk.Datos.DataOferta
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

class VerOfertas : AppCompatActivity(){
    var mCursor: Cursor? = null
    var mOfertasAdapter: OfertasAdapter? = null
    var mlista: ListView? = null
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference
    private val PATH_USERS = "users/"
    private val PATH_OFFERS = "ofertas/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_ofertas)

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
        val cursor = MatrixCursor(arrayOf("_id", "tipo","idioma", "fecha", "horaInicio", "horaFinal", "lugar", "descripcion", "dueno", "trabajador", "aceptado", "llave"))
        myRef = database.getReference(PATH_OFFERS)
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId != null){
            var i = 0
            if (tipo == "misOfertas") {
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (singleSnapshot in dataSnapshot.children) {
                            val myOffer = singleSnapshot.getValue(DataOferta::class.java)
                            val llave = singleSnapshot.key
                            if (myOffer != null && myOffer.dueno == userId) {
                                cursor.addRow(arrayOf(
                                    i,
                                    "misOfertas",
                                    myOffer.idioma,
                                    myOffer.fecha,
                                    myOffer.horaInicio,
                                    myOffer.horaFinal,
                                    myOffer.lugar,
                                    myOffer.descripcion,
                                    myOffer.dueno,
                                    myOffer.trabajador,
                                    myOffer.aceptado,
                                    llave
                                ))
                                i++
                            }
                        }
                        callback.onCursorReady(cursor)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })



            }
            else if(tipo == "misTrabajos" ) {
                var tipo = ""
                val userRef = database.getReference(PATH_USERS).child(userId)
                userRef.child("tipo").get().addOnSuccessListener { dataSnapshot ->
                    tipo = dataSnapshot.value.toString()
                }
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (singleSnapshot in dataSnapshot.children) {
                            val myOffer = singleSnapshot.getValue(DataOferta::class.java)
                            val llave = singleSnapshot.key
                            if (myOffer != null && myOffer.trabajador == userId) {
                                cursor.addRow(arrayOf(
                                    i,
                                    "misTrabajos",
                                    myOffer.idioma,
                                    myOffer.fecha,
                                    myOffer.horaInicio,
                                    myOffer.horaFinal,
                                    myOffer.lugar,
                                    myOffer.descripcion,
                                    myOffer.dueno,
                                    myOffer.trabajador,
                                    myOffer.aceptado,
                                    llave
                                ))
                                i++
                            }
                        }
                        callback.onCursorReady(cursor)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })

            }
            else if(tipo == "aceptarOfertas" ){
                var tipo = ""
                val userRef = database.getReference(PATH_USERS).child(userId)
                Log.i(TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ")
                userRef.child("tipo").get().addOnSuccessListener { dataSnapshot ->
                    tipo = dataSnapshot.value.toString()
                }
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (singleSnapshot in dataSnapshot.children) {
                            val myOffer = singleSnapshot.getValue(DataOferta::class.java)
                            val llave = singleSnapshot.key
                            if (myOffer != null  && myOffer.trabajador.isEmpty()) {
                                cursor.addRow(arrayOf(
                                    i,
                                    "aceptarOfertas",
                                    myOffer.idioma,
                                    myOffer.fecha,
                                    myOffer.horaInicio,
                                    myOffer.horaFinal,
                                    myOffer.lugar,
                                    myOffer.descripcion,
                                    myOffer.dueno,
                                    myOffer.trabajador,
                                    myOffer.aceptado,
                                    llave
                                ))
                                i++
                            }
                        }
                        callback.onCursorReady(cursor)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })

            }
            else if(tipo == "AceptarTraductor"){
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (singleSnapshot in dataSnapshot.children) {
                            val myOffer = singleSnapshot.getValue(DataOferta::class.java)
                            val llave = singleSnapshot.key
                            if (myOffer != null && myOffer.dueno == userId && myOffer.trabajador.isNotEmpty() && myOffer.aceptado == false) {
                                cursor.addRow(arrayOf(
                                    i,
                                    "AceptarTraductor",
                                    myOffer.idioma,
                                    myOffer.fecha,
                                    myOffer.horaInicio,
                                    myOffer.horaFinal,
                                    myOffer.lugar,
                                    myOffer.descripcion,
                                    myOffer.dueno,
                                    myOffer.trabajador,
                                    myOffer.aceptado,
                                    llave
                                ))
                                i++
                            }
                        }
                        callback.onCursorReady(cursor)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
            }
        }

    }

    fun initView(tipo : String?) {
        setUserPhoto()
        mlista = findViewById(R.id.ofertas)

        createCursor(tipo, object : CursorCallback {
            override fun onCursorReady(cursor: MatrixCursor) {
                mCursor = cursor
                mOfertasAdapter = OfertasAdapter(this@VerOfertas, mCursor!!)
                mlista?.adapter = mOfertasAdapter
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