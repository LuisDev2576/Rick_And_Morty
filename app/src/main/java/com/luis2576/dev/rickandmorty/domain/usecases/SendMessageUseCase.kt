package com.luis2576.dev.rickandmorty.domain.usecases

import com.luis2576.dev.rickandmorty.domain.models.Contact
import com.luis2576.dev.rickandmorty.domain.models.Conversation
import com.luis2576.dev.rickandmorty.domain.repositories.IndividualChatRepository
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
