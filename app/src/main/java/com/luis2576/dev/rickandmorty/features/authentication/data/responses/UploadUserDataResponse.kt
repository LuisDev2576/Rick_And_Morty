package com.luis2576.dev.rickandmorty.features.authentication.data.responses

sealed class UploadUserDataResponse {

    data object Success : UploadUserDataResponse()

    data class Failure(
        val e: Exception
    ) : UploadUserDataResponse()
}
