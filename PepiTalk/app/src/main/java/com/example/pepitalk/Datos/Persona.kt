package com.example.pepitalk.Datos

data class Persona(var tipo: String, var usuario: String,var nombre: String, var contrasena: String, var correo: String){
    companion object {
    var persona1 = Persona(
        tipo = "Cliente",
        usuario = "gabriel13",
        nombre = "Gabriel Espitia",
        contrasena = "clave123",
        correo = "gabriel13@gmail.com"
    )

    var persona2 = Persona(
        tipo = "Traductor",
        usuario = "juafra1",
        nombre = "Juanita Franco",
        contrasena = "clave456",
        correo = "juafra1@gmail.com"
    )

    var personaLog = Persona(
        tipo = "Traductor",
        usuario = "Pepito123",
        nombre = "Pepito el Pinguino",
        contrasena = "Pinguino",
        correo = "pepito@gmail.com")
    // Crear una lista que contenga las personas
    val personas = mutableListOf(persona1, persona2)
    }
}

