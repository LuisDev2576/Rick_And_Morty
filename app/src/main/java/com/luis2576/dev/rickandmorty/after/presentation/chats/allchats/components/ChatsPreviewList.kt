package com.luis2576.dev.rickandmorty.after.presentation.chats.allchats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.luis2576.dev.rickandmorty.after.domain.models.ChatPreview
import com.luis2576.dev.rickandmorty.after.ui.navigation.ContactsScreen
import com.luis2576.dev.rickandmorty.after.ui.navigation.IndividualChatScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsPreviewList(chatsPreview: List<ChatPreview>, navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Citadel", fontSize = 32.sp, fontWeight = FontWeight.Bold) },
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.Forum, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(text = "Chats", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Button(onClick = { navController.navigate(ContactsScreen) }) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Text(text = "New Chat")
                        }
                    }
                    Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                            Text(text = "Settings", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
    ){
        LazyColumn(modifier = Modifier
            .padding(it)
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()) {
            items(chatsPreview) { chatPreview ->
                ChatPreviewItem(
                    chatPreview = chatPreview,
                    onClick = {
                        navController.navigate(IndividualChatScreen(contactId = chatPreview.characterId))
                    }
                )
            }
        }
    }



}
