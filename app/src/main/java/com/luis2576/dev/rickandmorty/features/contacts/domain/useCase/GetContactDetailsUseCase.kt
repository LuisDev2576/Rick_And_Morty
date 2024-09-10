package com.luis2576.dev.rickandmorty.features.contacts.domain.useCase

import com.luis2576.dev.rickandmorty.features.contacts.domain.model.Contact
import com.luis2576.dev.rickandmorty.features.contacts.domain.repository.ContactRepository
import com.luis2576.dev.rickandmorty.util.Resource
import javax.inject.Inject
// TODO Pasar esto a la feature de chat para cuando se le pase el parametro de contactId proveniente de contacts para añadir un nuevo chat
/**
 * Caso de uso para obtener los detalles de un contacto por su ID.
 *
 * @param repository Repositorio de contactos.
 */
class GetContactDetailsUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    /**
    * Obtiene los detalles de un contacto por su ID.
    *
    * @param id ID del contacto.
    * @return Contacto si se encuentra, null en caso contrario.
    */
    suspend operator fun invoke(id: String): Contact? {
        return when (val result = repository.getContactById(id)) {
            is Resource.Success -> result.data
            else -> null
        }
    }
}