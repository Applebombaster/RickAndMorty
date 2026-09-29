package ru.urfu.rickandmorty.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.urfu.rickandmorty.data.model.Character
import ru.urfu.rickandmorty.data.repository.CharacterRepository
import ru.urfu.rickandmorty.ui.navigation.Routes

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // ID приходит из навигации как аргумент маршрута "character/{id}".
    // SavedStateHandle сам достаёт его из NavBackStackEntry.
    private val characterId: Int = checkNotNull(savedStateHandle[Routes.ARG_CHARACTER_ID])

    private val _character = MutableStateFlow<Character?>(null)
    val character: StateFlow<Character?> = _character.asStateFlow()

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        // В практике 4 заменим на сетевой запрос по characterId.
        _character.value = CharacterRepository.getCharacterById(characterId)
    }
}