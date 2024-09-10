package com.luis2576.dev.rickandmorty.features.individualChat.data.dataSource

import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactEntity
import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactsDao
import com.luis2576.dev.rickandmorty.features.individualChat.domain.dataSource.IndividualChatDataSource
import javax.inject.Inject

//TODO Ajustar doc
class IndividualChatDataSourceImpl @Inject constructor(
    private val contactDao: ContactsDao
) : IndividualChatDataSource {

    /**
     * Obtiene un contacto por su ID desde la base de datos
     *
     * @param characterId ID del contacto a buscar
     * @return Entidad del contacto si se encuentra, null en caso contrario
     */
    override suspend fun getContactById(contactId: String): ContactEntity {
        return contactDao.getContactById(contactId.toInt())
    }
}