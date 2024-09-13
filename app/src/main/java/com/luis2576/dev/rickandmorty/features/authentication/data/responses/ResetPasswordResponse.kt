package com.luis2576.dev.rickandmorty.features.authentication.data.responses

sealed class ResetPasswordResponse<out T> {


    data object Void: ResetPasswordResponse<Nothing>()

    data object SendingEmailPassworReset: ResetPasswordResponse<Nothing>()

    data class PasswordResetEmailSent<out T>(
        val data: T?
    ): ResetPasswordResponse<T>()

    data class EmailError(
        val e: Exception
    ): ResetPasswordResponse<Nothing>()

    data class UserNotFoundError(
        val e: Exception
    ) : ResetPasswordResponse<Nothing>()

    data class Failure(
        val e: Exception
    ): ResetPasswordResponse<Nothing>()


}