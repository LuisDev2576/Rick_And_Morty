package com.luis2576.dev.rickandmorty.features.contacts.data.repository

import android.content.Context
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.features.contacts.data.remote.RickAndMortyApi
import com.luis2576.dev.rickandmorty.features.contacts.domain.dataSource.ContactDataSource
import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactEntity
import com.luis2576.dev.rickandmorty.features.contacts.data.mappers.toContact
import com.luis2576.dev.rickandmorty.features.contacts.data.mappers.toContactEntity
import com.luis2576.dev.rickandmorty.features.contacts.data.mappers.toContactPreview
import com.luis2576.dev.rickandmorty.features.contacts.domain.model.Contact
import com.luis2576.dev.rickandmorty.features.contacts.domain.model.ContactPreview
import com.luis2576.dev.rickandmorty.features.contacts.domain.repository.ContactRepository
import com.luis2576.dev.rickandmorty.util.NetworkUtil
import com.luis2576.dev.rickandmorty.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
/**
 * Implementación del repositorio de contactos que interactúa con la API de Rick and Morty y una fuente de datos local
 *
 * @param rickAndMortyApi La interfaz para hacer peticiones a la API de Rick and Morty
 * @param contactDataSource La fuente de datos local para almacenar y recuperar contactos
 * @param networkUtil Utilidad para verificar la conectividad de red
 * @param context El contexto de la aplicación
 */
class ContactRepositoryImpl
@Inject
constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val contactDataSource: ContactDataSource,
    private val networkUtil: NetworkUtil,
    @ApplicationContext private val context: Context
) : ContactRepository {

    /**
     * Obtiene una lista de vistas previas de contactos.
     * Si `forceFetchFromRemote` es verdadero, se fuerza la obtención de datos desde el servidor remoto,
     * incluso si hay datos locales disponibles. Si no hay conexión a internet y no hay datos locales
     * disponibles, se devuelve un error.
     *
     * @param forceFetchFromRemote Indica si se debe forzar la obtención de datos desde el servidor remoto
     * @return Un objeto `Resource` que contiene la lista de contactos o un error en caso de fallo
     */
    override suspend fun getAllContactsPreviews(
        forceFetchFromRemote: Boolean
    ): Resource<List<ContactPreview>> {
        return try {
            val localContacts = getLocalContacts()

            // Si tenemos datos locales, retornamos si no se fuerza la recarga remota
            localContacts.takeIf { it is Resource.Success && it.data?.isNotEmpty() == true && !forceFetchFromRemote }?.let { return it }

            // Si no hay internet, retornamos error (ya sabemos que no hay datos locales útiles)
            if (!networkUtil.isInternetAvailable()) {
                return Resource.Error(context.getString(R.string.no_internet_no_data))
            }

            // Intentar cargar desde remoto
            fetchRemoteContacts()
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.error_loading_contacts))
        }
    }

    /**
     * Obtiene una lista de vistas previas de contactos desde la base de datos local
     *
     * @return Un objeto `Resource` que contiene la lista de contactos o un error en caso de fallo
     */
    override suspend fun getLocalContacts(): Resource<List<ContactPreview>> {
        return try {
            val localCharacters = contactDataSource.getAllContactPreview().map { it.toContactPreview() }
            Resource.Success(localCharacters)
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.error_loading_contacts))
        }
    }

    /**
     * Obtiene una lista de vistas previas de contactos desde el servidor remoto y los almacena en la base de datos local.
     * Itera sobre las páginas de resultados hasta que no haya más páginas disponibles
     *
     * @return Un objeto `Resource` que contiene la lista de contactos o un error en caso de fallo
     */
    override suspend fun fetchRemoteContacts(): Resource<List<ContactPreview>> {
        return try {
            var currentPage = 1
            val allContactsEntity = mutableListOf<ContactEntity>()

            do {
                val contactListFromApi = rickAndMortyApi.getAllContacts(currentPage)
                val contactsEntity = contactListFromApi.results.map { it.toContactEntity() }
                allContactsEntity.addAll(contactsEntity)

                currentPage++
            } while (contactListFromApi.info.next != null)

            contactDataSource.upsertContactList(allContactsEntity)
            Resource.Success(data = allContactsEntity.map { it.toContactPreview() })
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.error_loading_contacts))
        }
    }

    // TODO Sacar de este repositorio y ponerlo en el de chat para cuando se vaya a crear un nuevo chat, se obtenga la información del personaje o contacto
    /**
     * Obtiene los detalles de un contacto por su ID desde la base de datos local
     *
     * @param id el ID del contacto a buscar
     * @return Un objeto `Resource` que contiene el contacto o un error en caso de fallo
     */
    override suspend fun getContactById(id: String): Resource<Contact> {
        return try {
            val contactEntity = contactDataSource.getContactById(id)
            if (contactEntity != null) {
                Resource.Success(contactEntity.toContact())
            } else {
                Resource.Error(context.getString(R.string.contact_not_found))
            }
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.failed_to_load_contact_details))
        }
    }

}
