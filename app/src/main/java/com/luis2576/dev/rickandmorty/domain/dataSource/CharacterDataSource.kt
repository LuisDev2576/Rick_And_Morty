package com.luis2576.dev.rickandmorty.domain.dataSource

import com.luis2576.dev.rickandmorty.data.local.CharacterEntity
import com.luis2576.dev.rickandmorty.data.local.CharacterEntityPreview

interface CharacterDataSource {
    suspend fun getAllCharactersPreview(): List<CharacterEntityPreview>
    suspend fun upsertCharacterList(characterList: List<CharacterEntity>)
    suspend fun getCharacterById(characterId: String): CharacterEntity?
}
