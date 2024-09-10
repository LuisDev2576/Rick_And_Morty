package com.luis2576.dev.rickandmorty.di

import com.luis2576.dev.rickandmorty.features.contacts.data.repository.ContactRepositoryImpl
import com.luis2576.dev.rickandmorty.features.contacts.domain.repository.ContactRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCharacterRepository(
        repositoryImpl: ContactRepositoryImpl
    ): ContactRepository
}
