package ru.urfu.rickandmorty.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.urfu.rickandmorty.data.remote.dto.CharacterDto
import ru.urfu.rickandmorty.data.remote.dto.CharacterListResponseDto

interface ApiService {
    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null
    ): CharacterListResponseDto

    @GET("api/character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDto
}