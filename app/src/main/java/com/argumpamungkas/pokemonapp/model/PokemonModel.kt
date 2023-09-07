package com.argumpamungkas.pokemonapp.model

data class PokemonResponse(
 val count: Int?,
 val next: String?,
 val results: List<PokemonModel>
)


data class PokemonModel(
 val name : String?
)

