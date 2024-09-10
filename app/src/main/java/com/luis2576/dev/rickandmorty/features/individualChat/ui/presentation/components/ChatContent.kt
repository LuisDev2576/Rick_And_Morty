package com.luis2576.dev.rickandmorty.features.individualChat.ui.presentation.components

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Contact

@Composable
fun ChatContent(
    contact: Contact,
    backHome: () -> Unit,
    //messages: NewChatClass,
    //chatScreenViewModel: ChatScreenViewModel
){

    Scaffold(
        topBar = { ChatTopBar(contact = contact, backHome = backHome) },
        bottomBar = { ChatBottomBar(onSendClick = { /* Lógica de envío aquí */ }) },
        content = { paddingValues ->
            paddingValues
            // Conversation(modifier = Modifier.padding(paddingValues), messages = messages)
        }
    )
}
