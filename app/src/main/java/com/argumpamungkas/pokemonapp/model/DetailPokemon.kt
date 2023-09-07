package com.argumpamungkas.pokemonapp.model

data class DetailPokemon(
    val sprites: Sprites?,
    val abilities: List<Abilities>?
)

data class Sprites(
    val front_default: String?
)

data class Abilities(
    val ability: Ability?
)

data class Ability(
    val name: String?,
)
