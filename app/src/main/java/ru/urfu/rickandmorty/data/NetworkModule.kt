package ru.urfu.rickandmorty.data

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.urfu.rickandmorty.BuildConfig
import ru.urfu.rickandmorty.data.local.AppDatabase
import ru.urfu.rickandmorty.data.remote.ApiService
import ru.urfu.rickandmorty.data.repository.CharacterRepositoryImpl
import ru.urfu.rickandmorty.domain.repository.CharacterRepository
import ru.urfu.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import ru.urfu.rickandmorty.domain.usecase.GetCharactersUseCase
import java.util.concurrent.TimeUnit
import ru.urfu.rickandmorty.core.CoreModule

object NetworkModule {
    private const val BASE_URL = "https://rickandmortyapi.com/"
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        CoreModule.init(context)
    }

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(appContext!!, AppDatabase::class.java, "rick_and_morty_db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    val favoriteDao by lazy { database.favoriteDao() }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG && appContext != null) {
            builder.addInterceptor(ChuckerInterceptor(appContext!!))
        }
        builder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val characterRepository: CharacterRepository by lazy {
        CharacterRepositoryImpl(apiService, favoriteDao)
    }

    val getCharactersUseCase: GetCharactersUseCase by lazy {
        GetCharactersUseCase(characterRepository)
    }

    val getCharacterByIdUseCase: GetCharacterByIdUseCase by lazy {
        GetCharacterByIdUseCase(characterRepository)
    }
}