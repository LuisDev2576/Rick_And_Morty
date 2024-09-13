package com.luis2576.dev.rickandmorty.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luis2576.dev.rickandmorty.features.authentication.ui.presentation.login.LoginScreen
import com.luis2576.dev.rickandmorty.features.authentication.ui.presentation.register.RegisterScreen
import com.luis2576.dev.rickandmorty.features.authentication.ui.presentation.restartPassword.RestartPasswordScreen
import com.luis2576.dev.rickandmorty.features.chatsHome.ui.charactersHome.ChatsHomeScreen
import com.luis2576.dev.rickandmorty.features.contacts.ui.presentation.ContactsScreen
import com.luis2576.dev.rickandmorty.features.individualChat.ui.presentation.IndividualChatScreen
import com.luis2576.dev.rickandmorty.util.userInformation.UserInformationViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation() {
    SharedTransitionLayout {
        val navHostController = rememberNavController()

        NavHost(
            navController = navHostController,
            startDestination = LoginScreen
        ) {
            composable<SplashScreen> {
//                SplashScreen(
//                    navController = navHostController
//                )
            }
            composable<LoginScreen>{ LoginScreen(navController = navHostController) }
            composable<RestartPasswordScreen>{ RestartPasswordScreen(navController = navHostController) }
            composable<RegisterScreen>{ RegisterScreen(navController = navHostController) }

            composable<ChatsHomeScreen> { backStackEntry -> val userInformationViewModel: UserInformationViewModel = hiltViewModel(backStackEntry)
                ChatsHomeScreen(navController = navHostController, userInformationViewModel = userInformationViewModel)
            }
            composable<ContactsScreen> { ContactsScreen(navController = navHostController) }

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
