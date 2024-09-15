package com.luis2576.dev.rickandmorty.before.features.individualChat.domain.useCase

import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.model.Contact
import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.model.Conversation
import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.repository.IndividualChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: IndividualChatRepository
) {
    /**
     * Envía un mensaje a un chat.
     *
     * @param userId ID del usuario.
     * @param contactId ID del contacto.
     * @param conversation Conversación actualizada con mensaje a enviar.
     * TODO Actualizar
     * @return Resultado del envío del mensaje.
     */
    suspend fun invoke(userId: String, contact: Contact, conversation: Conversation): Result<Unit> {
        return repository.sendMessage(userId, contact, conversation)
    }
}
