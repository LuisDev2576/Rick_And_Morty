package com.luis2576.dev.rickandmorty.features.individualChat.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Conversation
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Contact
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Message
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.DownloadConversationResponse
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.SendMessageResult

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatContent(
    contact: Contact,
    backHome: () -> Unit,
    conversationState: DownloadConversationResponse,
    sendMessageState: SendMessageResult,
    message: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: (Conversation, String, Message) -> Unit,
    onSendFirstMessage: (String, Message) -> Unit,
){

    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = { ChatTopBar(contact = contact, backHome = backHome) },
        bottomBar = { ChatBottomBar(
            onSendClick = { conversation, message ->
                focusManager.clearFocus()
                onSendMessage(
                    conversation,
                    contact.id,
                    message
                )
            },
            message = message,
            onMessageChange = onMessageChange,
            conversationState = conversationState,
            sendMessageState = sendMessageState,
            onSendFirstMessage = { message ->
                focusManager.clearFocus()
                onSendFirstMessage(
                    contact.id,
                    message
                )
            }
        ) },
        content = { paddingValues ->
            Conversation(modifier = Modifier.padding(paddingValues), conversationState = conversationState)
        }
    )
}
