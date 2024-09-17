package com.luis2576.dev.rickandmorty.after.presentation.chats.individualchats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.luis2576.dev.rickandmorty.after.domain.models.Conversation
import com.luis2576.dev.rickandmorty.after.domain.models.responses.DownloadUserDataResponse
import com.luis2576.dev.rickandmorty.after.domain.models.responses.LoadContactResponse
import com.luis2576.dev.rickandmorty.after.presentation.chats.allchats.components.ChatsPreviewList
import com.luis2576.dev.rickandmorty.after.presentation.chats.allchats.components.LoadingScreen
import com.luis2576.dev.rickandmorty.after.presentation.chats.individualchats.components.ChatContent
import com.luis2576.dev.rickandmorty.after.presentation.contact.contactlist.components.ErrorScreen
import com.luis2576.dev.rickandmorty.after.presentation.shared.UserInformationViewModel
import com.luis2576.dev.rickandmorty.after.ui.navigation.ChatsHomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualChatScreen(
    userInformationViewModel: UserInformationViewModel,
    contactId: String?,
    navController: NavHostController,
    individualChatViewMode: IndividualChatViewModel = hiltViewModel(),
){


    val contactResponse by individualChatViewMode.contact.collectAsState()
    val conversationResponse by individualChatViewMode.downloadedConversation.collectAsState()
    val sendMessageResponse by individualChatViewMode.sendMessageResult.collectAsState()
    val userInformation by userInformationViewModel.userInformation

    when (userInformation) {
        is DownloadUserDataResponse.DownloadingUserData -> {
            LoadingScreen()
        }
        is DownloadUserDataResponse.UserInfoDownloaded -> {
            val userInfo = (userInformation as DownloadUserDataResponse.UserInfoDownloaded).data
            if (userInfo != null) {
                // Llama a loadContact cada vez que se navega a la pantalla con un nuevo contactId
                LaunchedEffect(contactId) {
                    contactId?.let {
                        individualChatViewMode.loadContact(contactId)
                        individualChatViewMode.downloadConversation(contactId, userId = userInfo.uid)
                    }
                }

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
                                    individualChatViewMode.onMessage(it)
                                },
                                onSendMessage = { conversation, contactId, message ->
                                    individualChatViewMode.sendMessage(
                                        contact = it,
                                        userId = userInfo.uid,
                                        conversation = conversation,
                                        newMessage = message,
                                        userName = userInfo.name
                                    )
                                },
                                sendMessageState = sendMessageResponse,
                                onSendFirstMessage = { contactId, message ->
                                    individualChatViewMode.sendMessage(userId = userInfo.uid, userName = userInfo.uid, contact = it, newMessage = message, conversation = Conversation("", listOf(message)))
                                }
                            )
                        }
                    }
                    is LoadContactResponse.Failure -> {
                        Column(
                            modifier = Modifier.background(Color.Blue).fillMaxSize()
                        ) {
                            Text(
                                text = "Error al cargar el contacto: ${contactState.message}"
                            )
                        }
                    }
                    is LoadContactResponse.Void -> {
                        Column(
                            modifier = Modifier.background(Color.Red).fillMaxSize()
                        ) {
                            Text(text = "Esperando contacto...")
                        }
                    }
                }
            }else{
                Column(
                    modifier = Modifier.background(Color.Cyan).fillMaxSize()
                ) {
                    Text(text = "No information available")
                }
            }
        }
        is DownloadUserDataResponse.Failure -> {
            val errorMessage = (userInformation as DownloadUserDataResponse.Failure).e.message
            ErrorScreen(errorMessage = errorMessage.toString())
        }
        else -> {
            Column(
                modifier = Modifier.background(Color.Green).fillMaxSize()
            ) {
                Text(text = "No information available")
            }
        }
    }

}