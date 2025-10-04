package com.example.laboratorio5.data.remote

import com.example.laboratorio5.data.model.Pokemon
import java.net.URL

class ApiService {

    private val baseUrl = "https://pokeapi.co/api/v2"

    suspend fun fetchPokemonList(limit: Int = 100): List<Pokemon> {
        return try {
            val url = URL("$baseUrl/pokemon?limit=$limit")
            val jsonString = url.readText()
            parseJsonResponse(jsonString)
        } catch (e: Exception) {
            throw Exception("Error en la llamada a la API: ${e.message}")
        }
    }

    private fun parseJsonResponse(jsonString: String): List<Pokemon> {
        val pokemonList = mutableListOf<Pokemon>()

        try {
            val resultsStart = jsonString.indexOf("\"results\":[") + 11
            val resultsEnd = jsonString.indexOf("]", resultsStart)
            val resultsJson = jsonString.substring(resultsStart, resultsEnd)

            val objects = resultsJson.split("},")

            objects.forEachIndexed { index, obj ->
                try {
                    val cleanObj = obj.replace("{", "").replace("}", "")
                    val nameStart = cleanObj.indexOf("\"name\":\"") + 8
                    val nameEnd = cleanObj.indexOf("\"", nameStart)
                    val name = cleanObj.substring(nameStart, nameEnd)

                    val urlStart = cleanObj.indexOf("\"url\":\"") + 7
                    val urlEnd = cleanObj.indexOf("\"", urlStart)
                    val url = cleanObj.substring(urlStart, urlEnd)

                    val id = url.split("/").filter { it.isNotEmpty() }.last().toIntOrNull() ?: (index + 1)

                    pokemonList.add(Pokemon(id, name, url))
                } catch (e: Exception) {
                    // Ignorar errores de parseo individual
                }
            }
        } catch (e: Exception) {
            repeat(100) { i ->
                pokemonList.add(Pokemon(i + 1, "pokemon-${i + 1}", ""))
            }
        }

        return pokemonList
    }
}