package com.luis2576.dev.rickandmorty.features.individualChat.ui.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.luis2576.dev.rickandmorty.features.individualChat.ui.presentation.components.ChatContent
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.LoadContactResponse
import com.luis2576.dev.rickandmorty.features.individualChat.ui.viewModel.IndividualChatViewModel
import com.luis2576.dev.rickandmorty.ui.navigation.ContactsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualChatScreen(
    contactId: String?,
    navController: NavHostController,
    individualChatViewMode: IndividualChatViewModel = hiltViewModel(),
){
    // Llama a loadContact cada vez que se navega a la pantalla con un nuevo contactId
    LaunchedEffect(contactId) {
        contactId?.let { individualChatViewMode.loadContact(contactId) }
    }

    val contactResponse by individualChatViewMode.contact.collectAsState()

    when (val contactState = contactResponse) {
        is LoadContactResponse.LoadingContact -> {
            CircularProgressIndicator()
        }
        is LoadContactResponse.ContactLoaded -> {
            contactState.contact?.let {
                ChatContent(
                    contact = it,
                    backHome = {
                        navController.navigate(ContactsScreen)
                    }
                )
            }
        }
        is LoadContactResponse.Failure -> {
            Text(
                text = "Error al cargar el contacto: ${contactState.message}"
            )
        }
        is LoadContactResponse.Void -> {
            Text(text = "Esperando contacto...")
        }
    }
}