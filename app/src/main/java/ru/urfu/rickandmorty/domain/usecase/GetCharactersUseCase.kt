package ru.urfu.rickandmorty.domain.usecase

import ru.urfu.rickandmorty.domain.model.CharacterPage
import ru.urfu.rickandmorty.domain.repository.CharacterRepository

class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int = 1, name: String? = null): CharacterPage =
        repository.getCharacters(page, name)
}