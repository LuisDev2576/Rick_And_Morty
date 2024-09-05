package com.luis2576.dev.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharactersDao {

    // Método para obtener un resumen de todos los personajes (sin todos los detalles)
    @Query("SELECT id, name, image, gender, status FROM CharacterEntity")
    suspend fun getAllCharactersPreview(): List<CharacterEntityPreview>

    // Método para insertar o actualizar una lista de personajes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCharacterList(characterList: List<CharacterEntity>)

    // Método para obtener un personaje por su ID
    @Query("SELECT * FROM CharacterEntity WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Int): CharacterEntity?

}