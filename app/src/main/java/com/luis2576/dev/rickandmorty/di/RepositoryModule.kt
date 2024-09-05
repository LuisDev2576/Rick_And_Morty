package com.luis2576.dev.rickandmorty.di

import com.luis2576.dev.rickandmorty.data.repository.CharacterRepositoryImpl
import com.luis2576.dev.rickandmorty.domain.repository.CharacterRepository
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
        repositoryImpl: CharacterRepositoryImpl
    ): CharacterRepository
}
