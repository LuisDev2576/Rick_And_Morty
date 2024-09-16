package com.luis2576.dev.rickandmorty.after.domain.models.responses

sealed class LoginResponse<out T> {
    data object UnLogged: LoginResponse<Nothing>()
    data object LoadingLogin: LoginResponse<Nothing>()
    data class Logged<out T>(val data: T?): LoginResponse<T>()
    data class EmailError(val e: Exception): LoginResponse<Nothing>()
    data class PasswordError(val e: Exception) : LoginResponse<Nothing>()
    data class TooManyRequestsError(val e: Exception): LoginResponse<Nothing>()
    data class UserNotFoundError(val e: Exception) : LoginResponse<Nothing>()
    data class Failure(val e: Exception): LoginResponse<Nothing>()
}

sealed class DownloadUserDataResponse<out T> {
    data object Void: DownloadUserDataResponse<Nothing>()
    data object DownloadingUserData: DownloadUserDataResponse<Nothing>()
    data class UserInfoDownloaded<out T>(val data: T?): DownloadUserDataResponse<T>()
    data class Failure(val e: Exception): DownloadUserDataResponse<Nothing>()
}

sealed class LogoutResponse<out T> {
    data class Success<out T>(val data: T) : LogoutResponse<T>()
    data class Error(val exception: Exception) : LogoutResponse<Nothing>()
    data object LoadingLogout: LogoutResponse<Nothing>()
    data object Void: LogoutResponse<Nothing>()
}

sealed class RegistrationResponse<out T> {
    data object UnRegistered : RegistrationResponse<Nothing>()
    data object LoadingRegistration : RegistrationResponse<Nothing>()
    data class Registered<out T>(val data: T?) : RegistrationResponse<T>()
    data class EmailAlreadyInUseError(val e: Exception) : RegistrationResponse<Nothing>()
    data class Failure(val e: Exception) : RegistrationResponse<Nothing>()
    data class NameError(val e: Exception) : RegistrationResponse<Nothing>()
    data class EmailError(val e: Exception): RegistrationResponse<Nothing>()
    data class PasswordError(val e: Exception): RegistrationResponse<Nothing>()
    data class ConfirmPasswordError(val e: Exception): RegistrationResponse<Nothing>()
    data class WeakPasswordError(val e: Exception) : RegistrationResponse<Nothing>()
    data class InvalidCredentialsError(val e: Exception) : RegistrationResponse<Nothing>()
}

sealed class ResetPasswordResponse<out T> {
    data object Void: ResetPasswordResponse<Nothing>()
    data object SendingEmailPassworReset: ResetPasswordResponse<Nothing>()
    data class PasswordResetEmailSent<out T>(val data: T?): ResetPasswordResponse<T>()
    data class EmailError(val e: Exception): ResetPasswordResponse<Nothing>()
    data class UserNotFoundError(val e: Exception) : ResetPasswordResponse<Nothing>()
    data class Failure(val e: Exception): ResetPasswordResponse<Nothing>()
}

sealed class UploadUserDataResponse {
    data object Success : UploadUserDataResponse()
    data class Failure(val e: Exception) : UploadUserDataResponse()
}
