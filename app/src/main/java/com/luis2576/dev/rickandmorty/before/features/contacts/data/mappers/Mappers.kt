package com.luis2576.dev.rickandmorty.before.features.contacts.data.mappers

import com.luis2576.dev.rickandmorty.before.features.contacts.data.local.ContactEntity
import com.luis2576.dev.rickandmorty.before.features.contacts.data.local.ContactEntityPreview
import com.luis2576.dev.rickandmorty.before.features.contacts.data.remote.Character
import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.model.Contact
import com.luis2576.dev.rickandmorty.before.features.contacts.domain.model.ContactPreview

/**
 * Convierte una entidad de vista previa de contacto (`ContactEntityPreview`) en un objeto de vista previa de contacto (`ContactPreview`).
 *
 * @receiver La entidad de vista previa de contacto a convertir
 * @return El objeto de vista previa de contacto resultante
 */
fun ContactEntityPreview.toContactPreview(): ContactPreview {
    return ContactPreview(
        id = this.id,
        name = this.name,
        image = this.image
    )
}

/**
 * Convierte un personaje (`Character`) obtenido de la API en una entidad de contacto (`ContactEntity`) para ser almacenada en la base de datos.
 *
 * @receiver El personaje a convertir
 * @return La entidad de contacto resultante
 */
fun Character.toContactEntity(): ContactEntity {
    return ContactEntity(
        id = this.id.toString(),
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type.ifEmpty { "" },
        gender = this.gender,
        originName = this.origin.name,
        originUrl = this.origin.url,
        locationName = this.location.name,
        locationUrl = this.location.url,
        image = this.image,
        episodeList = this.episode,
        created = this.created
    )
}

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
fun ContactEntity.toContact(): Contact {
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
