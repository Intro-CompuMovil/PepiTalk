package com.example.pepitalk

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

class OfertasAdapter (context: Context, cursor: Cursor) : CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.layout_ofertas, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val idiomaTextView = view.findViewById<TextView>(R.id.languageOferta)
        val diaTextView = view.findViewById<TextView>(R.id.date)

        val idioma = cursor.getString(cursor.getColumnIndexOrThrow("idioma"))
        val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
        val horaInicio = cursor.getString(cursor.getColumnIndexOrThrow("horaInicio"))
        val horaFin = cursor.getString(cursor.getColumnIndexOrThrow("horaFin"))
        val lugar = cursor.getString(cursor.getColumnIndexOrThrow("lugar"))
        val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))

        idiomaTextView.text = idioma
        diaTextView.text = fecha

        view.setOnClickListener {
            val intent = Intent(context, VerOferta::class.java).apply {
                putExtra("idioma", idioma)
                putExtra("fecha", fecha)
                putExtra("horaInicio", horaInicio)
                putExtra("horaFin", horaFin)
                putExtra("lugar", lugar)
                putExtra("descripcion", descripcion)
            }
            context.startActivity(intent)
        }
    }
}