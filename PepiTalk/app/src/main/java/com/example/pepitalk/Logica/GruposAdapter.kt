package com.example.pepitalk.Logica

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.pepitalk.R

class GruposAdapter (context: Context, cursor: Cursor) : CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.layout_grupos, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val nombreTextView = view.findViewById<TextView>(R.id.groupName)
        val idiomaTextView = view.findViewById<TextView>(R.id.language)
        val nivelTextView = view.findViewById<TextView>(R.id.level)

        val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
        val idioma = cursor.getString(cursor.getColumnIndexOrThrow("idioma"))
        val nivel = cursor.getString(cursor.getColumnIndexOrThrow("nivel"))
        val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))

        nombreTextView.text = nombre
        idiomaTextView.text = idioma
        nivelTextView.text = nivel

        view.setOnClickListener {
            val intent = Intent(context, VerGrupo::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("idioma", idioma)
                putExtra("nivel", nivel)
                putExtra("descripcion", descripcion)
            }
            context.startActivity(intent)
        }
    }
}