package com.luis2576.dev.rickandmorty.features.individualChat.ui.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Conversation(
    modifier: Modifier = Modifier,
    //messages: NewChatClass
) {
//    val messagesGroupedByDate = messages.messages.groupBy {
//        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.timestamp))
//    }
//
//    LazyColumn(
//        modifier = modifier
//            .background(MaterialTheme.colorScheme.surface)
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        messagesGroupedByDate.forEach { (date, messagesForDate) ->
//            stickyHeader {
//                DateHeader(date)
//            }
//            items(messagesForDate.sortedBy { it.timestamp }) { message ->
//                MessageItem(message = message)
//            }
//        }
//    }
}