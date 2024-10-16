package com.example.pepitalk.Datos

data class Persona(var tipo: String,
                   var usuario: String,
                   var nombre: String,
                   var contrasena: String,
                   var correo: String,
                   var calificaciones: MutableList<DataCalificaciones>)

