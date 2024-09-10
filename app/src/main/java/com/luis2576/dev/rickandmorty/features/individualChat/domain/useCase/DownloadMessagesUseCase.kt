package com.luis2576.dev.rickandmorty.features.individualChat.domain.useCase

import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Conversation
import com.luis2576.dev.rickandmorty.features.individualChat.domain.repository.IndividualChatRepository
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.DownloadConversationResponse
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
