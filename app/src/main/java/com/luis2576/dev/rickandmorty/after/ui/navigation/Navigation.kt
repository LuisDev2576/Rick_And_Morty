package com.luis2576.dev.rickandmorty.after.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luis2576.dev.rickandmorty.after.presentation.authentication.login.LoginScreen
import com.luis2576.dev.rickandmorty.after.presentation.authentication.register.RegisterScreen
import com.luis2576.dev.rickandmorty.after.presentation.authentication.resetpassword.ResetPasswordScreen
import com.luis2576.dev.rickandmorty.after.presentation.chats.allchats.ChatsHomeScreen
import com.luis2576.dev.rickandmorty.after.presentation.contact.contactlist.ContactsScreen
import com.luis2576.dev.rickandmorty.after.presentation.chats.individualchats.IndividualChatScreen
import com.luis2576.dev.rickandmorty.after.presentation.contact.contactlist.ContactsViewModel
import com.luis2576.dev.rickandmorty.after.presentation.shared.UserInformationViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation() {
    SharedTransitionLayout {
        val navHostController = rememberNavController()
        val contactsViewModel: ContactsViewModel = hiltViewModel()

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
            composable<RestartPasswordScreen>{ ResetPasswordScreen(navController = navHostController) }
            composable<RegisterScreen>{ RegisterScreen(navController = navHostController) }

            composable<ChatsHomeScreen> { backStackEntry -> val userInformationViewModel: UserInformationViewModel = hiltViewModel(backStackEntry)
                ChatsHomeScreen(navController = navHostController, userInformationViewModel = userInformationViewModel)
            }

            composable<ContactsScreen> {
                ContactsScreen(
                    navController = navHostController,
                    contactsViewModel = contactsViewModel
                )
            }

            composable<IndividualChatScreen> {backStackEntry -> val userInformationViewModel: UserInformationViewModel = hiltViewModel(backStackEntry)
                val contactId = backStackEntry.arguments?.getString("contactId")?: "1"
                IndividualChatScreen(contactId = contactId, navController = navHostController, userInformationViewModel = userInformationViewModel)
            }
        }
    }
}
