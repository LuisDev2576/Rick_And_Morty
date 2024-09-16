package com.luis2576.dev.rickandmorty.after.di

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.luis2576.dev.rickandmorty.after.util.NetworkUtil
import com.luis2576.dev.rickandmorty.after.domain.repositories.UserInformationRepository
import com.luis2576.dev.rickandmorty.after.data.repositories.UserInformationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideNetworkUtil(context: Context): NetworkUtil = NetworkUtil(context)

    @Provides
    fun provideFirebaseStorage() =  Firebase.storage

    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun provideUserInformationRepository(
        context: Context,
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): UserInformationRepository {
        return UserInformationRepositoryImpl(context, firebaseFirestore, firebaseStorage)
    }

}