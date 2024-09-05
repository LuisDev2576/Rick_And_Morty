package com.luis2576.dev.rickandmorty.domain.model

import javax.annotation.concurrent.Immutable

@Immutable
data class MyCharacter(
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
