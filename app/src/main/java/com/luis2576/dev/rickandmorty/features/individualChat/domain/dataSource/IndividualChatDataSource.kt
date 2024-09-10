package com.luis2576.dev.rickandmorty.features.individualChat.domain.dataSource

import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactEntity

//TODO Ajustar doc
interface IndividualChatDataSource {

    /**
     * Obtiene un contacto por su ID desde la fuente de datos.
     *
     * @param contactId ID del contacto a buscar
     * @return Entidad del contacto si se encuentra, null en caso contrario
     */
    suspend fun getContactById(contactId: String): ContactEntity
}