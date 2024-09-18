package com.luis2576.dev.rickandmorty.di

import com.luis2576.dev.rickandmorty.data.repositories.AuthRepositoryImpl
import com.luis2576.dev.rickandmorty.domain.repositories.AuthRepository
import com.luis2576.dev.rickandmorty.data.repositories.ContactRepositoryImpl
import com.luis2576.dev.rickandmorty.domain.repositories.ContactRepository
import com.luis2576.dev.rickandmorty.data.repositories.IndividualChatRepositoryImpl
import com.luis2576.dev.rickandmorty.domain.repositories.IndividualChatRepository
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
    abstract fun bindContactRepository(
        repositoryImpl: ContactRepositoryImpl
    ): ContactRepository

    @Binds
    @Singleton
    abstract fun bindIndividualChatRepository(
        repositoryImpl: IndividualChatRepositoryImpl
    ): IndividualChatRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        repositoryImpl: AuthRepositoryImpl
    ): AuthRepository

}

