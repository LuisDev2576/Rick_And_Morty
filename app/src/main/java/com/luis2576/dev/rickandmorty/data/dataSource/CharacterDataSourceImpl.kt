package com.luis2576.dev.rickandmorty.data.dataSource

import com.luis2576.dev.rickandmorty.data.local.CharacterEntity
import com.luis2576.dev.rickandmorty.data.local.CharacterEntityPreview
import com.luis2576.dev.rickandmorty.data.local.CharactersDao
import com.luis2576.dev.rickandmorty.domain.dataSource.CharacterDataSource
import javax.inject.Inject

class CharacterDataSourceImpl @Inject constructor(
    private val characterDao: CharactersDao
) : CharacterDataSource {

    override suspend fun getAllCharactersPreview(): List<CharacterEntityPreview> {
        return characterDao.getAllCharactersPreview()
    }

    override suspend fun upsertCharacterList(characterList: List<CharacterEntity>) {
        characterDao.upsertCharacterList(characterList)
    }

    override suspend fun getCharacterById(characterId: String): CharacterEntity? {
        return characterDao.getCharacterById(characterId.toInt())
    }
}
