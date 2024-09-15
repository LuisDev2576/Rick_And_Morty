package com.luis2576.dev.rickandmorty.before.features.contacts.domain.model

import javax.annotation.concurrent.Immutable

/**
 * Representa una vista previa de un contacto con información básica. Inmutable para garantizar la integridad de los datos.
 *
 * @param id Identificador único del contacto.
 * @param name Nombre del contacto
 * @param image URL de la imagen del contacto
 */
@Immutable
data class ContactPreview(
    val id: String,
    val name: String,
    val image: String
)