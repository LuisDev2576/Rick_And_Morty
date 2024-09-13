package com.luis2576.dev.rickandmorty.features.authentication.ui.presentation.restartPassword

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
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.features.authentication.data.responses.ResetPasswordResponse
import com.luis2576.dev.rickandmorty.features.authentication.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class RestartPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
    ) : ViewModel() {

    private var _restartPassworEmail by mutableStateOf("")
    val restartPassworEmail: String get() = _restartPassworEmail
    fun onRestartPassworEmailChange(newEmail: String) {
        _restartPassworEmail = newEmail
        _passwordResetStatus.value = ResetPasswordResponse.Void
    }
    private fun isEmailValid(email: String): Boolean {
        val pattern =
            Pattern.compile("^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE)
        return pattern.matcher(email).matches()
    }

    private var _restartPasswordButtonEnabled by mutableStateOf(true)
    val restartPasswordButtonEnabled: Boolean get() = _restartPasswordButtonEnabled

    private var _passwordResetStatus = mutableStateOf<ResetPasswordResponse<Unit>>(
        ResetPasswordResponse.Void)
    val passwordResetStatus: State<ResetPasswordResponse<Unit>> = _passwordResetStatus
    fun sendPasswordResetEmail() {
        viewModelScope.launch {
            when {
                !isEmailValid(restartPassworEmail) -> {
                    _passwordResetStatus.value = ResetPasswordResponse.EmailError(Exception(context.getString(R.string.error_invalid_email)))
                    withContext(Dispatchers.Main) {
                        showSnackBar(message = context.getString(R.string.error_invalid_email))
                    }
                }
                else -> {
                    _passwordResetStatus.value = ResetPasswordResponse.SendingEmailPassworReset
                    _restartPasswordButtonEnabled = false
                    val response = authRepository.sendPasswordResetEmail(restartPassworEmail)
                    _passwordResetStatus.value = response
                    when (response) {
                        is ResetPasswordResponse.PasswordResetEmailSent -> {
                            _restartPasswordButtonEnabled = true
                            withContext(Dispatchers.Main) {
                                showSnackBar(
                                    message = context.getString(R.string.password_reset_email_sent),
                                    duration = SnackbarDuration.Long
                                )
                            }
                        }
                        is ResetPasswordResponse.UserNotFoundError -> {
                            _restartPasswordButtonEnabled = true
                            withContext(Dispatchers.Main) {
                                showSnackBar(
                                    message = response.e.message
                                        ?: context.getString(R.string.error_unknown)
                                )
                            }
                        }
                        is ResetPasswordResponse.EmailError -> {
                            _restartPasswordButtonEnabled = true
                            withContext(Dispatchers.Main) {
                                showSnackBar(message = response.e.message ?: context.getString(R.string.error_unknown))
                            }
                        }
                        is ResetPasswordResponse.Failure -> {
                            _restartPasswordButtonEnabled = true
                            withContext(Dispatchers.Main) {
                                showSnackBar(message = response.e.message ?: context.getString(R.string.error_unknown))
                            }
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private val _snackbarHostState = SnackbarHostState()
    val snackbarHostState: SnackbarHostState get() = _snackbarHostState
    suspend fun showSnackBar(
        message: String,
        actionLabel: String? = null,
        actionPerformed: (() -> Unit)? = null,
        dismissedPerformed: (() -> Unit)? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ){
        _snackbarHostState.currentSnackbarData?.dismiss()
        when(_snackbarHostState.showSnackbar(message = message, actionLabel = actionLabel, duration = duration)){
            SnackbarResult.Dismissed -> {
                if (dismissedPerformed != null){
                    dismissedPerformed()
                }
            }
            SnackbarResult.ActionPerformed -> {
                if (actionPerformed != null) {
                    actionPerformed()
                }
            }
        }
    }
}