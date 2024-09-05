package com.luis2576.dev.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val status: String,
    val species: String,
    val type: String, // Puede estar vacío, pero sigue siendo un campo String
    val gender: String,
    val originName: String,
    val originUrl: String?,
    val locationName: String,
    val locationUrl: String?,
    val image: String,
    val episodeList: List<String>, // Lista de URLs de episodios
    val created: String
)
