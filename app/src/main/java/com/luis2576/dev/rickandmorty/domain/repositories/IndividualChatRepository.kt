package com.luis2576.dev.rickandmorty.domain.repositories

import com.luis2576.dev.rickandmorty.domain.models.Contact
import com.luis2576.dev.rickandmorty.domain.models.Conversation
import com.luis2576.dev.rickandmorty.domain.models.responses.DownloadConversationResponse
import com.luis2576.dev.rickandmorty.util.Resource
import kotlinx.coroutines.flow.Flow

//TODO Ajustar doc
interface IndividualChatRepository {

    /**
     * Obtiene los detalles de un contacto por su ID
     *
     * @param id el ID del contacto a buscar
     * @return Un objeto `Resource` que contiene el contacto o un error en caso de fallo
     */
    suspend fun getContactById(id: String): Resource<Contact>

    /**
     * Descarga los mensajes de un chat.
     *
     * @param userId ID del usuario.
     * @param contact ID del chat.
     * @return Un Flow que emite la respuesta de descarga de mensajes.
     */
    fun downloadMessages(contact: String, userId: String): Flow<DownloadConversationResponse>

    /**
     * Envía un mensaje a un conversation.
     *
     * @param userId ID del usuario.
     * @param contact ID del contacto.
     * @param conversation Conversación actualizada con mensaje a enviar.
     * @return Resultado del envío del mensaje.
     */
    suspend fun sendMessage(userId: String, contact: Contact, conversation: Conversation): Result<Unit>

}