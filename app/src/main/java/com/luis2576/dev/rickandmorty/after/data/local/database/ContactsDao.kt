package com.luis2576.dev.rickandmorty.after.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.luis2576.dev.rickandmorty.after.data.local.models.ContactEntity
import com.luis2576.dev.rickandmorty.after.data.local.models.ContactEntityPreview

/**
 * Interfaz DAO (Data Access Object) que define las operaciones para acceder a los contactos en la base de datos
 */
@Dao
interface ContactsDao {

    /**
     * Obtiene una lista de vistas previas de todos los contactos almacenados en la base de datos
     *
     * @return Lista de entidades de vista previa de contactos
     */
    @Query("SELECT id, name, image FROM ContactEntity")
    suspend fun getAllContactsPreview(): List<ContactEntityPreview>

    /**
     * Inserta o actualiza una lista de contactos en la base de datos.
     * Si un contacto con el mismo ID ya existe, se actualizará su información
     *
     * @param characterList Lista de entidades de contactos a insertar o actualizar
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertContactList(characterList: List<ContactEntity>)

    /**
     * Obtiene un contacto por su ID desde la base de datos
     *
     * @param characterId ID del contacto a buscar
     * @return Entidad del contacto si se encuentra, null en caso contrario
     */
    @Query("SELECT * FROM ContactEntity WHERE id = :characterId")
    suspend fun getContactById(characterId: Int): ContactEntity

}