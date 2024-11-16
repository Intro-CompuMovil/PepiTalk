package com.example.pepitalk.Datos

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream

class Data {
    companion object{
        val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1001
        val MY_PERMISSION_REQUEST_CAMERA=0;
        val MY_PERMISSION_REQUEST_GALLERY=1;
        val listaGrupos = mutableListOf<DataGrupo>()
        val personas = mutableListOf<Persona>()
        val listaOfertas = mutableListOf<DataOferta>()
        val listaReuniones = mutableListOf<DataReunion>()
        var personaLog = Persona()
    }


}