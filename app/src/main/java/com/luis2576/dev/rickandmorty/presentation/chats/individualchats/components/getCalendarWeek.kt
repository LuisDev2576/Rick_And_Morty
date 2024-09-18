package com.luis2576.dev.rickandmorty.presentation.chats.individualchats.components

import java.util.Calendar
import java.util.Date

fun getCalendarWeek(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.WEEK_OF_YEAR)
}