package com.luis2576.dev.rickandmorty.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import coil.ImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.luis2576.dev.rickandmorty.features.contacts.data.dataSource.ContactDataSourceImpl
import com.luis2576.dev.rickandmorty.features.contacts.data.local.AppDatabase
import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactsDao
import com.luis2576.dev.rickandmorty.features.contacts.data.remote.RickAndMortyApi
import com.luis2576.dev.rickandmorty.features.contacts.domain.dataSource.ContactDataSource
import com.luis2576.dev.rickandmorty.features.individualChat.data.dataSource.IndividualChatDataSourceImpl
import com.luis2576.dev.rickandmorty.features.individualChat.domain.dataSource.IndividualChatDataSource
import com.luis2576.dev.rickandmorty.util.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideImageLoader(app: Application): ImageLoader {
        return ImageLoader(app)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()
    }

    @Provides
    @Singleton
    fun providesRickAndMortyApi(client: OkHttpClient, gson: Gson): RickAndMortyApi {
        return Retrofit.Builder()
            .baseUrl(RickAndMortyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(RickAndMortyApi::class.java)
    }

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
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideNetworkUtil(context: Context): NetworkUtil = NetworkUtil(context)

    @Provides
    @Singleton
    fun provideContactDao(appDatabase: AppDatabase): ContactsDao {
        return appDatabase.contactDao()
    }

    @Provides
    @Singleton
    fun provideContactDataSource(contactDao: ContactsDao): ContactDataSource {
        return ContactDataSourceImpl(contactDao)
    }


    @Provides
    fun provideFirebaseStorage() =  Firebase.storage

    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun provideIndividualChatDataSource(
        contactDao: ContactsDao,
        firebaseFirestore: FirebaseFirestore
    ): IndividualChatDataSource {
        return IndividualChatDataSourceImpl(contactDao, firebaseFirestore)
    }
}
