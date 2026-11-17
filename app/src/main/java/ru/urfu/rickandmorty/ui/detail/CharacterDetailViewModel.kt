package ru.urfu.rickandmorty.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.urfu.rickandmorty.data.NetworkModule
import ru.urfu.rickandmorty.domain.model.Character
import ru.urfu.rickandmorty.ui.navigation.Routes

sealed interface CharacterDetailUiState {
    data object Loading : CharacterDetailUiState
    data class Success(val character: Character) : CharacterDetailUiState
    data class Error(val message: String) : CharacterDetailUiState
}

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle[Routes.ARG_CHARACTER_ID])
    private val getCharacterByIdUseCase = NetworkModule.getCharacterByIdUseCase

    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = CharacterDetailUiState.Loading
        viewModelScope.launch {
            runCatching { getCharacterByIdUseCase(characterId) }
                .onSuccess { _uiState.value = CharacterDetailUiState.Success(it) }
                .onFailure {
                    _uiState.value = CharacterDetailUiState.Error(
                        it.message ?: "Не удалось загрузить персонажа"
                    )
                }
        }
    }
}