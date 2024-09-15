package com.luis2576.dev.rickandmorty.before.features.contacts.data.remote

/**
 * Clase que representa la respuesta de la API de Rick and Morty.
 *
 * @param info Información de paginación.
 * @param results Lista de personajes.
 */
data class RickAndMortyApiResponse(
    val info: Info,
    val results: List<Character>
)

/**
 * Clase que representa la información de paginación en la respuesta de la API.
 *
 * @param count Número total de personajes.
 * @param pages Número total de páginas.
 * @param next URL de la siguiente página (si existe).
 * @param prev URL de la página anterior (si existe).
 */
data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

/**
 * Clase que representa un personaje en la respuesta de la API.
 *
 * @param id ID del personaje
 * @param name Nombre del personaje
 * @param status Estado del personaje (e.g., "Alive", "Dead", "unknown")
 * @param species Especie del personaje
 * @param type Tipo del personaje (puede estar vacío)
 * @param gender Género del personaje
 * @param origin Origen del personaje
 * @param location Ubicación actual del personaje
 * @param image URL de la imagen del personaje
 * @param episode Lista de URLs de los episodios en los que aparece el personaje
 * @param url URL de la información completa del personaje en la API
 * @param created Fecha de creación del personaje en la API
 */
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String

)

/**
 * Clase que representa el origen de un personaje.
 *
 * @param name Nombre del origen
 * @param url URL de la información completa del origen en la API
 */
data class Origin(
    val name: String,
    val url: String
)

/**
 * Clase que representa la ubicación actual de un personaje
 *
 * @param name Nombre de la ubicación
 * @param url URL de la información completa de la ubicación en la API
 */
data class Location(
    val name: String,
    val url: String
)