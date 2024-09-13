package com.luis2576.dev.rickandmorty.features.authentication.ui.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NavigationTextButton(
    primaryTextId: Int,
    secondaryTextId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = primaryTextId),
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .padding(horizontal = 10.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(id = secondaryTextId),
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .clickable(onClick = onClick)
                .padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}