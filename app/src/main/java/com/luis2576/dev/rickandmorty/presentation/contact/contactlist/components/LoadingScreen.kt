package com.luis2576.dev.rickandmorty.presentation.contact.contactlist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Componente que muestra una pantalla de carga.
 */
@Composable
fun LoadingScreen() {
    //TODO Mejorar la pantalla de carga
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
