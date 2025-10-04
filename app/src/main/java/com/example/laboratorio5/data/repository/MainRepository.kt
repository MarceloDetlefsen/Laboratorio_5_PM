package com.example.laboratorio5.data.repository

import com.example.laboratorio5.data.model.Pokemon
import com.example.laboratorio5.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(
    private val apiService: ApiService = ApiService()
) {

    suspend fun getPokemonList(): List<Pokemon> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.fetchPokemonList()
                response
            } catch (e: Exception) {
                throw Exception("Error al obtener la lista de Pokémon: ${e.message}")
            }
        }
    }

    fun getPokemonById(id: Int, pokemonList: List<Pokemon>): Pokemon? {
        return pokemonList.find { it.id == id }
    }
}