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
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Conversation
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Message
import com.luis2576.dev.rickandmorty.features.individualChat.ui.presentation.components.ChatContent
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.LoadContactResponse
import com.luis2576.dev.rickandmorty.features.individualChat.ui.viewModel.IndividualChatViewModel
import com.luis2576.dev.rickandmorty.ui.navigation.ChatsHomeScreen
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
        contactId?.let {
            individualChatViewMode.loadContact(contactId)
            individualChatViewMode.downloadConversation(contactId, "aCYgcmKGjATnPvsUKgBNAAbew2h2")
        }
    }

    val contactResponse by individualChatViewMode.contact.collectAsState()
    val conversationResponse by individualChatViewMode.downloadedConversation.collectAsState()
    val sendMessageResponse by individualChatViewMode.sendMessageResult.collectAsState()

    when (val contactState = contactResponse) {
        is LoadContactResponse.LoadingContact -> {
            CircularProgressIndicator()
        }
        is LoadContactResponse.ContactLoaded -> {
            contactState.contact?.let {
                ChatContent(
                    contact = it,
                    backHome = {
                        navController.navigate(ChatsHomeScreen)
                    },
                    conversationState = conversationResponse,
                    message = individualChatViewMode.message,
                    onMessageChange = {
                        individualChatViewMode.onEmailChange(it)
                    },
                    onSendMessage = { conversation, contactId, message ->
                        individualChatViewMode.sendMessage(
                            contact = it,
                            userId = "aCYgcmKGjATnPvsUKgBNAAbew2h2",
                            conversation = conversation,
                            newMessage = message
                        )
                    },
                    sendMessageState = sendMessageResponse,
                    onSendFirstMessage = { contactId, message ->
                        individualChatViewMode.sendMessage(userId = "aCYgcmKGjATnPvsUKgBNAAbew2h2", contact = it, newMessage = message, conversation = Conversation("", listOf(message)))
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