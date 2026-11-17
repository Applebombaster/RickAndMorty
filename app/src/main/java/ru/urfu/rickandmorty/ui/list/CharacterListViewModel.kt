package ru.urfu.rickandmorty.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.urfu.rickandmorty.core.CoreModule
import ru.urfu.rickandmorty.core.local.FilterSettings
import ru.urfu.rickandmorty.data.NetworkModule
import ru.urfu.rickandmorty.domain.model.Character

sealed interface CharacterListUiState {
    data object Loading : CharacterListUiState
    data class Success(
        val characters: List<CharacterUi>,
        val favorites: Set<Int>,
        val page: Int,
        val hasNextPage: Boolean,
        val isLoadingNextPage: Boolean = false
    ) : CharacterListUiState
    data class Error(val message: String) : CharacterListUiState
}

class CharacterListViewModel : ViewModel() {
    private val repo = NetworkModule.characterRepository
    private val prefsRepo = CoreModule.prefsRepository

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = CharacterListUiState.Error(throwable.message ?: "Ошибка загрузки")
    }

    private val _uiState = MutableStateFlow<CharacterListUiState>(CharacterListUiState.Loading)
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    private var currentFilters = FilterSettings()

    init {
        viewModelScope.launch(exceptionHandler) {
            prefsRepo.filterSettingsFlow.collect { savedFilters ->
                currentFilters = savedFilters
                loadFirstPage()
            }
        }
        viewModelScope.launch(exceptionHandler) {
            repo.getFavoriteIds().collect { favIds ->
                val current = _uiState.value
                if (current is CharacterListUiState.Success) {
                    _uiState.value = current.copy(favorites = favIds)
                }
            }
        }
    }

    fun loadFirstPage() {
        _uiState.value = CharacterListUiState.Loading
        viewModelScope.launch(exceptionHandler) {
            val page = repo.getCharacters(1, currentFilters.name, currentFilters.status, currentFilters.species)
            _uiState.value = CharacterListUiState.Success(
                characters = page.characters.map { it.toUi() },
                favorites = emptySet(),
                page = page.currentPage,
                hasNextPage = page.hasNextPage
            )
        }
    }

    fun loadNextPage() {
        val current = _uiState.value as? CharacterListUiState.Success ?: return
        if (!current.hasNextPage || current.isLoadingNextPage) return

        _uiState.update { current.copy(isLoadingNextPage = true) }
        viewModelScope.launch(exceptionHandler) {
            val page = repo.getCharacters(current.page + 1, currentFilters.name, currentFilters.status, currentFilters.species)
            _uiState.update { state ->
                val s = state as CharacterListUiState.Success
                s.copy(
                    characters = s.characters + page.characters.map { it.toUi() },
                    page = page.currentPage,
                    hasNextPage = page.hasNextPage,
                    isLoadingNextPage = false
                )
            }
        }
    }

    fun toggleFavorite(id: Int) {
        val current = _uiState.value as? CharacterListUiState.Success ?: return
        val characterUi = current.characters.find { it.id == id } ?: return

        viewModelScope.launch(exceptionHandler) {
            repo.toggleFavorite(characterUi.toDomain())
        }
    }
}