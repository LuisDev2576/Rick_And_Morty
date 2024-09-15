package com.luis2576.dev.rickandmorty.before.features.contacts.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.luis2576.dev.rickandmorty.before.util.Converters

/**
 * Clase abstracta que representa la base de datos de la aplicación. Utiliza Room para gestionar la persistencia de datos.
 *
 * @param entities Lista de entidades que se almacenarán en la base de datos
 * @param version Versión de la base de datos. Se debe incrementar al realizar cambios en el esquema
 * @param exportSchema Indica si se debe exportar el esquema de la base de datos a un archivo. Útil para migraciones
 */
@Database(
    entities = [ContactEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Proporciona acceso al DAO (Data Access Object) para interactuar con la tabla de contactos
     *
     * @return Una instancia de `ContactsDao`
     */
    abstract fun contactDao(): ContactsDao
}