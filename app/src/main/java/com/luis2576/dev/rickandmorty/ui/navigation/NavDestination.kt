package com.luis2576.dev.rickandmorty.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object CharactersHome

@Serializable
data class CharacterDetails(val characterId: String)
