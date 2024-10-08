package com.luis2576.dev.rickandmorty.presentation.shared

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis2576.dev.rickandmorty.domain.models.responses.DownloadUserDataResponse
import com.luis2576.dev.rickandmorty.domain.models.UserInformation
import com.luis2576.dev.rickandmorty.domain.repositories.AuthRepository
import com.luis2576.dev.rickandmorty.domain.repositories.UserInformationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInformationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userInformationRepository: UserInformationRepository,
) : ViewModel() {

    private var _userInformation = mutableStateOf<DownloadUserDataResponse<UserInformation>>(
        DownloadUserDataResponse.Void)
    val userInformation: State<DownloadUserDataResponse<UserInformation>> get() = _userInformation

    init {
        val uid = authRepository.getCurrentUser()?.uid
        viewModelScope.launch {
            if (uid != null) {
                _userInformation.value = DownloadUserDataResponse.DownloadingUserData
                userInformationRepository.downloadUserInformation(uid).collect { response ->
                    _userInformation.value = response
                }
            }
        }
    }
}
