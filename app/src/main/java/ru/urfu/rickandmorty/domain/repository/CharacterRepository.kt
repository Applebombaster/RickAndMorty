package ru.urfu.rickandmorty.domain.repository

import ru.urfu.rickandmorty.domain.model.Character
import ru.urfu.rickandmorty.domain.model.CharacterPage

interface CharacterRepository {
    suspend fun getCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null
    ): CharacterPage

    suspend fun getCharacterById(id: Int): Character
}