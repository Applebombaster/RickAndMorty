package ru.urfu.rickandmorty.data

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.urfu.rickandmorty.BuildConfig  // <--- Импорт BuildConfig
import ru.urfu.rickandmorty.data.remote.ApiService
import ru.urfu.rickandmorty.data.repository.CharacterRepositoryImpl
import ru.urfu.rickandmorty.domain.repository.CharacterRepository
import ru.urfu.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import ru.urfu.rickandmorty.domain.usecase.GetCharactersUseCase
import java.util.concurrent.TimeUnit

object NetworkModule {

    private const val BASE_URL = "https://rickandmortyapi.com/"

    // Контекст для Chucker
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)

        // Подключаем Chucker только в debug и только если контекст передан
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
        CharacterRepositoryImpl(apiService)
    }

    val getCharactersUseCase: GetCharactersUseCase by lazy {
        GetCharactersUseCase(characterRepository)
    }

    val getCharacterByIdUseCase: GetCharacterByIdUseCase by lazy {
        GetCharacterByIdUseCase(characterRepository)
    }
}