package com.example.pepitalk.Logica

import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class VerReuniones : AppCompatActivity(){

    var mCursor: Cursor? = null
    var mReuniones: ReunionAdapter? = null
    var mlista: ListView? = null

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
        val cursor = MatrixCursor(arrayOf("_id", "nombre", "dia", "hora", "idioma", "nivel", "lugar", "descripcion", "dueno", "integrantes", "calificaciones"))

        if (tipo == "misReuniones") {
            for (i in 0 until Data.listaReuniones.size) {
                val reunion = Data.listaReuniones[i]
                if (Data.personaLog.usuario == reunion.dueno || reunion.integrantes.contains(Data.personaLog.usuario)) {
                    cursor.addRow(arrayOf(
                        i,
                        reunion.nombre,
                        reunion.dia,
                        reunion.hora,
                        reunion.idioma,
                        reunion.nivel,
                        reunion.lugar,
                        reunion.descripcion,
                        reunion.dueno,
                        reunion.integrantes.joinToString(","),
                        reunion.calificaciones.joinToString(",") { "DataCalificaciones(nota=${it.nota}, comentario=${it.comentario})" }
                    ))
                }
            }
        } else if (tipo == "reunionesParaUnirme") {
            for (i in 0 until Data.listaReuniones.size) {
                val reunion = Data.listaReuniones[i]
                if (Data.personaLog.usuario != reunion.dueno && !reunion.integrantes.contains(Data.personaLog.usuario)) {
                    cursor.addRow(arrayOf(
                        i,
                        reunion.nombre,
                        reunion.dia,
                        reunion.hora,
                        reunion.idioma,
                        reunion.nivel,
                        reunion.lugar,
                        reunion.descripcion,
                        reunion.dueno,
                        reunion.integrantes,
                        reunion.calificaciones
                    ))
                }
            }
        }

        return cursor
    }

    fun initView(tipo : String?) {
        mlista = findViewById(R.id.reuniones)
        mCursor = createCursor(tipo)
        mReuniones = ReunionAdapter(this, mCursor!!)
        mlista?.adapter = mReuniones
    }
}