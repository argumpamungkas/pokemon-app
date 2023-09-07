package com.argumpamungkas.pokemonapp.network

import com.argumpamungkas.pokemonapp.model.DetailPokemon
import com.argumpamungkas.pokemonapp.model.PokemonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndpoint {

    @GET("pokemon/")
    fun getListPokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Call<PokemonResponse>

    @GET("pokemon/{name}")
    fun getDetailPokemon(
        @Path("name") name: String
    ): Call<DetailPokemon>
}