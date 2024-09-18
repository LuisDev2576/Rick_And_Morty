package com.luis2576.dev.rickandmorty.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import coil.ImageLoader
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.luis2576.dev.rickandmorty.data.local.database.AppDatabase
import com.luis2576.dev.rickandmorty.data.local.database.ContactsDao
import com.luis2576.dev.rickandmorty.data.remote.api.RickAndMortyApi
import com.luis2576.dev.rickandmorty.util.NetworkUtil
import com.luis2576.dev.rickandmorty.domain.repositories.UserInformationRepository
import com.luis2576.dev.rickandmorty.data.repositories.UserInformationRepositoryImpl
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
    fun provideImageLoader(app: Application): ImageLoader {
        return ImageLoader(app)
    }

}
