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

class VerOfertas : AppCompatActivity(){
    var mCursor: Cursor? = null
    var mOfertasAdapter: OfertasAdapter? = null
    var mlista: ListView? = null

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
        val cursor = MatrixCursor(arrayOf("_id", "idioma", "fecha", "horaInicio", "horaFinal", "lugar", "descripcion", "dueno", "trabajador", "aceptado"))

        if (tipo == "misOfertas") {
                for (i in 0 until Data.listaOfertas.size) {
                    val oferta = Data.listaOfertas[i]
                    if (Data.personaLog.usuario == oferta.dueno && Data.personaLog.tipo == "Cliente") {
                        cursor.addRow(arrayOf(
                            i,
                            oferta.idioma,
                            oferta.fecha,
                            oferta.horaInicio,
                            oferta.horaFinal,
                            oferta.lugar,
                            oferta.descripcion,
                            oferta.dueno,
                            oferta.trabajador,
                            oferta.aceptado
                        ))
                    }
                }
            }
            else if(tipo == "misTrabajos" ) {
                for (i in 0 until Data.listaOfertas.size) {
                    val oferta = Data.listaOfertas[i]
                    if (Data.personaLog.usuario == oferta.trabajador && Data.personaLog.tipo == "Traductor") {
                        cursor.addRow(arrayOf(
                            i,
                            oferta.idioma,
                            oferta.fecha,
                            oferta.horaInicio,
                            oferta.horaFinal,
                            oferta.lugar,
                            oferta.descripcion,
                            oferta.dueno,
                            oferta.trabajador,
                            oferta.aceptado
                        ))
                    }
                }
            }
            else if(tipo == "aceptarOfertas" ){
                for (i in 0 until Data.listaOfertas.size) {
                    val oferta = Data.listaOfertas[i]
                    if (Data.personaLog.tipo == "Traductor" && oferta.trabajador.isEmpty()) {
                        cursor.addRow(arrayOf(
                            i,
                            oferta.idioma,
                            oferta.fecha,
                            oferta.horaInicio,
                            oferta.horaFinal,
                            oferta.lugar,
                            oferta.descripcion,
                            oferta.dueno,
                            oferta.trabajador,
                            oferta.aceptado
                        ))
                    }
                }
            }

        return cursor
    }

    fun initView(tipo : String?) {
        mlista = findViewById(R.id.ofertas)
        mCursor = createCursor(tipo)
        mOfertasAdapter = OfertasAdapter(this, mCursor!!)
        mlista?.adapter = mOfertasAdapter
    }
}