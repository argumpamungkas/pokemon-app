package com.argumpamungkas.pokemonapp.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "pokemon.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "table_pokemon"
        private const val UUID = "uuid"
        private const val NAME = "name"
        private const val BACKGROUND = "background"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTablePokemon = ("CREATE TABLE " + TABLE_NAME + "("
                + UUID + " TEXT PRIMARY KEY," + NAME + " TEXT,"
                + BACKGROUND + " INT" + ")"
                )
        db?.execSQL(createTablePokemon)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVer: Int, newVer: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertPokemon(pokemon: PokemonModelDB): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(UUID, pokemon.uuid)
        contentValues.put(NAME, pokemon.name)
        contentValues.put(BACKGROUND, pokemon.background)

        val selection = "$NAME = ? AND $UUID != ? OR $UUID = ?"
        val selectionArgs = arrayOf(pokemon.name, pokemon.uuid)

        val cursor = db.query(
            TABLE_NAME,
            arrayOf(UUID),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.count == 0) {
            val insertSuccess = db.insert(TABLE_NAME, null, contentValues)
            cursor.close()
            db.close()
            insertSuccess
        } else {
            cursor.close()
            db.close()
            return -1
        }

    }

    @SuppressLint("Range")
    fun getAllPokemon(): ArrayList<PokemonModelDB> {
        val pokemonList: ArrayList<PokemonModelDB> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var uuid: String
        var name: String
        var background: Int

        if (cursor.moveToFirst()) {
            do {
                uuid = cursor.getString(cursor.getColumnIndex("uuid"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                background = cursor.getInt(cursor.getColumnIndex("background"))
                val pokemon = PokemonModelDB(uuid = uuid, name = name, background = background)
                pokemonList.add(pokemon)
            } while (cursor.moveToNext())
        }
        return pokemonList
    }

    @SuppressLint("Range")
    fun getAllPokemonAsc(): ArrayList<PokemonModelDB> {
        val pokemonList: ArrayList<PokemonModelDB> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $NAME ASC"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var uuid: String
        var name: String
        var background: Int

        if (cursor.moveToFirst()) {
            do {
                uuid = cursor.getString(cursor.getColumnIndex("uuid"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                background = cursor.getInt(cursor.getColumnIndex("background"))
                val pokemon = PokemonModelDB(uuid = uuid, name = name, background = background)
                pokemonList.add(pokemon)
            } while (cursor.moveToNext())
        }
        return pokemonList
    }

    @SuppressLint("Range")
    fun getAllPokemonDesc(): ArrayList<PokemonModelDB> {
        val pokemonList: ArrayList<PokemonModelDB> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $NAME DESC"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var uuid: String
        var name: String
        var background: Int

        if (cursor.moveToFirst()) {
            do {
                uuid = cursor.getString(cursor.getColumnIndex("uuid"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                background = cursor.getInt(cursor.getColumnIndex("background"))
                val pokemon = PokemonModelDB(uuid = uuid, name = name, background = background)
                pokemonList.add(pokemon)
            } while (cursor.moveToNext())
        }
        return pokemonList
    }


}