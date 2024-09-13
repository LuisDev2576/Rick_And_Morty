package com.luis2576.dev.rickandmorty.features.chatsHome.ui.charactersHome.components

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

fun formatearTiempo(minutos: Long, timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()

    return when {
        minutos < 60 -> {
            "${minutos}m"
        }
        minutos in 60..(3 * 60) -> {
            val horas = (minutos / 60.0).roundToInt()
            "${horas}h"
        }
        minutos in (3 * 60)..(12 * 60) -> {
            val formatter = DateTimeFormatter.ofPattern("h:mm a")
            val formattedTime = formatter.format(localDateTime)
            formattedTime.lowercase() // Convertir "AM" o "PM" a minúsculas
        }
        minutos >= (12 * 60) && esHoy(localDateTime) -> {
            "Hoy"
        }
        minutos >= (12 * 60) && esAyer(localDateTime) -> {
            "Ayer"
        }
        else -> {
            val formatter = DateTimeFormatter.ofPattern("EEEE") // Nombre del día de la semana
            formatter.format(localDateTime)
        }
    }
}