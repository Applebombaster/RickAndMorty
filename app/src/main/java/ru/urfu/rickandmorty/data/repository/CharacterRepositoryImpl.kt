package ru.urfu.rickandmorty.data.repository

import ru.urfu.rickandmorty.data.mapper.toDomain
import ru.urfu.rickandmorty.data.remote.ApiService
import ru.urfu.rickandmorty.domain.model.Character
import ru.urfu.rickandmorty.domain.model.CharacterPage
import ru.urfu.rickandmorty.domain.repository.CharacterRepository

class CharacterRepositoryImpl(
    private val api: ApiService
) : CharacterRepository {

    override suspend fun getCharacters(page: Int, name: String?): CharacterPage {
        val response = api.getCharacters(
            page = page,
            name = name?.takeIf { it.isNotBlank() }
        )
        return CharacterPage(
            characters = response.results.map { it.toDomain() },
            currentPage = page,
            totalPages = response.info.pages,
            hasNextPage = response.info.next != null
        )
    }

    override suspend fun getCharacterById(id: Int): Character =
        api.getCharacterById(id).toDomain()
}