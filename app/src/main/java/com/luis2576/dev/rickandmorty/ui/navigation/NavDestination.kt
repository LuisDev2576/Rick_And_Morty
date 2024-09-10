package com.luis2576.dev.rickandmorty.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object ContactsScreen

@Serializable data class IndividualChatScreen(val contactId: String)
