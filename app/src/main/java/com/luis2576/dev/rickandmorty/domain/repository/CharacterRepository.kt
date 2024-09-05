package com.luis2576.dev.rickandmorty.domain.repository

import com.luis2576.dev.rickandmorty.domain.model.MyCharacter
import com.luis2576.dev.rickandmorty.domain.model.MyCharacterPreview
import com.luis2576.dev.rickandmorty.util.Resource

interface CharacterRepository {
    suspend fun getAllCharactersPreviews(forceFetchFromRemote: Boolean): Resource<List<MyCharacterPreview>>
    suspend fun getCharacterById(id: String): Resource<MyCharacter>
    suspend fun getLocalCharacters(): Resource<List<MyCharacterPreview>>
    suspend fun fetchRemoteCharacters(): Resource<List<MyCharacterPreview>>
}
