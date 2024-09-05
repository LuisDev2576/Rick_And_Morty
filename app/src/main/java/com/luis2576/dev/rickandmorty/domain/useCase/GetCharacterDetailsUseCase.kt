package com.luis2576.dev.rickandmorty.domain.useCase

import com.luis2576.dev.rickandmorty.domain.model.MyCharacter
import com.luis2576.dev.rickandmorty.domain.repository.CharacterRepository
import com.luis2576.dev.rickandmorty.util.Resource
import javax.inject.Inject

class GetCharacterDetailsUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: String): MyCharacter? {
        return when (val result = repository.getCharacterById(id)) {
            is Resource.Success -> result.data
            else -> null
        }
    }
}
