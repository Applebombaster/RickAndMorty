package ru.urfu.rickandmorty.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.urfu.rickandmorty.data.local.FavoriteDao
import ru.urfu.rickandmorty.data.local.toEntity
import ru.urfu.rickandmorty.data.mapper.toDomain
import ru.urfu.rickandmorty.data.remote.ApiService
import ru.urfu.rickandmorty.domain.model.Character
import ru.urfu.rickandmorty.domain.model.CharacterPage
import ru.urfu.rickandmorty.domain.repository.CharacterRepository

class CharacterRepositoryImpl(
    private val api: ApiService,
    private val dao: FavoriteDao
) : CharacterRepository {

    override suspend fun getCharacters(
        page: Int, name: String?, status: String?, species: String?
    ): CharacterPage {
        val response = api.getCharacters(
            page = page,
            name = name?.takeIf { it.isNotBlank() },
            status = status?.takeIf { it.isNotBlank() },
            species = species?.takeIf { it.isNotBlank() }
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

    override fun getFavoriteIds(): Flow<Set<Int>> =
        dao.getAllFavorites().map { entities ->
            entities.map { it.id }.toSet()
        }

    override suspend fun toggleFavorite(character: Character) {
        if (dao.isFavoriteSync(character.id)) {
            dao.deleteFavorite(character.id)
        } else {
            dao.insertFavorite(character.toEntity())
        }
    }
}