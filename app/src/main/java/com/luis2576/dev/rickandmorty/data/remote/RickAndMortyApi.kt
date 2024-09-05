package com.luis2576.dev.rickandmorty.data.remote

import com.luis2576.dev.rickandmorty.data.remote.response.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getAllCharacters(
        @Query("page") page: Int = 1
    ): CharacterResponse

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}
