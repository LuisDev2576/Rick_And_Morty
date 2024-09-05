package com.luis2576.dev.rickandmorty.ui.presentation.charactersHome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.luis2576.dev.rickandmorty.domain.model.MyCharacterPreview

@Composable
fun CharactersHomeScreen(viewModel: CharactersHomeViewModel = hiltViewModel()) {
    val charactersListState by viewModel.charactersListState.collectAsState()

    when (charactersListState) {
        is CharactersHomeViewModel.CharactersListState.Loading -> {
            LoadingScreen()
        }
        is CharactersHomeViewModel.CharactersListState.Success -> {
            val characters = (charactersListState as CharactersHomeViewModel.CharactersListState.Success).characters
            CharactersList(characters)
        }
        is CharactersHomeViewModel.CharactersListState.Error -> {
            val errorMessage = (charactersListState as CharactersHomeViewModel.CharactersListState.Error).message
            ErrorScreen(errorMessage)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = errorMessage, color = Color.Red)
    }
}

@Composable
fun CharactersList(characters: List<MyCharacterPreview>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(characters) { character ->
            CharacterItem(character)
        }
    }
}

@Composable
fun CharacterItem(character: MyCharacterPreview) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = character.image),
            contentDescription = "Character Image",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = character.name, style = MaterialTheme.typography.titleLarge)
            Text(text = character.status, style = MaterialTheme.typography.titleMedium)
            Text(text = character.gender, style = MaterialTheme.typography.titleMedium)
        }
    }
}
