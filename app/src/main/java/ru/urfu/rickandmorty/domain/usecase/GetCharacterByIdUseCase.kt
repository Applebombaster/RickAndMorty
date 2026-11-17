package ru.urfu.rickandmorty.domain.usecase

import ru.urfu.rickandmorty.domain.model.Character
import ru.urfu.rickandmorty.domain.repository.CharacterRepository

class GetCharacterByIdUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Character = repository.getCharacterById(id)
}