package com.example.pepitalk

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class Calificar : AppCompatActivity(), AdapterView.OnItemSelectedListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calificar)

        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.onItemSelectedListener = this
        val editText = findViewById<EditText>(R.id.editTextComentario)
        val buttonCalificar = findViewById<Button>(R.id.buttonCalificar)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


    }
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}