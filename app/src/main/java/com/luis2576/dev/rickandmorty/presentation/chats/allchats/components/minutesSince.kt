package com.luis2576.dev.rickandmorty.presentation.chats.allchats.components

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

fun minutesSince(timestamp: Long): Long {
    val currentTimeMillis = System.currentTimeMillis()
    val diffInMillis = currentTimeMillis - timestamp
    return TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
}

fun esHoy(timestamp: LocalDateTime): Boolean {
    val today = LocalDate.now()
    return timestamp.toLocalDate().isEqual(today)
}

fun esAyer(timestamp: LocalDateTime): Boolean {
    val yesterday = LocalDate.now().minusDays(1)
    return timestamp.toLocalDate().isEqual(yesterday)
}