package com.luis2576.dev.rickandmorty.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luis2576.dev.rickandmorty.features.contacts.ui.presentation.ContactsScreen

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

            composable<CharacterDetails> {

            }
        }
    }
}
