package com.luis2576.dev.rickandmorty.features.individualChat.domain.model

data class Conversation(
    val messages: List<Message>
)

data class Message(
    val text: String?,
    val imageUrl: String?,
    val timestamp: Long,
    val sendByMe: Boolean?,
    val read: Boolean? = null
)