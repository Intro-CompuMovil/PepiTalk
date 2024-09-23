package com.example.pepitalk

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

class ReunionAdapter (context: Context, cursor: Cursor) : CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.layout_reuniones, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val nombreTextView = view.findViewById<TextView>(R.id.nombreReunion)
        val diaTextView = view.findViewById<TextView>(R.id.date)
        val idiomaTextView = view.findViewById<TextView>(R.id.language)
        val nivelTextView = view.findViewById<TextView>(R.id.level)

        val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
        val dia = cursor.getString(cursor.getColumnIndexOrThrow("dia"))
        val hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"))
        val idioma = cursor.getString(cursor.getColumnIndexOrThrow("idioma"))
        val nivel = cursor.getString(cursor.getColumnIndexOrThrow("nivel"))
        val lugar = cursor.getString(cursor.getColumnIndexOrThrow("lugar"))
        val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))

        nombreTextView.text = nombre
        diaTextView.text = dia
        idiomaTextView.text = idioma
        nivelTextView.text = nivel

        view.setOnClickListener {
            val intent = Intent(context, VerReunion::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("dia", dia)
                putExtra("hora", hora)
                putExtra("idioma", idioma)
                putExtra("nivel", nivel)
                putExtra("lugar", lugar)
                putExtra("descripcion", descripcion)
            }
            context.startActivity(intent)
        }
    }
}