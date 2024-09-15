package com.luis2576.dev.rickandmorty.before.features.individualChat.ui.state

import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.model.Conversation

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
