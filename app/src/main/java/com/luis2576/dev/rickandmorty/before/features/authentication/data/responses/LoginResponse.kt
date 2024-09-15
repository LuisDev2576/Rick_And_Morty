package com.luis2576.dev.rickandmorty.before.features.authentication.data.responses

sealed class LoginResponse<out T> {

    data object UnLogged: LoginResponse<Nothing>()

    data object LoadingLogin: LoginResponse<Nothing>()

    data class Logged<out T>(
        val data: T?
    ): LoginResponse<T>()

    data class EmailError(
        val e: Exception
    ): LoginResponse<Nothing>()

    data class PasswordError(
        val e: Exception
    ) : LoginResponse<Nothing>()

    data class TooManyRequestsError(val e: Exception): LoginResponse<Nothing>()

    data class UserNotFoundError(
        val e: Exception
    ) : LoginResponse<Nothing>()

    data class Failure(
        val e: Exception
    ): LoginResponse<Nothing>()


}