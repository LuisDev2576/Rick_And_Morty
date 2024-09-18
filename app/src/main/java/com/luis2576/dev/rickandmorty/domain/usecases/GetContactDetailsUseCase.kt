package com.luis2576.dev.rickandmorty.domain.usecases

import com.luis2576.dev.rickandmorty.domain.models.Contact
import com.luis2576.dev.rickandmorty.domain.repositories.IndividualChatRepository
import com.luis2576.dev.rickandmorty.util.Resource
import javax.inject.Inject

//TODO Ajustar doc
class GetContactDetailsUseCase @Inject constructor(
    private val repository: IndividualChatRepository
) {
    /**
    * Obtiene los detalles de un contacto por su ID.
    *
    * @param id ID del contacto.
    * @return Contacto si se encuentra, null en caso contrario.
    */
    suspend operator fun invoke(id: String): Contact? {
        return when (val result = repository.getContactById(id)) {
            is Resource.Success -> {
                result.data
            }
            is Resource.Error -> {
                result.data
            }
            is Resource.Loading -> {
                result.data
            }
        }
    }
}