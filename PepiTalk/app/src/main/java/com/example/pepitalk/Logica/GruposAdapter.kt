package com.example.pepitalk.Logica

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pepitalk.Datos.DataCalificaciones
import com.example.pepitalk.R
import kotlin.math.roundToLong

class GruposAdapter (context: Context, cursor: Cursor) : CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.layout_grupos, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val nombreTextView = view.findViewById<TextView>(R.id.groupName)
        val idiomaTextView = view.findViewById<TextView>(R.id.language)
        val nivelTextView = view.findViewById<TextView>(R.id.level)
        val calificacionTextView = view.findViewById<TextView>(R.id.calificacion)
        val imagenGrupo = view.findViewById<ImageView>(R.id.imageView2)

        val llave = cursor.getString(cursor.getColumnIndexOrThrow("llave"))
        val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
        val idioma = cursor.getString(cursor.getColumnIndexOrThrow("idioma"))
        val nivel = cursor.getString(cursor.getColumnIndexOrThrow("nivel"))
        val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
        val dueno = cursor.getString(cursor.getColumnIndexOrThrow("dueno"))
        val integrantes = cursor.getString(cursor.getColumnIndexOrThrow("integrantes"))
        val calificacionesString = cursor.getString(cursor.getColumnIndexOrThrow("calificaciones"))
        val imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))


        val calificaciones = parseCalificaciones(calificacionesString)
        val promedio = calcularPromedio(calificaciones)

        nombreTextView.text = nombre
        idiomaTextView.text = idioma
        nivelTextView.text = nivel
        calificacionTextView.text = promedio.toString()

        Glide.with(context)
            .load(imageUrl)  // Carga la URL de descarga de Firebase
            // .placeholder(R.drawable.placeholder)  // Imagen de marcador de posición mientras carga
            //  .error(R.drawable.error)  // Imagen de error si falla la carga
            .into(imagenGrupo)

        view.setOnClickListener {
            val intent = Intent(context, VerGrupo::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("idioma", idioma)
                putExtra("nivel", nivel)
                putExtra("descripcion", descripcion)
                putExtra("dueno", dueno)
                putExtra("integrantes", integrantes)
                putExtra("calificaciones", calificacionesString)
                putExtra("imageUrl", imageUrl)
                putExtra("llave", llave)
            }
            context.startActivity(intent)
        }
    }

    private fun parseCalificaciones(calificacionesString: String?): List<DataCalificaciones> {
        if (calificacionesString.isNullOrEmpty()) return emptyList()

        return calificacionesString
            .removeSurrounding("[", "]")
            .split("),")
            .mapNotNull {
                val parts = it.removeSurrounding("DataCalificaciones(", ")").split(", comentario=")
                if (parts.size == 2) {
                    val nota = parts[0].split("=")[1].toDoubleOrNull()
                    val comentario = parts[1]
                    if (nota != null) DataCalificaciones(nota, comentario) else null
                } else {
                    null
                }
            }
    }

    private fun calcularPromedio(calificaciones: List<DataCalificaciones>): String {
        val notas = calificaciones.map { it.nota }
        return if (notas.isNotEmpty()) {
            String.format("%.1f", notas.average())
        } else {
            "0.0"
        }
    }

}