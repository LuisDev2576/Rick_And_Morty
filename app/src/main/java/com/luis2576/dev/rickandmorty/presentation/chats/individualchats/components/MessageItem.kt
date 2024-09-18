package com.luis2576.dev.rickandmorty.presentation.chats.individualchats.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.luis2576.dev.rickandmorty.domain.models.Message
import com.luis2576.dev.rickandmorty.util.formatTimestampToTime12Hour

@Composable
fun MessageItem(
    message: Message
) {

    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (message.sendByMe == true)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .align(
                    if (message.sendByMe == true) Alignment.End else Alignment.Start
                )
                .padding(
                    start = if (message.sendByMe == true) 100.dp else 0.dp,
                    end = if (message.sendByMe == false) 100.dp else 0.dp
                ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp).widthIn(min = 150.dp)
            ) {

                message.imageUrl?.let { im1 ->
                    Image(
                        painter = rememberAsyncImagePainter(model = im1),
                        contentDescription = "ContactsDto Image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {  }
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                message.text?.let { it1 ->
                    Text(
                        text = it1,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .wrapContentWidth(Alignment.Start), // No se expande más de lo necesario
                        color = if (message.sendByMe == true)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Texto principal

                // Segundo texto (timestamp)
                Text(
                    text = formatTimestampToTime12Hour(message.timestamp),
                    color = if (message.sendByMe == true)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.End), // Alinea a la derecha el timestamp
                )
            }
        }
    }


}
