package com.luis2576.dev.rickandmorty.before.features.authentication.data.responses

sealed class UploadUserDataResponse {

    data object Success : UploadUserDataResponse()

    data class Failure(
        val e: Exception
    ) : UploadUserDataResponse()
}
