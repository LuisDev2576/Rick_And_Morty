package com.luis2576.dev.rickandmorty.after.domain.models

data class UserInformation(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val chatPreviews: List<ChatPreview> = emptyList()
)
