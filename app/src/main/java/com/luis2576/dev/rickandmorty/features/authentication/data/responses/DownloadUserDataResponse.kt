package com.luis2576.dev.rickandmorty.features.authentication.data.responses

sealed class DownloadUserDataResponse<out T> {

    data object Void: DownloadUserDataResponse<Nothing>()

    data object DownloadingUserData: DownloadUserDataResponse<Nothing>()

    data class UserInfoDownloaded<out T>(
        val data: T? 
    ): DownloadUserDataResponse<T>()


    data class Failure(
        val e: Exception
    ): DownloadUserDataResponse<Nothing>()


}