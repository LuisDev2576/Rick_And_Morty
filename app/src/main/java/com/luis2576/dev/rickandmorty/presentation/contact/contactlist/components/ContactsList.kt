package com.luis2576.dev.rickandmorty.presentation.contact.contactlist.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.luis2576.dev.rickandmorty.domain.models.ContactPreview
import com.luis2576.dev.rickandmorty.ui.navigation.ChatsHomeScreen
import com.luis2576.dev.rickandmorty.ui.navigation.IndividualChatScreen

/**
 * Componente que muestra una lista de contactos.
 *
 * @param characters La lista de objetos ContactPreview que representan los contactos a mostrar
 * @param navController El controlador de navegación para navegar a otras pantallas
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactsList(
    characters: List<ContactPreview>,
    navController: NavHostController
) {

    var isSearchFieldOpen by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }

    val filteredList = characters
        .filter { it.name.contains(searchText, ignoreCase = true) }
        .sortedBy { it.name }

    val groupedContacts = filteredList.groupBy { it.name.first().uppercaseChar() }

    Scaffold(
        topBar = {
            if(!isSearchFieldOpen){
                TopAppBar(
                    title = {
                        Column {
                            Text(text = "Contactos", fontWeight = FontWeight.Bold)
                            Text(text = "${characters.size} contactos", fontSize = 16.sp)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate(ChatsHomeScreen) }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to home Buttom")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchFieldOpen = true }, modifier = Modifier.padding(end = 16.dp)) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Contact Button", modifier = Modifier.size(30.dp))
                        }
                    }
                )
            }else{
                TopAppBar(
                    title = {
                        SearchBar(searchText = searchText, onValueChange ={searchText = it})
                    },
                    navigationIcon = {
                        IconButton(onClick = { isSearchFieldOpen = false; searchText = "" }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to home Buttom")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchFieldOpen = false; searchText = "" }, modifier = Modifier.padding(end = 16.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Search Contact Button", modifier = Modifier.size(30.dp))
                        }
                    }

                )

            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {

                groupedContacts.forEach { (initial, contactsForInitial) ->

                    stickyHeader {
                        InputChip(
                            selected = true,
                            onClick = {},
                            label = {
                                Text(
                                    text = initial.toString(),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            enabled = false
                        )

                    }

                    // Mostrar los contactos para esta letra
                    items(contactsForInitial) { contact ->
                        ContactItem(
                            contact,
                            onClick = {
                                navController.navigate(IndividualChatScreen(contact.id))
                            }
                        )
                    }
                }
            }
        }
    )
}