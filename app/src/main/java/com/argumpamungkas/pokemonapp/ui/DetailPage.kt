package com.argumpamungkas.pokemonapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.argumpamungkas.pokemonapp.adapter.AdapterListAbility
import com.argumpamungkas.pokemonapp.databinding.ActivityDetailPageBinding
import com.argumpamungkas.pokemonapp.model.Constant
import com.argumpamungkas.pokemonapp.model.DetailPokemon
import com.argumpamungkas.pokemonapp.network.ApiService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPage : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPageBinding
    private lateinit var listAbility: AdapterListAbility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        getDetailPokemon()
    }

    fun setupRecyclerView(){
        listAbility = AdapterListAbility(arrayListOf())
        binding.rvListAbility.adapter = listAbility
    }

    private fun getDetailPokemon(){
        ApiService().enpoint.getDetailPokemon(Constant.POKEMON_NAME)
            .enqueue(object : Callback<DetailPokemon> {
                override fun onResponse(
                    call: Call<DetailPokemon>,
                    response: Response<DetailPokemon>
                ) {
                    if (response.isSuccessful){
                        showPokemon(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<DetailPokemon>, t: Throwable) {
                    Log.d("detail", "errorResp: $t")
                }

            })
    }

    fun showPokemon(detail: DetailPokemon){
        binding.tvNamePokemon.text = Constant.POKEMON_NAME.capitalize()
        Picasso.get().load(detail.sprites?.front_default).into(binding.ivPokemon)
        listAbility.setData(detail.abilities!!)
    }
}