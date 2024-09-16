package com.luis2576.dev.rickandmorty.after.data.local.models

import androidx.room.PrimaryKey
import com.luis2576.dev.rickandmorty.after.domain.models.ContactPreview

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

/**
 * Convierte una entidad de vista previa de contacto (`ContactEntityPreview`) en un objeto de vista previa de contacto (`ContactPreview`).
 *
 * @receiver La entidad de vista previa de contacto a convertir
 * @return El objeto de vista previa de contacto resultante
 */

fun ContactEntityPreview.toDomain(): ContactPreview {
    return ContactPreview(
        id = this.id,
        name = this.name,
        image = this.image
    )
}
