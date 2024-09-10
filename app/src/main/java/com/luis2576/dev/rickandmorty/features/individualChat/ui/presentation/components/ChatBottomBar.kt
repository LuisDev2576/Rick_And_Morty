package com.luis2576.dev.rickandmorty.features.individualChat.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Conversation
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Message
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.DownloadConversationResponse
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.SendMessageResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatBottomBar(
    onSendClick: (Conversation, Message) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    conversationState: DownloadConversationResponse,
    onSendFirstMessage: (Message) -> Unit,
    sendMessageState: SendMessageResult,
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 0.dp)
    ) {
        ChatTextField(
            name = message,
            onNameChange = { onMessageChange(it) },
            isError = false,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        IconButton(
            onClick = {
                when (conversationState) {
                    is DownloadConversationResponse.MessagesDownloaded -> {
                        conversationState.conversation.let { conversation ->
                            onSendClick(conversation, Message(text = message, imageUrl = null, sendByMe = true, read = false, timestamp = System.currentTimeMillis()))
                        }
                    }
                    else -> {
                        onSendClick(Conversation(emptyList()), Message(text = message, imageUrl = null, sendByMe = true, read = false, timestamp = System.currentTimeMillis()))
//                        onSendFirstMessage(
//                            Message(
//                                text = message,
//                                imageUrl = null,
//                                timestamp = System.currentTimeMillis(),
//                                sendByMe = true,
//                                read = false
//                            )
//                        )
                    }
                }
            },
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.size(60.dp),
            enabled = when (sendMessageState) { is SendMessageResult.SendingMessage -> { false } else -> { true } }
        ) {

            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send Message",
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(30.dp)
            )

        }


    }
}