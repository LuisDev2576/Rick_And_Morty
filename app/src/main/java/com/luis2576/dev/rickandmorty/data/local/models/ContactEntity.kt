package com.luis2576.dev.rickandmorty.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luis2576.dev.rickandmorty.domain.models.ContactPreview
import com.luis2576.dev.rickandmorty.domain.models.Contact

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

/**
 * Convierte una entidad de contacto (`ContactEntity`) en un objeto de vista previa de contacto (`ContactPreview`).
 *
 * @receiver La entidad de contacto a convertir
 * @return El objeto de vista previa de contacto resultante
 */
fun ContactEntity.toContactPreview(): ContactPreview {
    return ContactPreview(
        id = this.id,
        name = this.name,
        image = this.image
    )
}

/**
 * Convierte una entidad de contacto (`ContactEntity`) en un objeto de contacto completo (`Contact`).
 *
 * @receiver La entidad de contacto a convertir
 * @return El objeto de contacto completo resultante
 */
fun ContactEntity.toDomain(): Contact {
    return Contact(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        originName = this.originName,
        originUrl = this.originUrl,
        locationName = this.locationName,
        locationUrl = this.locationUrl,
        image = this.image,
        episodeList = this.episodeList,
        created = this.created
    )
}
