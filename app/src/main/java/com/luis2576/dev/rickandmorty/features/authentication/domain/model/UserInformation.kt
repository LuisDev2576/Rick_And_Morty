package com.luis2576.dev.rickandmorty.features.authentication.domain.model

import com.luis2576.dev.rickandmorty.features.chatsHome.domain.model.ChatPreview

data class UserInformation(
    val uid: String,
    val name: String,
    val email: String,
    val previewChats: List<ChatPreview>
)
