package com.luis2576.dev.rickandmorty.after.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestampToTime12Hour(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(date)
}