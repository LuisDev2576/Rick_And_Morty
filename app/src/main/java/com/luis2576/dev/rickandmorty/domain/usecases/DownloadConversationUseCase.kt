package com.luis2576.dev.rickandmorty.domain.usecases

import com.luis2576.dev.rickandmorty.domain.models.responses.DownloadConversationResponse
import com.luis2576.dev.rickandmorty.domain.repositories.IndividualChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadConversationUseCase @Inject constructor(
    private val repository: IndividualChatRepository
) {
    /**
     * Descarga los conversación de un contacto.
     *
     * @param userId ID del usuario.
     * @return Flow con la respuesta de descarga de la conversación.
     */
    fun invoke(contactId: String, userId: String): Flow<DownloadConversationResponse> {
        return repository.downloadMessages(contactId, userId)
    }
}
