package com.luis2576.dev.rickandmorty.before.features.contacts.domain.useCase

import com.luis2576.dev.rickandmorty.before.features.contacts.domain.model.ContactPreview
import com.luis2576.dev.rickandmorty.before.features.contacts.domain.repository.ContactRepository
import com.luis2576.dev.rickandmorty.before.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Caso de uso para obtener una vista previa de todos los contactos.
 *
 * @param repository Repositorio de contactos.
 */
class GetAllContactsPreviewUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    /**
     * Obtiene un flujo de recursos que representa la lista de contactos.
     *
     * @param forceFetchFromRemote Indica si se debe forzar la obtención de datos desde el servidor remoto.
     * @return Flujo de recursos que contiene la lista de contactos o un error.
     */
    operator fun invoke(forceFetchFromRemote: Boolean): Flow<Resource<List<ContactPreview>>> = flow {
        emit(Resource.Loading())
        val result = repository.getAllContactsPreviews(forceFetchFromRemote)
        emit(result)
    }.flowOn(Dispatchers.IO)
}
