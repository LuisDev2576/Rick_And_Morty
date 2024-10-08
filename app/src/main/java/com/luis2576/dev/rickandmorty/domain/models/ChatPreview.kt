package com.luis2576.dev.rickandmorty.domain.models

data class ChatPreview(
    val characterId: String = "",
    val characterName: String = "",
    val characterImageUrl: String = "",
    val text: String? = null,
    val imageUrl: String? = "",
    val timestamp: Long = 0L,
    val sendByMe: Boolean? = null,
    val read: Boolean? = null
)