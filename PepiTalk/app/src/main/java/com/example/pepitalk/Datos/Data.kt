package com.example.pepitalk.Datos

class Data {
    companion object{
        val MY_PERMISSION_REQUEST_CAMERA=0;
        val MY_PERMISSION_REQUEST_GALLERY=1;
        val listaGrupos = mutableListOf<DataGrupo>()
        val personas = mutableListOf<Persona>()
        val listaOfertas = mutableListOf<DataOferta>()
        val listaReuniones = mutableListOf<DataReunion>()
        var personaLog = Persona(
            tipo = "Traductor",
            usuario = "Pepito123",
            nombre = "Pepito el Pinguino",
            contrasena = "Pinguino",
            correo = "pepito@gmail.com")
    }
}