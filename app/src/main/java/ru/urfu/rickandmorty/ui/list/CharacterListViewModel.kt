package ru.urfu.rickandmorty.ui.list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.urfu.rickandmorty.data.model.Character
import ru.urfu.rickandmorty.data.repository.CharacterRepository

sealed interface CharacterListUiState {
    data object Loading : CharacterListUiState
    data class Success(val characters: List<Character>) : CharacterListUiState
    data class Error(val message: String) : CharacterListUiState
}

class CharacterListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterListUiState>(CharacterListUiState.Loading)
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        // В практике 4 здесь будет вызов Retrofit.
        // Пока данные мок — но структура уже готова к сети.
        val characters = CharacterRepository.getCharacters()
        _uiState.value = CharacterListUiState.Success(characters)
    }
}