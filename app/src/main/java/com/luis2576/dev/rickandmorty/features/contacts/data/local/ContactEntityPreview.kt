package com.luis2576.dev.rickandmorty.features.contacts.data.local

import androidx.room.PrimaryKey

/**
 * Entidad de Room que representa una vista previa de un contacto en la base de datos.
 * Se utiliza para mostrar información básica de los contactos de manera eficiente
 *
 * @param id Identificador único del contacto (clave primaria, no autogenerada)
 * @param name Nombre del contacto
 * @param image URL de la imagen del contacto
 */
data class ContactEntityPreview(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val image: String
)