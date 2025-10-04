package com.example.laboratorio5.data.remote

import com.example.laboratorio5.data.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 100
    ): PokemonListResponse
}