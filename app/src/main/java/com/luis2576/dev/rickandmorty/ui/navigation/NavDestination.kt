package com.luis2576.dev.rickandmorty.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object SplashScreen
@Serializable object LoginScreen
@Serializable object RestartPasswordScreen
@Serializable object RegisterScreen

@Serializable object ChatsHomeScreen
@Serializable object ContactsScreen

@Serializable data class IndividualChatScreen(val contactId: String)
