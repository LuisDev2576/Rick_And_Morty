package com.luis2576.dev.rickandmorty.before.features.authentication.ui.presentation.login

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.before.features.authentication.data.responses.DownloadUserDataResponse
import com.luis2576.dev.rickandmorty.before.features.authentication.data.responses.LoginResponse
import com.luis2576.dev.rickandmorty.before.features.authentication.domain.repository.AuthRepository
import com.luis2576.dev.rickandmorty.before.ui.navigation.ChatsHomeScreen
import com.luis2576.dev.rickandmorty.before.util.userInformation.UserInformationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userInformationRepository: UserInformationRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var _loginButtonEnabled by mutableStateOf(true)
    val loginButtonEnabled: Boolean get() = _loginButtonEnabled

    private var _email by mutableStateOf("")
    val email: String get() = _email

    private var _password by mutableStateOf("")
    val password: String get() = _password

    private var _loginStatus = mutableStateOf<LoginResponse<FirebaseUser>>(LoginResponse.UnLogged)
    val loginStatus: State<LoginResponse<FirebaseUser>> = _loginStatus

    private var _navigateToScreen = mutableStateOf<Any?>(null)
    val navigateToScreen: State<Any?> get() = _navigateToScreen

    private val _snackbarHostState = SnackbarHostState()
    val snackbarHostState: SnackbarHostState get() = _snackbarHostState

    fun onEmailChange(newEmail: String) {
        _email = newEmail
        resetLoginStatusIfError()
    }

    fun onPasswordChange(newPassword: String) {
        _password = newPassword
        resetLoginStatusIfError()
    }

    fun onLoginClick() {
        loginUser(email = _email, password = _password)
    }



    private fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile("^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE)
        return pattern.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            when {
                !isEmailValid(email) -> {
                    handleLoginError(LoginResponse.EmailError(Exception(context.getString(R.string.error_invalid_email))))
                }
                !isPasswordValid(password) -> {
                    handleLoginError(LoginResponse.PasswordError(Exception(context.getString(R.string.error_short_password))))
                }
                else -> {
                    _loginButtonEnabled = false
                    _loginStatus.value = LoginResponse.LoadingLogin
                    val response = authRepository.loginUser(email, password)
                    _loginStatus.value = response
                    if (response is LoginResponse.Logged) {
                        response.data?.let { determineUserTypeAndNavigate(it.uid) }
                    } else {
                        handleLoginError(response)
                    }
                }
            }
        }
    }

    private suspend fun ShowSnackBar(
        message: String,
        actionLabel: String? = null,
        actionPerformed: (() -> Unit)? = null,
        dismissedPerformed: (() -> Unit)? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        _snackbarHostState.currentSnackbarData?.dismiss()
        when (_snackbarHostState.showSnackbar(message = message, actionLabel = actionLabel, duration = duration)) {
            SnackbarResult.Dismissed -> dismissedPerformed?.invoke()
            SnackbarResult.ActionPerformed -> actionPerformed?.invoke()
        }
    }

    private fun determineUserTypeAndNavigate(uid: String) {
        viewModelScope.launch {
            // Inicia la descarga de información del usuario
            userInformationRepository.downloadUserInformation(uid).collect { response ->
                when (response) {
                    is DownloadUserDataResponse.UserInfoDownloaded -> {
                        if (response.data != null) {
                            _navigateToScreen.value = ChatsHomeScreen
                        } else {
                            _loginButtonEnabled = true
                            withContext(Dispatchers.Main) {
                                ShowSnackBar(message = "Usuario desconocido")
                            }
                        }
                    }
                    is DownloadUserDataResponse.Failure -> {
                        _loginButtonEnabled = true
                        withContext(Dispatchers.Main) {
                            ShowSnackBar(message = "Error al descargar información del usuario")
                        }
                    }
                    else -> {
                        // Maneja otros posibles casos de respuesta aquí si es necesario
                    }
                }
            }
        }
    }


    private fun resetLoginStatusIfError() {
        when (loginStatus.value) {
            is LoginResponse.EmailError,
            is LoginResponse.UserNotFoundError,
            is LoginResponse.PasswordError,
            is LoginResponse.TooManyRequestsError -> {
                _loginStatus.value = LoginResponse.UnLogged
            }
            else -> { /* No reset needed */ }
        }
    }

    private fun handleLoginError(response: LoginResponse<*>) {

        viewModelScope.launch {
            when(response){
                is LoginResponse.PasswordError  -> {
                    _loginButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        ShowSnackBar(message = response.e.message!!)
                    }
                }
                is LoginResponse.EmailError  -> {
                    _loginButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        ShowSnackBar(message = response.e.message!!)
                    }
                }
                is LoginResponse.Failure  -> {
                    _loginButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        ShowSnackBar(message = response.e.message!!)
                    }
                }
                is LoginResponse.TooManyRequestsError  -> {
                    _loginButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        ShowSnackBar(message = response.e.message!!)
                    }
                }
                is LoginResponse.UserNotFoundError  -> {
                    _loginButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        ShowSnackBar(message = response.e.message!!)
                    }
                }
                else -> {}
            }
        }
    }
}
