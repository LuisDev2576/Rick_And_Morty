package com.luis2576.dev.rickandmorty.before.features.contacts.domain.dataSource

import com.luis2576.dev.rickandmorty.before.features.contacts.data.local.ContactEntity
import com.luis2576.dev.rickandmorty.before.features.contacts.data.local.ContactEntityPreview

/**
 * Interfaz que define las operaciones para interactuar con la fuente de datos de contactos.
 */
interface ContactDataSource {
    /**
     * Obtiene una lista de vistas previas de todos los contactos almacenados en la fuente de datos.
     *
     * @return Lista de entidades de vista previa de contactos.
     */
    suspend fun getAllContactPreview(): List<ContactEntityPreview>

    /**
     * Inserta o actualiza una lista de contactos en la fuente de datos.
     *
     * @param contactList Lista de entidades de contactos a insertar o actualizar
     */
    suspend fun upsertContactList(contactList: List<ContactEntity>)
}