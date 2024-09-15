package com.luis2576.dev.rickandmorty.before.features.authentication.data.responses

sealed class RegistrationResponse<out T> {

    data object UnRegistered : RegistrationResponse<Nothing>()

    data object LoadingRegistration : RegistrationResponse<Nothing>()

    data class Registered<out T>(
        val data: T?
    ) : RegistrationResponse<T>()

    data class EmailAlreadyInUseError(
        val e: Exception
    ) : RegistrationResponse<Nothing>()

    data class Failure(
        val e: Exception
    ) : RegistrationResponse<Nothing>()

    data class NameError(
        val e: Exception
    ) : RegistrationResponse<Nothing>()

    data class PhoneError(
        val e: Exception
    ) : RegistrationResponse<Nothing>()

    data class EmailError(
        val e: Exception
    ): RegistrationResponse<Nothing>()

    data class PasswordError(
        val e: Exception
    ): RegistrationResponse<Nothing>()

    data class ConfirmPasswordError(
        val e: Exception
    ): RegistrationResponse<Nothing>()

    data class WeakPasswordError(
        val e: Exception
    ) : RegistrationResponse<Nothing>()

    data class InvalidCredentialsError(
        val e: Exception
    ) : RegistrationResponse<Nothing>()

}
