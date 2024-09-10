package com.luis2576.dev.rickandmorty.features.individualChat.domain.model

import javax.annotation.concurrent.Immutable

/**
 * Representa un contacto completo con todos sus detalles. Inmutable para garantizar la integridad de los datos.
 *
 * @param id Identificador único del contacto.
 * @param name Nombre del contacto.
 * @param status Estado actual del contacto (e.g., "Alive", "Dead", "unknown").
 * @param species Especie del contacto.
 * @param type Subtipo o clasificación adicional del contacto (puede estar vacío).
 * @param gender Género del contacto.
 * @param originName Nombre del lugar de origen del contacto.
 * @param originUrl (Opcional) URL con más información sobre el origen del contacto.
 * @param locationName Nombre de la ubicación actual del contacto.
 * @param locationUrl (Opcional) URL con más información sobre la ubicación actual del contacto.
 * @param image URL de la imagen del contacto.
 * @param episodeList Lista de URLs de los episodios en los que aparece el contacto (relevante si se trata de personajes de una serie).
 * @param created Fecha de creación del contacto en la fuente de datos original.
 */
@Immutable
data class Contact(
    val id: String,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val originUrl: String?,
    val locationName: String,
    val locationUrl: String?,
    val image: String,
    val episodeList: List<String>,
    val created: String
)
