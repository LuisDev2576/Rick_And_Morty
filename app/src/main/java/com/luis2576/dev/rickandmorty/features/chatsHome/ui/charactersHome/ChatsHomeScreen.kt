package com.luis2576.dev.rickandmorty.features.chatsHome.ui.charactersHome

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.luis2576.dev.rickandmorty.features.authentication.data.responses.DownloadUserDataResponse
import com.luis2576.dev.rickandmorty.features.chatsHome.ui.charactersHome.components.ChatsPreviewList
import com.luis2576.dev.rickandmorty.features.chatsHome.ui.charactersHome.components.LoadingScreen
import com.luis2576.dev.rickandmorty.features.contacts.ui.presentation.components.ErrorScreen
import com.luis2576.dev.rickandmorty.util.userInformation.UserInformationViewModel

@Composable
fun ChatsHomeScreen(
    userInformationViewModel: UserInformationViewModel,
    navController: NavHostController
) {

    val userInformation by userInformationViewModel.userInformation

    when (userInformation) {
        is DownloadUserDataResponse.DownloadingUserData -> {
            LoadingScreen()
        }
        is DownloadUserDataResponse.UserInfoDownloaded -> {
            val userInfo = (userInformation as DownloadUserDataResponse.UserInfoDownloaded).data
            if (userInfo != null) {
                ChatsPreviewList(chatsPreview = userInfo.chatPreviews.sortedByDescending { it.timestamp }, navController = navController)

            }else{
                // TODO Manejar en caso de que userInfo sea n ull
            }
        }
        is DownloadUserDataResponse.Failure -> {
            val errorMessage = (userInformation as DownloadUserDataResponse.Failure).e.message
            ErrorScreen(errorMessage = errorMessage.toString())
        }
        else -> {
            Text(text = "No information available")
        }
    }
}



