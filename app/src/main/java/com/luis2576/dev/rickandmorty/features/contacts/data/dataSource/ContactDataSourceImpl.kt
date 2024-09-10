package com.luis2576.dev.rickandmorty.features.contacts.data.dataSource

import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactEntity
import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactEntityPreview
import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactsDao
import com.luis2576.dev.rickandmorty.features.contacts.domain.dataSource.ContactDataSource
import javax.inject.Inject

/**
 * Implementación de ContactDataSource que utiliza un ContactsDao para interactuar con la base de datos.
 *
 * @param contactDao El objeto DAO para acceder a la base de datos de contactos
 */
class ContactDataSourceImpl @Inject constructor(
    private val contactDao: ContactsDao
) : ContactDataSource {

    /**
     * Obtiene una lista de vistas previas de todos los contactos almacenados en la base de datos.
     *
     * @return Lista de entidades de vista previa de contactos.
     */
    override suspend fun getAllContactPreview(): List<ContactEntityPreview> {
        return contactDao.getAllContactsPreview()
    }

    /**
     * Inserta o actualiza una lista de contactos en la base de datos
     *
     * @param characterList Lista de entidades de contactos a insertar o actualizar
     */
    override suspend fun upsertContactList(contactList: List<ContactEntity>) {
        contactDao.upsertContactList(contactList)
    }
}