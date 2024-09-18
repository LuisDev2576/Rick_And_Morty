package com.luis2576.dev.rickandmorty.domain.models

data class UserInformation(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val chatPreviews: List<ChatPreview> = emptyList()
)
