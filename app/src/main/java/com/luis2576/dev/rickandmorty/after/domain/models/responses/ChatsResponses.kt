package com.luis2576.dev.rickandmorty.after.domain.models.responses

import com.luis2576.dev.rickandmorty.after.domain.models.Contact
import com.luis2576.dev.rickandmorty.after.domain.models.ContactPreview
import com.luis2576.dev.rickandmorty.after.domain.models.Conversation
import com.luis2576.dev.rickandmorty.after.domain.models.Message

/**
 * Representa los diferentes estados posibles de la lista de contactos.
 */
sealed class ContactListState {
    /**
     * Estado que indica que la lista de contactos se está cargando.
     */
    data object Loading : ContactListState()

    /**
     * Estado que indica que la lista de contactos se cargó con éxito.
     *
     * @param contacts La lista de contactos obtenidos.
     */
    data class Success(val contacts: List<ContactPreview>) : ContactListState()

    /**
     * Estado que indica que ocurrió un error al cargar la lista de contactos
     *
     * @param message El mensaje de error.
     */
    data class Error(val message: String) : ContactListState()
}


sealed class DownloadConversationResponse {

    // Estado vacío inicial
    object Void : DownloadConversationResponse()

    // Estado cuando los mensajes se están descargando
    object DownloadingConversation : DownloadConversationResponse()

    // Estado cuando los mensajes han sido descargados correctamente
    data class MessagesDownloaded(val conversation: Conversation) : DownloadConversationResponse()

    // Estado cuando ocurre un error durante la descarga
    data class Failure(
        val e: Exception
    ) : DownloadConversationResponse()
}


sealed class LoadContactResponse {

    // Estado vacío inicial
    object Void : LoadContactResponse()

    // Estado cuando el contacto se está cargando
    object LoadingContact : LoadContactResponse()

    // Estado cuando el contacto ha sido cargado correctamente
    data class ContactLoaded(val contact: Contact?) : LoadContactResponse()

    // Estado cuando ocurre un error durante la carga
    data class Failure(val message: String) : LoadContactResponse()
}


sealed class SendMessageResult {

    object Void : SendMessageResult()

    object SendingMessage : SendMessageResult()

    data class MessageSent(val message: Message) : SendMessageResult()

    data class Failure(val e: Exception) : SendMessageResult()
}