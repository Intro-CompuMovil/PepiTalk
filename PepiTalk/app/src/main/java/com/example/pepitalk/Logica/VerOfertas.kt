package com.example.pepitalk.Logica

import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
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

    fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val isStream: InputStream = assets.open("ofertas.json")
            val size: Int = isStream.available()
            val buffer = ByteArray(size)
            isStream.read(buffer)
            isStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun createCursorFromJsonArray(jsonArray: JSONArray): MatrixCursor {
        val cursor = MatrixCursor(arrayOf("_id", "idioma", "fecha", "horaInicio", "horaFin", "lugar", "descripcion"))
        for (i in 0 until jsonArray.length()) {
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
            cursor.addRow(arrayOf(
                i,
                jsonObject.getString("idioma"),
                jsonObject.getString("fecha"),
                jsonObject.getString("horaInicio"),
                jsonObject.getString("horaFin"),
                jsonObject.getString("lugar"),
                jsonObject.getString("descripcion")
            ))
        }
        return cursor
    }

    fun initView() {
        mlista = findViewById(R.id.ofertas)
        val json  = JSONObject(loadJSONFromAsset())
        val personasJson = json.getJSONArray("listaOfertas")
        mCursor = createCursorFromJsonArray(personasJson)
        mOfertasAdapter = OfertasAdapter(this, mCursor!!)
        mlista?.adapter = mOfertasAdapter
    }
}