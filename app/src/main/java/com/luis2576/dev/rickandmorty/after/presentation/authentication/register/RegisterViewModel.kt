package com.luis2576.dev.rickandmorty.after.presentation.authentication.register

import android.content.Context
import android.util.Log
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
import com.luis2576.dev.rickandmorty.after.domain.models.responses.RegistrationResponse
import com.luis2576.dev.rickandmorty.after.domain.models.responses.UploadUserDataResponse
import com.luis2576.dev.rickandmorty.after.domain.models.UserInformation
import com.luis2576.dev.rickandmorty.after.domain.repositories.AuthRepository
import com.luis2576.dev.rickandmorty.after.ui.navigation.ChatsHomeScreen
import com.luis2576.dev.rickandmorty.after.domain.repositories.UserInformationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userInformationRepository: UserInformationRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var _fullName by mutableStateOf("")
    val fullName: String get() = _fullName

    private var _type by mutableStateOf("")

    private var _email by mutableStateOf("")
    val email: String get() = _email

    private var _password by mutableStateOf("")
    val password: String get() = _password

    private var _confirmPassword by mutableStateOf("")
    val confirmPassword: String get() = _confirmPassword

    private var _registrationStatus = mutableStateOf<RegistrationResponse<FirebaseUser>>(
        RegistrationResponse.UnRegistered)
    val registrationStatus: State<RegistrationResponse<FirebaseUser>> = _registrationStatus

    private var _registerButtonEnabled by mutableStateOf(true)
    val registerButtonEnabled: Boolean get() = _registerButtonEnabled

    fun onFullNameChange(newFullName: String) {
        _fullName = newFullName
        when(_registrationStatus.value){
            is RegistrationResponse.NameError -> {
                _registrationStatus.value = RegistrationResponse.UnRegistered
            }
            else -> {

            }
        }
    }

    fun onEmailChange(newEmail: String) {
        _email = newEmail
        when(_registrationStatus.value){
            is RegistrationResponse.EmailError -> {
                _registrationStatus.value = RegistrationResponse.UnRegistered
            }
            is RegistrationResponse.InvalidCredentialsError -> {
                _registrationStatus.value = RegistrationResponse.UnRegistered
            }
            else -> {

            }
        }
    }



    fun onTypeChange(newType: String) {
        when(newType){
            "Profesional" ->  _type = "professional"
            "Admin" ->  _type = "admin"
            "Supervisor" ->  _type = "supervisor"
        }
    }


    fun onPasswordChange(newPassword: String) {
        _password = newPassword
        when(_registrationStatus.value){
            is RegistrationResponse.PasswordError -> {
                _registrationStatus.value = RegistrationResponse.UnRegistered
            }
            is RegistrationResponse.WeakPasswordError -> {
                _registrationStatus.value = RegistrationResponse.UnRegistered
            }
            else -> {

            }
        }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword = newConfirmPassword
        when(_registrationStatus.value){
            is RegistrationResponse.ConfirmPasswordError -> {
                _registrationStatus.value = RegistrationResponse.UnRegistered
            }
            is RegistrationResponse.WeakPasswordError -> {
                _registrationStatus.value = RegistrationResponse.UnRegistered
            }
            else -> {

            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile("^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE)
        return pattern.matcher(email).matches()
    }

    private fun arePasswordsMatched(): Boolean {
        return _password == _confirmPassword
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    private var _navigateToScreen = mutableStateOf<Any?>(null)
    val navigateToScreen: State<Any?> get() = _navigateToScreen

    fun onRegisterClick() {
        _registrationStatus.value = RegistrationResponse.LoadingRegistration
        _registerButtonEnabled = false

        viewModelScope.launch {
            when {
                _fullName.isEmpty() -> {
                    _registrationStatus.value = RegistrationResponse.NameError(Exception(context.getString(R.string.error_name_empty)))
                    _registerButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        showSnackBar(message = context.getString(R.string.error_name_empty))
                    }
                }
                _email.isEmpty() -> {
                    _registrationStatus.value = RegistrationResponse.EmailError(Exception(context.getString(
                        R.string.error_email_empty)))
                    _registerButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        showSnackBar(message = context.getString(R.string.error_email_empty))
                    }
                }
                !isEmailValid(_email) -> {
                    _registrationStatus.value = RegistrationResponse.EmailError(Exception(context.getString(R.string.error_email_invalid)))
                    _registerButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        showSnackBar(message = context.getString(R.string.error_email_invalid))
                    }
                }
                _password.isEmpty() -> {
                    _registrationStatus.value = RegistrationResponse.PasswordError(Exception(context.getString(R.string.error_password_empty)))
                    _registerButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        showSnackBar(message = context.getString(R.string.error_password_empty))

                    }
                }
                !isPasswordValid(_password) -> {
                    _registrationStatus.value = RegistrationResponse.PasswordError(Exception(context.getString(R.string.error_password_short)))
                    _registerButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        showSnackBar(message = context.getString(R.string.error_password_short))
                    }
                }
                _confirmPassword.isEmpty() -> {
                    _registrationStatus.value = RegistrationResponse.ConfirmPasswordError(Exception(context.getString(R.string.error_confirm_password_empty)))
                    _registerButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        showSnackBar(message = context.getString(R.string.error_confirm_password_empty))
                    }
                }
                !isPasswordValid(_confirmPassword) -> {
                    _registrationStatus.value = RegistrationResponse.ConfirmPasswordError(Exception(context.getString(R.string.error_password_short)))
                    _registerButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        showSnackBar(message = context.getString(R.string.error_password_short))
                    }
                }
                !arePasswordsMatched() -> {
                    _registrationStatus.value = RegistrationResponse.ConfirmPasswordError(Exception(context.getString(R.string.error_passwords_not_matched)))
                    _registerButtonEnabled = true
                    withContext(Dispatchers.Main) {
                        showSnackBar(message = context.getString(R.string.error_passwords_not_matched))
                    }
                }
                else -> {
                    // Se presupone que el usuario va a ser de tipo Profesional por defecto
                    val userInformation = UserInformation(
                        uid = "",
                        name = _fullName,
                        email = _email,
                    )

                    // Registro de usuario en Firebase
                    val response = authRepository.registerUser(_email, _password)
                    _registrationStatus.value = response

                    if (response is RegistrationResponse.Registered && response.data != null) {
                        val uid = response.data.uid // Obtener el UID del usuario registrado
                        val updatedUserInformation = userInformation.copy(uid = uid)

                        // Guardar los datos del usuario en la base de datos usando el UserInformationRepository
                        val saveUserInfoResponse = userInformationRepository.uploadUserInformation(uid, updatedUserInformation)

                        if (saveUserInfoResponse is UploadUserDataResponse.Success) {
                            Log.d("RegisterViewModel", "Navigating to HomeScreen")
                            _navigateToScreen.value = ChatsHomeScreen
                        } else {
                            _registerButtonEnabled = true
                            withContext(Dispatchers.Main) {
                                showSnackBar(message = "Error cargando los datos del usuario")
                            }
                        }
                    } else if (response is RegistrationResponse.Failure) {
                        _registerButtonEnabled = true
                        withContext(Dispatchers.Main) {
                            showSnackBar(message = response.e.message ?: context.getString(R.string.error_unknown))
                        }
                    } else if (response is RegistrationResponse.EmailAlreadyInUseError) {
                        _registerButtonEnabled = true
                        withContext(Dispatchers.Main) {
                            showSnackBar(message = response.e.message ?: context.getString(R.string.error_unknown))
                        }
                    }
                }
            }
        }
    }


    private val _snackbarHostState = SnackbarHostState()
    val snackbarHostState: SnackbarHostState get() = _snackbarHostState
    private suspend fun showSnackBar(
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