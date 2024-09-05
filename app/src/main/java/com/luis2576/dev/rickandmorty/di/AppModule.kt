package com.luis2576.dev.rickandmorty.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import coil.ImageLoader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.luis2576.dev.rickandmorty.data.dataSource.CharacterDataSourceImpl
import com.luis2576.dev.rickandmorty.data.local.AppDatabase
import com.luis2576.dev.rickandmorty.data.local.CharactersDao
import com.luis2576.dev.rickandmorty.data.remote.RickAndMortyApi
import com.luis2576.dev.rickandmorty.domain.dataSource.CharacterDataSource
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
            "chractersDatabase.db"
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
    fun provideCharacterDao(appDatabase: AppDatabase): CharactersDao {
        return appDatabase.characterDao()
    }

    @Provides
    @Singleton
    fun provideCharacterDataSource(characterDao: CharactersDao): CharacterDataSource {
        return CharacterDataSourceImpl(characterDao)
    }
}
