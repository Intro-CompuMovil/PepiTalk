package com.example.pepitalk.Datos

data class DataOferta(
    var idioma: String,
    var fecha: String,
    var horaInicio: String,
    var horaFinal: String,
    var lugar: String,
    var descripcion: String,
    var dueno: String,
    var trabajador: String,
    var aceptado: Boolean)
