package com.luis2576.dev.rickandmorty.features.chatsHome.domain.model

data class ChatPreview(
    val characterId: String,
    val characterName: String,
    val characterImageUrl: String,
    val text: String?,
    val imageUrl: String?,
    val timestamp: Long,
    val sendByMe: Boolean?,
    val read: Boolean?
)