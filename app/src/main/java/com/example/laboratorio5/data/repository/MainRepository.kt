package com.example.laboratorio5.data.repository

import com.example.laboratorio5.data.model.Pokemon
import com.example.laboratorio5.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getPokemonList(): List<Pokemon> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonList(limit = 100)

                // Convertir PokemonBasic a Pokemon con ID extraído de la URL
                response.results.mapIndexed { index, pokemonBasic ->
                    val id = extractIdFromUrl(pokemonBasic.url) ?: (index + 1)
                    Pokemon(
                        id = id,
                        name = pokemonBasic.name,
                        url = pokemonBasic.url
                    )
                }
            } catch (e: Exception) {
                throw Exception("Error al obtener la lista de Pokémon: ${e.message}")
            }
        }
    }

    private fun extractIdFromUrl(url: String): Int? {
        return url.split("/").filter { it.isNotEmpty() }.lastOrNull()?.toIntOrNull()
    }

    fun getPokemonById(id: Int, pokemonList: List<Pokemon>): Pokemon? {
        return pokemonList.find { it.id == id }
    }
}