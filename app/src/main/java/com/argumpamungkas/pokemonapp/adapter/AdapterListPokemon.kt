package com.argumpamungkas.pokemonapp.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.argumpamungkas.pokemonapp.databinding.AdapterListBinding
import com.argumpamungkas.pokemonapp.db.PokemonModelDB
import com.argumpamungkas.pokemonapp.model.PokemonModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class AdapterListPokemon(var pokemons: ArrayList<PokemonModelDB>, val listener: OnAdapterListener): RecyclerView.Adapter<AdapterListPokemon.ViewHolder>() {

    class ViewHolder(val binding: AdapterListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.binding.tvNamePokemon.text = pokemon.name?.capitalize()
        holder.binding.rlBgtext.setBackgroundColor(pokemon.background)
        holder.itemView.setOnClickListener {
            listener.onClick(pokemon)
        }
    }

    override fun getItemCount(): Int = pokemons.size

    interface OnAdapterListener{
        fun onClick(pokemon: PokemonModelDB){}
    }

    fun setData(newPokemons: List<PokemonModelDB>){
        pokemons.clear()
        pokemons.addAll(newPokemons)
        notifyDataSetChanged()
    }

    fun setDataNext(newPokemons: List<PokemonModelDB>){
        pokemons.addAll(newPokemons)
        notifyDataSetChanged()
    }
}