package com.luis2576.dev.rickandmorty.features.contacts.ui.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Componente que muestra una pantalla de error con un mensaje.
 *
 * @param errorMessage El mensaje de error a mostrar
 */
@Composable
fun ErrorScreen(errorMessage: String) {
    //TODO Mejorar la pantalla de error
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = errorMessage, color = Color.Red)
    }
}