package com.luis2576.dev.rickandmorty.di

import com.luis2576.dev.rickandmorty.features.authentication.data.repository.AuthRepositoryImpl
import com.luis2576.dev.rickandmorty.features.authentication.domain.repository.AuthRepository
import com.luis2576.dev.rickandmorty.features.contacts.data.repository.ContactRepositoryImpl
import com.luis2576.dev.rickandmorty.features.contacts.domain.repository.ContactRepository
import com.luis2576.dev.rickandmorty.features.individualChat.data.repository.IndividualChatRepositoryImpl
import com.luis2576.dev.rickandmorty.features.individualChat.domain.repository.IndividualChatRepository
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

