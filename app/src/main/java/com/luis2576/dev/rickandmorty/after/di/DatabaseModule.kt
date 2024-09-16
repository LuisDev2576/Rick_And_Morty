package com.luis2576.dev.rickandmorty.after.di

import android.app.Application
import androidx.room.Room
import com.luis2576.dev.rickandmorty.after.data.local.database.AppDatabase
import com.luis2576.dev.rickandmorty.after.data.local.database.ContactsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            AppDatabase::class.java,
        "contactDatabase.db"
        ).fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideContactDao(appDatabase: AppDatabase): ContactsDao {
        return appDatabase.contactDao()
    }
}

