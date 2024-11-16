package com.example.pepitalk.Datos

data class DataGrupo(
    var nombre: String,
    var idioma: String,
    var nivel: String,
    var descripcion: String,
    var dueno: String,
    var lugar: String,
    var integrantes: MutableList<String>,
    var calificaciones: MutableList<DataCalificaciones>,
    var imageUrl: String
){
    constructor() : this("", "", "", "", "", "", mutableListOf(), mutableListOf(), "")
}
