package com.example.pepitalk.Logica

import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataGrupo
import com.example.pepitalk.Datos.DataReunion
import com.example.pepitalk.Datos.Persona
import com.example.pepitalk.R

class VerCalificaciones : AppCompatActivity() {

    var mCursor: Cursor? = null
    var mCalificacionesAdapter: CalificacionesAdapter? = null
    var mlista: ListView? = null
    var tipo: String = "grupo"
    var identificador: String = "Bilingual"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_calificaciones)
        val nombre = findViewById<TextView>(R.id.nombre)
        nombre.setText(identificador)

        initView()
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        val inicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)

        inicio.setOnClickListener {
            if(/*es cliente*/true){
                val peticion = Intent(this, MenuCliente::class.java)
                startActivity(peticion)
            }else{
                val peticion = Intent(this, MenuTraductor::class.java)
                startActivity(peticion)
            }
        }

        perfil.setOnClickListener {
            val peticion = Intent(this, Perfil::class.java)
            startActivity(peticion)
        }
    }

    private fun createCursor() :MatrixCursor{
        val cursor = MatrixCursor(arrayOf("_id","nota", "comentario"))

        if(tipo == "grupo"){
            var group = DataGrupo("","","","","", "",mutableListOf(), mutableListOf())
            for(i in 0 until Data.listaGrupos.size){
                if(identificador == Data.listaGrupos[i].nombre){
                    group = Data.listaGrupos[i]
                }
            }
            for (i in 0 until group.calificaciones.size) {
                cursor.addRow(arrayOf(
                    i,
                    group.calificaciones[i].nota,
                    group.calificaciones[i].comentario
                ))
            }
        }
        else if(tipo == "traductor"){
            var translate = Persona("","","","","", mutableListOf())
            for(i in 0 until Data.personas.size){
                if(identificador == Data.personas[i].usuario){
                    translate = Data.personas[i]
                }
            }
            for (i in 0 until translate.calificaciones.size) {
                cursor.addRow(arrayOf(
                    i,
                    translate.calificaciones[i].nota,
                    translate.calificaciones[i].comentario
                ))
            }
        }
        else if(tipo == "reunion"){
            var reunion = DataReunion("","","","","","","","", mutableListOf(), mutableListOf())
            for(i in 0 until Data.listaReuniones.size){
                if(identificador == Data.listaReuniones[i].nombre){
                    reunion = Data.listaReuniones[i]
                }
            }
            for (i in 0 until reunion.calificaciones.size) {
                cursor.addRow(arrayOf(
                    i,
                    reunion.calificaciones[i].nota,
                    reunion.calificaciones[i].comentario
                ))
            }
        }
        return cursor
    }
    fun initView() {
        mlista = findViewById(R.id.calificaciones1)
        mCursor = createCursor()
        mCalificacionesAdapter = CalificacionesAdapter(this, mCursor!!)
        mlista?.adapter = mCalificacionesAdapter
    }
}