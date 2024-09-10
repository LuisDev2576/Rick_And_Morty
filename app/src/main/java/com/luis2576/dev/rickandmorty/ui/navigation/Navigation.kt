package com.luis2576.dev.rickandmorty.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luis2576.dev.rickandmorty.features.contacts.ui.presentation.ContactsScreen
import com.luis2576.dev.rickandmorty.features.individualChat.ui.presentation.IndividualChatScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation() {
    SharedTransitionLayout {
        val navHostController = rememberNavController()

        NavHost(
            navController = navHostController,
            startDestination = ContactsScreen
        ) {
            composable<ContactsScreen> {
                ContactsScreen(navController = navHostController)
            }

            composable<IndividualChatScreen> {backStackEntry ->
                val contactId = backStackEntry.arguments?.getString("contactId")?: "1"
                IndividualChatScreen(
                    contactId = contactId,
                    navController = navHostController
                )
            }
        }
    }
}
