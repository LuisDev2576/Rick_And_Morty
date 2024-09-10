package com.luis2576.dev.rickandmorty.features.contacts.data.dataSource

import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactEntity
import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactEntityPreview
import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactsDao
import com.luis2576.dev.rickandmorty.features.contacts.domain.dataSource.ContactDataSource
import javax.inject.Inject

/**
 * Implementación de ContactDataSource que utiliza un ContactsDao para interactuar con la base de datos.
 *
 * @param characterDao El objeto DAO para acceder a la base de datos de contactos
 */
class ContactDataSourceImpl @Inject constructor(
    private val characterDao: ContactsDao
) : ContactDataSource {

    /**
     * Obtiene una lista de vistas previas de todos los contactos almacenados en la base de datos.
     *
     * @return Lista de entidades de vista previa de contactos.
     */
    override suspend fun getAllContactPreview(): List<ContactEntityPreview> {
        return characterDao.getAllCharactersPreview()
    }

    /**
     * Inserta o actualiza una lista de contactos en la base de datos
     *
     * @param characterList Lista de entidades de contactos a insertar o actualizar
     */
    override suspend fun upsertContactList(characterList: List<ContactEntity>) {
        characterDao.upsertCharacterList(characterList)
    }

    /**
     * Obtiene un contacto por su ID desde la base de datos
     *
     * @param characterId ID del contacto a buscar
     * @return Entidad del contacto si se encuentra, null en caso contrario
     */
    override suspend fun getContactById(characterId: String): ContactEntity? {
        return characterDao.getCharacterById(characterId.toInt())
    }
}