package com.example.laboratorio5.data.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val id: Int,
    val name: String,
    val url: String = "",
    val imageUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
)

data class PokemonListResponse(
    @SerializedName("results")
    val results: List<PokemonBasic>
)

data class PokemonBasic(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int = 0,
    val weight: Int = 0,
    val sprites: PokemonSprites
)

data class PokemonSprites(
    val front_default: String?,
    val back_default: String?,
    val front_shiny: String?,
    val back_shiny: String?
)