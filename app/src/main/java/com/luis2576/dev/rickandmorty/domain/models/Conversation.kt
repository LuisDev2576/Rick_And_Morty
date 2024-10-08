package com.luis2576.dev.rickandmorty.domain.models

data class Conversation(
    val contactPersonality: String = "",
    val messages: List<Message> = emptyList()
)

data class Message(
    val imageUrl: String? = null,
    val read: Boolean = false,
    val sendByMe: Boolean = true,
    val text: String? = null,
    val timestamp: Long = 0L
)