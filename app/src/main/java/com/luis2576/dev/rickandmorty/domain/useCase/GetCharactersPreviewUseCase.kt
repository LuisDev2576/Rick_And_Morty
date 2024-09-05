package com.luis2576.dev.rickandmorty.domain.useCase

import com.luis2576.dev.rickandmorty.domain.model.MyCharacterPreview
import com.luis2576.dev.rickandmorty.domain.repository.CharacterRepository
import com.luis2576.dev.rickandmorty.util.Resource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCharactersPreviewUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(forceFetchFromRemote: Boolean): Flow<Resource<List<MyCharacterPreview>>> = flow {
        emit(Resource.Loading())
        val result = repository.getAllCharactersPreviews(forceFetchFromRemote)
        emit(result)
    }.flowOn(Dispatchers.IO)
}
