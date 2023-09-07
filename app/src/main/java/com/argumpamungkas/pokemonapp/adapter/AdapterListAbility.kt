package com.argumpamungkas.pokemonapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.argumpamungkas.pokemonapp.databinding.AdapterListAbilityBinding
import com.argumpamungkas.pokemonapp.model.Abilities
import com.argumpamungkas.pokemonapp.model.Ability

class AdapterListAbility(val abilities: ArrayList<Abilities>): RecyclerView.Adapter<AdapterListAbility.ViewHolder>() {

    class ViewHolder(val binding: AdapterListAbilityBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterListAbilityBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ability = abilities[position]
        holder.binding.tvAbility.text = "- ${ability.ability?.name}"
    }

    override fun getItemCount(): Int = abilities.size

    fun setData(newAbility: List<Abilities>){
        abilities.clear()
        abilities.addAll(newAbility)
        notifyDataSetChanged()
    }
}