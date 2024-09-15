package com.luis2576.dev.rickandmorty.before.features.contacts.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz Retrofit que define los endpoints de la API de Rick and Morty.
 */
interface RickAndMortyApi {

    /**
     * Obtiene una lista paginada de personajes.
     *
     * @param page Número de página a obtener (por defecto 1).
     * @return Respuesta de la API que contiene información de paginación y la lista de personajes.
     */
    @GET("character")
    suspend fun getAllContacts(
        @Query("page") page: Int = 1
    ): RickAndMortyApiResponse

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}
