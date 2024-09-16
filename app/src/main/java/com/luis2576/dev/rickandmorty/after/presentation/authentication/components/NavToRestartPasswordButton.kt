package com.luis2576.dev.rickandmorty.after.presentation.authentication.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.luis2576.dev.rickandmorty.R

@Composable
fun NavToRestartPasswordButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.recover_password),
        modifier = modifier
            .clip(RoundedCornerShape(20))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium
    )
}
