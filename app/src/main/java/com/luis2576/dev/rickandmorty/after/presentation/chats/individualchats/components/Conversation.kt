package com.luis2576.dev.rickandmorty.after.presentation.chats.individualchats.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.luis2576.dev.rickandmorty.after.domain.models.responses.DownloadConversationResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Conversation(
    modifier: Modifier = Modifier,
    conversationState: DownloadConversationResponse
) {
    when (conversationState) {
        DownloadConversationResponse.DownloadingConversation -> {
            CircularProgressIndicator()
        }
        is DownloadConversationResponse.Failure -> {

        }
        is DownloadConversationResponse.MessagesDownloaded -> {
            conversationState.conversation.let { conversation ->
            val messagesGroupedByDate = conversation.messages.groupBy {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.timestamp))
                }

                LazyColumn(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    messagesGroupedByDate.forEach { (date, messagesForDate) ->
                        stickyHeader {
                            DateHeader(date)
                        }
                        items(messagesForDate.sortedBy { it.timestamp }) { message ->
                            MessageItem(message = message)
                        }
                    }
                }
            }
        }
        DownloadConversationResponse.Void -> {
            Text(
                text = "Esperando descargar conversación"
            )
        }
    }


}