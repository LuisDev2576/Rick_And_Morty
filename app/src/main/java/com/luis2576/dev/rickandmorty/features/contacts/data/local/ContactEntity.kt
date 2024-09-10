package com.luis2576.dev.rickandmorty.features.contacts.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room que representa un contacto en la base de datos
 *
 * @param id Identificador único del contacto (clave primaria, no autogenerada)
 * @param name Nombre del contacto
 * @param status Estado actual del contacto
 * @param species Especie del contacto
 * @param type Subtipo o clasificación del contacto
 * @param gender Género del contacto
 * @param originName Nombre del lugar de origen
 * @param originUrl URL con más información sobre el origen
 * @param locationName Nombre de la ubicación actual
 * @param locationUrl URL con más información sobre la ubicación
 * @param image URL de la imagen del contacto
 * @param episodeList Lista de URLs de los episodios en los que aparece (si aplica)
 * @param created Fecha de creación del contacto en la fuente original
 */
@Entity
data class ContactEntity(
    @PrimaryKey(autoGenerate = false)
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