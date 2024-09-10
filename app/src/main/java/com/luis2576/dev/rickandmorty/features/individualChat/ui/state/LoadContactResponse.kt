package com.luis2576.dev.rickandmorty.features.individualChat.ui.state

import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Contact

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

