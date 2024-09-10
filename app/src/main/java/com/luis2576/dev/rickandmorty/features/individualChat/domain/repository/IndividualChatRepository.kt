package com.luis2576.dev.rickandmorty.features.individualChat.domain.repository

import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Contact
import com.luis2576.dev.rickandmorty.util.Resource

//TODO Ajustar doc
interface IndividualChatRepository {

    /**
     * Obtiene los detalles de un contacto por su ID
     *
     * @param id el ID del contacto a buscar
     * @return Un objeto `Resource` que contiene el contacto o un error en caso de fallo
     */
    suspend fun getContactById(id: String): Resource<Contact>

}