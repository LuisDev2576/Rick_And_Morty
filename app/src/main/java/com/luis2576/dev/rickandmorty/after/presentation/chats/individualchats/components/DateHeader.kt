package com.luis2576.dev.rickandmorty.after.presentation.chats.individualchats.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DateHeader(dateString: String) {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val yesterday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000))

    val headerText = when (dateString) {
        today -> "Hoy"
        yesterday -> "Ayer"
        else -> {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
            val isSameWeek = getCalendarWeek(date) == getCalendarWeek(Date())
            if (isSameWeek) {
                SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
            } else {
                SimpleDateFormat("d 'de' MMMM", Locale.getDefault()).format(date)
            }
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = headerText, modifier = Modifier.padding(vertical = 5.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.surface)
    }
}
