package com.luis2576.dev.rickandmorty.after.domain.repositories

import com.luis2576.dev.rickandmorty.after.domain.models.responses.DownloadUserDataResponse
import com.luis2576.dev.rickandmorty.after.domain.models.responses.UploadUserDataResponse
import com.luis2576.dev.rickandmorty.after.domain.models.UserInformation
import kotlinx.coroutines.flow.Flow

interface UserInformationRepository {
    fun downloadUserInformation(uid: String): Flow<DownloadUserDataResponse<UserInformation>>
    suspend fun uploadUserInformation(uid: String, userInformation: UserInformation): UploadUserDataResponse

}
