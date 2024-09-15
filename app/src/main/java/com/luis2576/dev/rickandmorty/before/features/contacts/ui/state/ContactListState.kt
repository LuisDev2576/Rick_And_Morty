package com.luis2576.dev.rickandmorty.before.features.contacts.ui.state

import com.luis2576.dev.rickandmorty.before.features.contacts.domain.model.ContactPreview

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