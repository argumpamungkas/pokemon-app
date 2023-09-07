package com.argumpamungkas.pokemonapp.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import com.argumpamungkas.pokemonapp.R
import com.argumpamungkas.pokemonapp.adapter.AdapterListPokemon
import com.argumpamungkas.pokemonapp.databinding.ActivityHomePageBinding
import com.argumpamungkas.pokemonapp.db.PokemonModelDB
import com.argumpamungkas.pokemonapp.db.SQLiteHelper
import com.argumpamungkas.pokemonapp.model.Constant
import com.argumpamungkas.pokemonapp.model.PokemonResponse
import com.argumpamungkas.pokemonapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var listAdapter: AdapterListPokemon

    private lateinit var sqliteHelper: SQLiteHelper

    private lateinit var filteredList: ArrayList<PokemonModelDB>

    private var isScrolling = false
    private var offset = 0
    private var totalCount = 0
    private var sorted = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sqliteHelper = SQLiteHelper(this)
        setupRecyclerView()
        setupListener()
        addPokemon()
        searchPokemon()
        showLoadingNext(false)

    }

    override fun onStart() {
        super.onStart()
        binding.llSort.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.llSort)
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.default_menu -> {
                        sorted = 0
                        getPokemon()
                    }
                    R.id.ascending -> {
                        sorted = 1
                        getPokemonAsc()
                    }
                    R.id.descending -> {
                        sorted = 2
                        getPokemonDesc()
                    }
                }
                true
            }
            popupMenu.show()
        }
    }

    private fun setupRecyclerView() {
        listAdapter =
            AdapterListPokemon(arrayListOf(), object : AdapterListPokemon.OnAdapterListener {
                override fun onClick(pokemon: PokemonModelDB) {
                    super.onClick(pokemon)
                    Constant.POKEMON_NAME = pokemon.name
                    startActivity(Intent(applicationContext, DetailPage::class.java))
                }
            })
        binding.rvListPokemon.adapter = listAdapter
    }

    private fun setupListener(){
        binding.nestedScroll.setOnScrollChangeListener(object : OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight){
                    if (!isScrolling){
                        if (offset <= totalCount){
                            addPokemonNext()
                        }
                    }
                }
            }

        })
    }

    private fun addPokemon() {
        offset = 0
        showLoading(true)
        ApiService().enpoint.getListPokemon(offset, 20)
            .enqueue(object : Callback<PokemonResponse> {
                override fun onResponse(
                    call: Call<PokemonResponse>,
                    response: Response<PokemonResponse>
                ) {
                    val rndm = Random()
                    if (response.isSuccessful) {
                        for (data in response.body()?.results!!) {
                            val color = Color.argb(
                                255,
                                rndm.nextInt(255 - 150) + 150,
                                rndm.nextInt(255 - 150) + 150,
                                rndm.nextInt(255 - 150) + 150
                            )
                            val pokemon = PokemonModelDB(
                                name = data.name!!,
                                background = color
                            )
                            sqliteHelper.insertPokemon(pokemon)
                        }
                        showLoading(false)
                        when(sorted){
                            0 -> getPokemon()
                            1 -> getPokemonAsc()
                            2 -> getPokemonDesc()
                        }
                    }
                }

                override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                    showLoading(false)
                    Log.d("listpokemon", "errorResponse: $t")
                }

            })

    }

    private fun addPokemonNext() {
        offset += 20
        showLoadingNext(true)
        ApiService().enpoint.getListPokemon(offset, 20)
            .enqueue(object : Callback<PokemonResponse> {
                override fun onResponse(
                    call: Call<PokemonResponse>,
                    response: Response<PokemonResponse>
                ) {
                    val rndm = Random()
                    if (response.isSuccessful) {
                        totalCount = response.body()?.count!!
                        for (data in response.body()?.results!!) {
                            val color = Color.argb(
                                255,
                                rndm.nextInt(255 - 150) + 150,
                                rndm.nextInt(255 - 150) + 150,
                                rndm.nextInt(255 - 150) + 150
                            )
                            val pokemon = PokemonModelDB(
                                name = data.name!!,
                                background = color
                            )
                            val data = sqliteHelper.insertPokemon(pokemon)
                            if (data == -1L){
                                showMessage("Data sudah ada")
                            }
                        }
                        showLoadingNext(false)
                        when(sorted){
                            0 -> getPokemon()
                            1 -> getPokemonAsc()
                            2 -> getPokemonDesc()
                        }
                    }
                }

                override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                    showLoadingNext(false)
                    Log.d("listpokemon", "errorResponse: $t")
                }

            })

    }

    fun getPokemon() {
        val listPokemon = sqliteHelper.getAllPokemon()
        listAdapter.setData(listPokemon)
        filteredList = listPokemon
    }

    private fun getPokemonAsc() {
        val listPokemon = sqliteHelper.getAllPokemonAsc()
        listAdapter.setData(listPokemon)
        filteredList = listPokemon
    }

    private fun getPokemonDesc() {
        val listPokemon = sqliteHelper.getAllPokemonDesc()
        listAdapter.setData(listPokemon)
        filteredList = listPokemon
    }

    private fun searchPokemon() {
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPokemon(newText!!)
                return true
            }

        })
    }

    fun filterPokemon(query: String) {
        val listFilter = ArrayList<PokemonModelDB>()
        for (i in filteredList) {
            if (i.name.lowercase(Locale.ROOT).contains(query)) {
                listFilter.add(i)
            }
        }

        if (listFilter.isEmpty()) {
            showMessage("No Data")
        } else {
            listAdapter.setData(listFilter)
        }
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> binding.pbLoading.visibility = View.VISIBLE
            false -> binding.pbLoading.visibility = View.GONE
        }
    }

    private fun showLoadingNext(loading: Boolean) {
        when (loading) {
            true -> {
                isScrolling
                binding.pbLoadingNext.visibility = View.VISIBLE
            }
            false -> {
                !isScrolling
                binding.pbLoadingNext.visibility = View.GONE
            }
        }
    }

    private fun showMessage(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}