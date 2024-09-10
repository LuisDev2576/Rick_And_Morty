package com.luis2576.dev.rickandmorty.features.contacts.domain.repository

import com.luis2576.dev.rickandmorty.features.contacts.domain.model.ContactPreview
import com.luis2576.dev.rickandmorty.util.Resource

/**
 * Interfaz que define las operaciones para acceder a los contactos, tanto locales como remotos
 */
interface ContactRepository {
    /**
     * Obtiene una lista de vistas previas de contactos.
     * Si `forceFetchFromRemote` es verdadero, se fuerza la obtención de datos desde el servidor remoto,
     * incluso si hay datos locales disponibles.
     *
     * @param forceFetchFromRemote Indica si se debe forzar la obtención de datos desde el servidor remoto
     * @return Un objeto `Resource` que contiene la lista de contactos o un error en caso de fallo
     */
    suspend fun getAllContactsPreviews(forceFetchFromRemote: Boolean): Resource<List<ContactPreview>>

    /**
     * Obtiene una lista de vistas previas de contactos desde la base de datos local
     *
     * @return Un objeto `Resource` que contiene la lista de contactos o un error en caso de fallo
     */
    suspend fun getLocalContacts(): Resource<List<ContactPreview>>

    /**
     * Obtiene una lista de vistas previas de contactos desde el servidor remoto
     *
     * @return Un objeto `Resource` que contiene la lista de contactos o un error en caso de fallo
     */
    suspend fun fetchRemoteContacts(): Resource<List<ContactPreview>>
}