package com.luis2576.dev.rickandmorty.features.contacts.ui.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.luis2576.dev.rickandmorty.features.contacts.domain.model.ContactPreview

/**
 * Componente que representa un elemento de contacto en la lista.
 *
 * @param contact El objeto ContactPreview que contiene la información del contacto.
 * @param onClick La función a ejecutar cuando se hace clic en el elemento.
 */
@Composable
fun ContactItem(
    contact: ContactPreview,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = contact.image),
            contentDescription = "Contact Image",
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = contact.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

    }
}
