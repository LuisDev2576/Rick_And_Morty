package com.luis2576.dev.rickandmorty.util.userInformation

import com.luis2576.dev.rickandmorty.features.authentication.data.responses.DownloadUserDataResponse
import com.luis2576.dev.rickandmorty.features.authentication.data.responses.UploadUserDataResponse
import com.luis2576.dev.rickandmorty.features.authentication.domain.model.UserInformation
import kotlinx.coroutines.flow.Flow

interface UserInformationRepository {
    fun downloadUserInformation(uid: String): Flow<DownloadUserDataResponse<UserInformation>>
    suspend fun uploadUserInformation(uid: String, userInformation: UserInformation): UploadUserDataResponse

}
