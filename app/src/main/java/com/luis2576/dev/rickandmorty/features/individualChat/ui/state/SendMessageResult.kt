package com.luis2576.dev.rickandmorty.features.individualChat.ui.state

import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Message

sealed class SendMessageResult {

    object Void : SendMessageResult()

    object SendingMessage : SendMessageResult()

    data class MessageSent(val message: Message) : SendMessageResult()

    data class Failure(val e: Exception) : SendMessageResult()
}