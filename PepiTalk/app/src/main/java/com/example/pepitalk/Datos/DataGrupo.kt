package com.example.pepitalk.Datos

data class DataGrupo(
    var nombre: String,
    var idioma: String,
    var nivel: String,
    var descripcion: String,
    var dueno: String,
    var integrantes: MutableList<String>)
