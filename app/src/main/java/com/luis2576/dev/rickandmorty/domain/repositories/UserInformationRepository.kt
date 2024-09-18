package com.luis2576.dev.rickandmorty.domain.repositories

import com.luis2576.dev.rickandmorty.domain.models.responses.DownloadUserDataResponse
import com.luis2576.dev.rickandmorty.domain.models.responses.UploadUserDataResponse
import com.luis2576.dev.rickandmorty.domain.models.UserInformation
import kotlinx.coroutines.flow.Flow

interface UserInformationRepository {
    fun downloadUserInformation(uid: String): Flow<DownloadUserDataResponse<UserInformation>>
    suspend fun uploadUserInformation(uid: String, userInformation: UserInformation): UploadUserDataResponse

}
