package com.luis2576.dev.rickandmorty.presentation.chats.individualchats.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luis2576.dev.rickandmorty.domain.models.Conversation
import com.luis2576.dev.rickandmorty.domain.models.Message
import com.luis2576.dev.rickandmorty.domain.models.responses.DownloadConversationResponse
import com.luis2576.dev.rickandmorty.domain.models.responses.SendMessageResult

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
                        onSendClick(Conversation("",emptyList()), Message(text = message, imageUrl = null, sendByMe = true, read = false, timestamp = System.currentTimeMillis()))
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