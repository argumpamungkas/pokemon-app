package com.argumpamungkas.pokemonapp.db

import java.util.UUID

data class PokemonModelDB (
    val uuid: String = autoUuid(),
    val name: String = "",
    val background: Int = 0,
){
    companion object {
        fun autoUuid(): String {
            val randomUUID = UUID.randomUUID()
            return randomUUID.toString()
        }
    }
}