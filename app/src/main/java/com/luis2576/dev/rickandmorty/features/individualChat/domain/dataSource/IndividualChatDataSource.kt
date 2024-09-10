package com.luis2576.dev.rickandmorty.features.individualChat.domain.dataSource

import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactEntity
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Contact
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Conversation
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.DownloadConversationResponse
import kotlinx.coroutines.flow.Flow

//TODO Ajustar doc
interface IndividualChatDataSource {

    /**
     * Obtiene un contacto por su ID desde la fuente de datos.
     *
     * @param contactId ID del contacto a buscar.
     * @return Entidad del contacto si se encuentra, null en caso contrario.
     */
    suspend fun getContactById(contactId: String): ContactEntity

    /**
     * Descarga los mensajes de un chat.
     *
     * @param userId ID del usuario.
     * @param contactId ID del contacto.
     * @return Flow que emite la respuesta de descarga de la conversación.
     */
    fun downloadMessages(userId: String, contactId: String): Flow<DownloadConversationResponse>

    /**
     * Envía un mensaje al chat.
     *
     * @param userId ID del usuario.
     * @param contact ID del contacto.
     * @param conversation Conversación actualizada con mensaje a enviar.
     */
    suspend fun sendMessage(userId: String, contact: Contact, conversation: Conversation)

}
