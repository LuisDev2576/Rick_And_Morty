package com.luis2576.dev.rickandmorty.data.repository

import android.content.Context
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.data.mappers.toCharacterEntity
import com.luis2576.dev.rickandmorty.data.mappers.toMyCharacter
import com.luis2576.dev.rickandmorty.data.mappers.toMyCharacterPreview
import com.luis2576.dev.rickandmorty.data.remote.RickAndMortyApi
import com.luis2576.dev.rickandmorty.domain.dataSource.CharacterDataSource
import com.luis2576.dev.rickandmorty.domain.model.MyCharacter
import com.luis2576.dev.rickandmorty.domain.model.MyCharacterPreview
import com.luis2576.dev.rickandmorty.domain.repository.CharacterRepository
import com.luis2576.dev.rickandmorty.util.NetworkUtil
import com.luis2576.dev.rickandmorty.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CharacterRepositoryImpl
@Inject
constructor(
    private val charactersApi: RickAndMortyApi,
    private val characterDataSource: CharacterDataSource,
    private val networkUtil: NetworkUtil,
    @ApplicationContext private val context: Context
) : CharacterRepository {

    override suspend fun getAllCharactersPreviews(
        forceFetchFromRemote: Boolean
    ): Resource<List<MyCharacterPreview>> {
        return try {
            val localCharacters = getLocalCharacters()
            if (localCharacters is Resource.Success && localCharacters.data?.isNotEmpty() == true &&
                !forceFetchFromRemote
            ) {
                return Resource.Success(localCharacters.data)
            }

            if (!networkUtil.isInternetAvailable()) {
                return if (localCharacters is Resource.Success &&
                    localCharacters.data?.isEmpty() == true
                ) {
                    Resource.Error(context.getString(R.string.no_internet_no_data))
                } else {
                    localCharacters
                }
            }

            fetchRemoteCharacters()
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.error_loading_countries))
        }
    }

    override suspend fun getLocalCharacters(): Resource<List<MyCharacterPreview>> {
        return try {
            val localCharacters = characterDataSource.getAllCharactersPreview().map { it.toMyCharacterPreview() }
            Resource.Success(localCharacters)
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.error_loading_countries))
        }
    }

    override suspend fun fetchRemoteCharacters(): Resource<List<MyCharacterPreview>> {
        return try {
            val characterListFromApi = charactersApi.getAllCharacters(1)
            val charactersEntity = characterListFromApi.results.map { it.toCharacterEntity() }
            characterDataSource.upsertCharacterList(charactersEntity)
            Resource.Success(data = charactersEntity.map { it.toMyCharacterPreview() })
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.error_loading_countries))
        }
    }

    override suspend fun getCharacterById(id: String): Resource<MyCharacter> {
        return try {
            val characterEntity = characterDataSource.getCharacterById(id)
            if (characterEntity != null) {
                Resource.Success(characterEntity.toMyCharacter())
            } else {
                Resource.Error(context.getString(R.string.country_not_found))
            }
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.failed_to_load_country_details))
        }
    }

}
