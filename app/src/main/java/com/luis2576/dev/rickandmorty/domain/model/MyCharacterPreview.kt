package com.luis2576.dev.rickandmorty.domain.model

import javax.annotation.concurrent.Immutable

@Immutable
data class MyCharacterPreview(
    val id: String,
    val name: String,
    val image: String,
    val gender: String,
    val status: String
)
