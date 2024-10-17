package com.example.pepitalk.Logica

import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.Datos.DataGrupo
import com.example.pepitalk.R

class VerGrupos : AppCompatActivity(){

    var mCursor: Cursor? = null
    var mGruposAdapter: GruposAdapter? = null
    var mlista: ListView? = null

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
            if(Data.personaLog.tipo == "Cliente"){
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

    private fun createCursor(tipo: String?): MatrixCursor {
        val cursor = MatrixCursor(arrayOf("_id", "nombre", "idioma", "nivel", "descripcion", "dueno", "integrantes", "calificaciones"))

        if (tipo == "misGrupos") {
            var group = DataGrupo("","","","","", mutableListOf(), mutableListOf())
            for (i in 0 until Data.listaGrupos.size) {
                if (Data.personaLog.usuario == Data.listaGrupos[i].dueno || Data.listaGrupos[i].integrantes.contains(Data.personaLog.usuario)) {
                    group = Data.listaGrupos[i]
                    cursor.addRow(arrayOf(
                        i,
                        group.nombre,
                        group.idioma,
                        group.nivel,
                        group.descripcion,
                        group.dueno,
                        group.integrantes,
                        group.calificaciones
                    ))
                }
            }
        } else if (tipo == "gruposParaUnirme") {
            var group = DataGrupo("","","","","", mutableListOf(), mutableListOf())
            for (i in 0 until Data.listaGrupos.size) {
                if (Data.personaLog.usuario != Data.listaGrupos[i].dueno && !Data.listaGrupos[i].integrantes.contains(Data.personaLog.usuario)) {
                    group = Data.listaGrupos[i]
                    cursor.addRow(arrayOf(
                        i,
                        group.nombre,
                        group.idioma,
                        group.nivel,
                        group.descripcion,
                        group.dueno,
                        group.integrantes,
                        group.calificaciones
                    ))
                }
            }
        }

        return cursor
    }

    fun initView( tipo: String? ) {
        mlista = findViewById(R.id.grupos1)
        mCursor = createCursor(tipo)
        mGruposAdapter = GruposAdapter(this, mCursor!!)
        mlista?.adapter = mGruposAdapter
    }
}