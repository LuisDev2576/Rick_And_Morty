package com.luis2576.dev.rickandmorty.data.local

import androidx.room.PrimaryKey

data class CharacterEntityPreview(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val image: String,
    val gender: String,
    val status: String
)
