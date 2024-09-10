package com.luis2576.dev.rickandmorty.features.individualChat.ui.presentation.components

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

@Composable
fun ChatBottomBar(onSendClick: () -> Unit) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 0.dp)
    ) {
        ChatTextField(
            name = "",
            onNameChange = { /* Lógica para actualizar el texto */ },
            isError = false,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        IconButton(
            onClick = onSendClick,
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.size(60.dp)
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