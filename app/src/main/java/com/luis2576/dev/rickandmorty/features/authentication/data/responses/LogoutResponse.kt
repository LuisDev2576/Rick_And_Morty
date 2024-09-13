package com.luis2576.dev.rickandmorty.features.authentication.data.responses

sealed class LogoutResponse<out T> {
    data class Success<out T>(val data: T) : LogoutResponse<T>()
    data class Error(val exception: Exception) : LogoutResponse<Nothing>()
    data object LoadingLogout: LogoutResponse<Nothing>()
    data object Void: LogoutResponse<Nothing>()
}