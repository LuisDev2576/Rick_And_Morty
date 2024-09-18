package com.luis2576.dev.rickandmorty.presentation.chats.individualchats.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.luis2576.dev.rickandmorty.domain.models.responses.DownloadConversationResponse
import kotlinx.coroutines.delay
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

                val listState = rememberLazyListState()

                LaunchedEffect(messagesGroupedByDate) {
                    // Calcular el desplazamiento total necesario para llegar al final
                    val lastItemIndex = messagesGroupedByDate.size - 1
                    val lastItemOffset = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.offset ?: 0
                    val totalScroll = listState.layoutInfo.totalItemsCount * lastItemOffset

                    listState.animateScrollBy(
                        value = totalScroll.toFloat(), // Desplazarse hasta el final
                        animationSpec = tween(durationMillis = 2000, delayMillis = 400) // Animación suave de 1000ms
                    )
                }

                LazyColumn(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize(),
                    state = listState,
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