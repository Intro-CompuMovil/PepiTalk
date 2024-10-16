package com.example.pepitalk.Datos

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

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
            correo = "pepito@gmail.com",
            calificaciones = mutableListOf(DataCalificaciones(5.0, "Excelente")))

    //funciones

        // Método para cargar JSON desde assets
        fun loadJSONFromAsset(context: Context, fileName: String): String? {
            var json: String? = null
            try {
                val isStream: InputStream = context.assets.open(fileName)
                val size: Int = isStream.available()
                val buffer = ByteArray(size)
                isStream.read(buffer)
                isStream.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }
            return json
        }
        //funcion para cargar los grupos del json a la lista de grupos
        fun loadGruposFromJson(context: Context) {
            val jsonString = loadJSONFromAsset(context, "grupos.json")
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val jsonArray = jsonObject.getJSONArray("listaGrupos")

                // Limpiamos la lista antes de agregar los nuevos grupos
                listaGrupos.clear()

                // Iteramos sobre cada objeto en el array de grupos
                for (i in 0 until jsonArray.length()) {
                    val grupoJson = jsonArray.getJSONObject(i)

                    // Convertimos los arrays de integrantes y calificaciones
                    val integrantesArray = grupoJson.getJSONArray("integrantes")
                    val calificacionesArray = grupoJson.getJSONArray("calificaciones")

                    // Creamos listas para almacenar los valores
                    val integrantesList = mutableListOf<String>()
                    val calificacionesList = mutableListOf<DataCalificaciones>()

                    // Llenamos la lista de integrantes
                    for (j in 0 until integrantesArray.length()) {
                        integrantesList.add(integrantesArray.getString(j))
                    }

                    // Llenamos la lista de calificaciones
                    for (j in 0 until calificacionesArray.length()) {
                        val calificacion = calificacionesArray.getInt(j)
                        calificacionesList.add(DataCalificaciones(calificacion.toDouble(),"Comentario opcional"))
                    }

                    // Creamos un nuevo objeto DataGrupo y lo añadimos a la lista
                    val grupo = DataGrupo(
                        nombre = grupoJson.getString("nombre"),
                        idioma = grupoJson.getString("idioma"),
                        nivel = grupoJson.getString("nivel"),
                        descripcion = grupoJson.getString("descripcion"),
                        dueno = grupoJson.getString("dueno"),
                        integrantes = integrantesList,
                        calificaciones = calificacionesList
                    )

                    listaGrupos.add(grupo)  // Añadimos el grupo a la lista
                }
            }
        }
        //funcion para cargar las personas del json a la lista de personas
        fun loadPersonasFromJson(context: Context) {
            val jsonString = loadJSONFromAsset(context, "personas.json")
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val jsonArray = jsonObject.getJSONArray("listaPersonas")

                // Limpiamos la lista antes de agregar las nuevas personas
                personas.clear()

                // Iteramos sobre cada objeto en el array de personas
                for (i in 0 until jsonArray.length()) {
                    val personaJson = jsonArray.getJSONObject(i)
                    val calificacionesArray = personaJson.getJSONArray("calificaciones")
                    val calificacionesList = mutableListOf<DataCalificaciones>()

                    // Llenamos la lista de calificaciones
                    for (j in 0 until calificacionesArray.length()) {
                        val calificacion = calificacionesArray.getInt(j)
                        calificacionesList.add(DataCalificaciones(calificacion.toDouble(),"Comentario opcional"))
                    }

                    // Creamos un nuevo objeto Persona y lo añadimos a la lista
                    val persona = Persona(
                        tipo = personaJson.getString("tipo"),
                        usuario = personaJson.getString("usuario"),
                        nombre = personaJson.getString("nombre"),
                        contrasena = personaJson.getString("contrasena"),
                        correo = personaJson.getString("correo"),
                        calificaciones = calificacionesList
                    )

                    personas.add(persona)  // Añadimos la persona a la lista
                }
            }
        }

        //funcion para cargar las ofertas del json a la lista de ofertas
        fun loadOfertasFromJson(context: Context) {
            val jsonString = loadJSONFromAsset(context, "ofertas.json")
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val jsonArray = jsonObject.getJSONArray("listaOfertas")

                val calificacionesList = mutableListOf<DataCalificaciones>()

                // Limpiamos la lista antes de agregar las nuevas ofertas
                listaOfertas.clear()


                // Iteramos sobre cada objeto en el array de ofertas
                for (i in 0 until jsonArray.length()) {
                    val ofertaJson = jsonArray.getJSONObject(i)



                    // Creamos un nuevo objeto DataOferta y lo añadimos a la lista
                    val oferta = DataOferta(
                        idioma = ofertaJson.getString("idioma"),
                        fecha = ofertaJson.getString("fecha"),
                        horaInicio = ofertaJson.getString("horaInicio"),
                        horaFinal = ofertaJson.getString("horaFin"),
                        lugar = ofertaJson.getString("lugar"),
                        descripcion = ofertaJson.getString("descripcion"),
                        dueno = ofertaJson.getString("dueno"),
                        trabajador = ofertaJson.getString("trabajador"),
                        aceptado = ofertaJson.getBoolean("aceptado")
                    )
                    listaOfertas.add(oferta)  // Añadimos la oferta a la lista
                }
            }
        }
        //funcion para cargar las reuniones del json a la lista de reuniones
      fun loadReunionesFromJson(context: Context) {
            val jsonString = loadJSONFromAsset(context, "reuniones.json")
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val jsonArray = jsonObject.getJSONArray("listaReuniones")

                // Limpiamos la lista antes de agregar las nuevas reuniones
                listaReuniones.clear()

                // Iteramos sobre cada objeto en el array de reuniones
                for (i in 0 until jsonArray.length()) {
                    val reunionJson = jsonArray.getJSONObject(i)

                    val integrantesArray = reunionJson.getJSONArray("integrantes")
                    val calificacionesArray = reunionJson.getJSONArray("calificaciones")

                    // Creamos listas para almacenar los valores
                    val integrantesList = mutableListOf<String>()
                    val calificacionesList = mutableListOf<DataCalificaciones>()

                    // Llenamos la lista de integrantes
                    for (j in 0 until integrantesArray.length()) {
                        integrantesList.add(integrantesArray.getString(j))
                    }

                    // Llenamos la lista de calificaciones
                    for (j in 0 until calificacionesArray.length()) {
                        val calificacion = calificacionesArray.getInt(j)
                        calificacionesList.add(DataCalificaciones(calificacion.toDouble(),"Comentario opcional"))
                    }

                    // Creamos un nuevo objeto DataReunion y lo añadimos a la lista
                    val reunion = DataReunion(
                        nombre = reunionJson.getString("nombre"),
                        dia = reunionJson.getString("dia"),
                        hora = reunionJson.getString("hora"),
                        idioma = reunionJson.getString("idioma"),
                        nivel = reunionJson.getString("nivel"),
                        lugar = reunionJson.getString("lugar"),
                        descripcion = reunionJson.getString("descripcion"),
                        dueno = reunionJson.getString("dueno"),
                        integrantes = integrantesList,
                        calificaciones = calificacionesList
                    )
                    listaReuniones.add(reunion)  // Añadimos la reunión a la lista
                }
            }
        }
    }


}