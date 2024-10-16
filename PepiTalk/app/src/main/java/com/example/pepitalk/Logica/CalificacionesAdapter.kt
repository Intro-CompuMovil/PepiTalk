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

class CalificacionesAdapter(context: Context, cursor: Cursor) : CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.layout_calificaciones, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val notaTextView = view.findViewById<TextView>(R.id.calificacion)
        val commentTextView = view.findViewById<TextView>(R.id.comentarioCalificacion)

        val nota = cursor.getString(cursor.getColumnIndexOrThrow("nota"))
        val comentario = cursor.getString(cursor.getColumnIndexOrThrow("comentario"))


        notaTextView.text = nota
        commentTextView.text = comentario

    }
}