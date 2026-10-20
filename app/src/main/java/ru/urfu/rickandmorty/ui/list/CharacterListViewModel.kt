package ru.urfu.rickandmorty.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.urfu.rickandmorty.data.NetworkModule
import ru.urfu.rickandmorty.data.local.FilterSettings
import ru.urfu.rickandmorty.data.local.toEntity
import ru.urfu.rickandmorty.domain.model.Character

sealed interface CharacterListUiState {
    data object Loading : CharacterListUiState
    data class Success(
        val characters: List<Character>,
        val favorites: Set<Int>, // Храним ID избранных
        val page: Int,
        val hasNextPage: Boolean,
        val isLoadingNextPage: Boolean = false
    ) : CharacterListUiState
    data class Error(val message: String) : CharacterListUiState
}

class CharacterListViewModel : ViewModel() {
    private val getCharactersUseCase = NetworkModule.getCharactersUseCase
    private val prefsRepo = NetworkModule.prefsRepository
    private val dao = NetworkModule.favoriteDao

    private val _uiState = MutableStateFlow<CharacterListUiState>(CharacterListUiState.Loading)
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    private var currentFilters = FilterSettings()

    init {
        // Слушаем фильтры
        viewModelScope.launch {
            prefsRepo.filterSettingsFlow.collect { savedFilters ->
                currentFilters = savedFilters
                loadFirstPage()
            }
        }
        // Слушаем избранное из БД (чтобы сердечки были красными)
        viewModelScope.launch {
            dao.getAllFavorites().collect { entities ->
                val favIds = entities.map { it.id }.toSet()
                val current = _uiState.value
                if (current is CharacterListUiState.Success) {
                    _uiState.value = current.copy(favorites = favIds)
                }
            }
        }
    }

    fun loadFirstPage() {
        _uiState.value = CharacterListUiState.Loading
        viewModelScope.launch {
            runCatching {
                getCharactersUseCase(1, currentFilters.name, currentFilters.status, currentFilters.species)
            }.onSuccess { page ->
                // Получаем текущие избранные ID
                dao.getAllFavorites().collect { entities ->
                    val favIds = entities.map { it.id }.toSet()
                    _uiState.value = CharacterListUiState.Success(
                        characters = page.characters,
                        favorites = favIds,
                        page = page.currentPage,
                        hasNextPage = page.hasNextPage
                    )
                }
            }.onFailure { e ->
                _uiState.value = CharacterListUiState.Error(e.message ?: "Ошибка")
            }
        }
    }

    fun loadNextPage() {
        val current = _uiState.value as? CharacterListUiState.Success ?: return
        if (!current.hasNextPage || current.isLoadingNextPage) return

        _uiState.update { current.copy(isLoadingNextPage = true) }
        viewModelScope.launch {
            runCatching {
                getCharactersUseCase(current.page + 1, currentFilters.name, currentFilters.status, currentFilters.species)
            }.onSuccess { page ->
                _uiState.update { state ->
                    val s = state as CharacterListUiState.Success
                    s.copy(
                        characters = s.characters + page.characters,
                        page = page.currentPage,
                        hasNextPage = page.hasNextPage,
                        isLoadingNextPage = false
                    )
                }
            }.onFailure {
                _uiState.update { (it as CharacterListUiState.Success).copy(isLoadingNextPage = false) }
            }
        }
    }

    fun toggleFavorite(character: Character) {
        val current = _uiState.value as? CharacterListUiState.Success ?: return
        viewModelScope.launch {
            if (character.id in current.favorites) {
                dao.deleteFavorite(character.id)
            } else {
                dao.insertFavorite(character.toEntity())
            }
        }
    }
}