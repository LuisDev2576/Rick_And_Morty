package com.luis2576.dev.rickandmorty.after.presentation.contact.contactlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.luis2576.dev.rickandmorty.after.domain.models.responses.ContactListState
import com.luis2576.dev.rickandmorty.after.presentation.contact.contactlist.components.ContactsList
import com.luis2576.dev.rickandmorty.after.presentation.contact.contactlist.components.ErrorScreen
import com.luis2576.dev.rickandmorty.after.presentation.contact.contactlist.components.LoadingScreen

/**
 * Pantalla principal que muestra la lista de contactos o una pantalla de error/carga según el estado.
 *
 * @param contactsViewModel El ViewModel que gestiona el estado de la lista de contactos
 * @param navController El controlador de navegación para navegar a otras pantallas
 */
@Composable
fun ContactsScreen(
    contactsViewModel: ContactsViewModel,
    navController: NavHostController
) {
    val contactListState by contactsViewModel.contactsListState.collectAsState()

    when (contactListState) {
        is ContactListState.Loading -> {
            LoadingScreen()
        }
        is ContactListState.Success -> {
            val characters = (contactListState as ContactListState.Success).contacts
            ContactsList(characters, navController = navController)
        }
        is ContactListState.Error -> {
            val errorMessage = (contactListState as ContactListState.Error).message
            ErrorScreen(errorMessage)
        }
    }
}