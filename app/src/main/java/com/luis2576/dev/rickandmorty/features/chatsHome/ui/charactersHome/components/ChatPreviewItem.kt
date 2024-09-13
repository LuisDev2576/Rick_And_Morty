package com.luis2576.dev.rickandmorty.features.chatsHome.ui.charactersHome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.luis2576.dev.rickandmorty.features.chatsHome.domain.model.ChatPreview
import kotlinx.coroutines.delay

@Composable
fun ChatPreviewItem(
    chatPreview: ChatPreview,
    onClick: () -> Unit
) {
    var minutos by remember { mutableStateOf(minutesSince(chatPreview.timestamp)) }
    LaunchedEffect(Unit) {
        minutos = minutesSince(chatPreview.timestamp)
        delay(60 * 1000)
    }
    LaunchedEffect(chatPreview.timestamp) {
        while (true) {
            minutos = minutesSince(chatPreview.timestamp)
            delay(60 * 1000)
        }
    }
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = chatPreview.characterImageUrl),
            contentDescription = "Character Image",
            modifier = Modifier
                .clickable { }
                .clip(CircleShape)
                .size(64.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = chatPreview.characterName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatearTiempo(minutos, chatPreview.timestamp),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp),
                        tint = if(chatPreview.read == false && chatPreview.sendByMe == false){ MaterialTheme.colorScheme.primary }else MaterialTheme.colorScheme.surface)

                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(chatPreview.sendByMe == true){
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = null,
                        modifier = Modifier,
                        tint = MaterialTheme.colorScheme.primary)
                }
                Text(
                    text = chatPreview.text?: "",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
