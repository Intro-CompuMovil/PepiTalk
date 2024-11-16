package com.example.pepitalk.Datos

data class DataReunion(
    var nombre: String,
    var dia: String,
    var hora: String,
    var idioma: String,
    var nivel: String,
    var lugar: String,
    var descripcion: String,
    var dueno: String,
    var integrantes: MutableList<String>,
    var calificaciones: MutableList<DataCalificaciones>,
    var imageUrl: String){
    constructor() : this("", "", "", "", "", "", "", "", mutableListOf(), mutableListOf(), "")
}
