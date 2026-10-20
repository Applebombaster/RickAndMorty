package ru.urfu.rickandmorty.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.urfu.rickandmorty.data.NetworkModule
import ru.urfu.rickandmorty.domain.model.Character

sealed interface CharacterListUiState {
    data object Loading : CharacterListUiState
    data class Success(
        val characters: List<Character>,
        val page: Int,
        val hasNextPage: Boolean,
        val isLoadingNextPage: Boolean = false
    ) : CharacterListUiState
    data class Error(val message: String) : CharacterListUiState
}

class CharacterListViewModel : ViewModel() {

    private val getCharactersUseCase = NetworkModule.getCharactersUseCase

    private val _uiState = MutableStateFlow<CharacterListUiState>(CharacterListUiState.Loading)
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    private var searchQuery: String = ""

    init {
        loadFirstPage()
    }

    /** Первая загрузка или перезагрузка после поиска/ошибки. */
    fun loadFirstPage() {
        _uiState.value = CharacterListUiState.Loading
        viewModelScope.launch {
            runCatching {
                getCharactersUseCase(page = 1, name = searchQuery)
            }.onSuccess { page ->
                _uiState.value = CharacterListUiState.Success(
                    characters = page.characters,
                    page = page.currentPage,
                    hasNextPage = page.hasNextPage
                )
            }.onFailure { e ->
                _uiState.value = CharacterListUiState.Error(
                    message = e.message ?: "Не удалось загрузить данные"
                )
            }
        }
    }

    /** Подгрузка следующей страницы — пагинация. */
    fun loadNextPage() {
        val current = _uiState.value as? CharacterListUiState.Success ?: return
        if (!current.hasNextPage || current.isLoadingNextPage) return

        _uiState.update { current.copy(isLoadingNextPage = true) }
        viewModelScope.launch {
            runCatching {
                getCharactersUseCase(page = current.page + 1, name = searchQuery)
            }.onSuccess { page ->
                _uiState.update {
                    (it as CharacterListUiState.Success).copy(
                        characters = it.characters + page.characters,
                        page = page.currentPage,
                        hasNextPage = page.hasNextPage,
                        isLoadingNextPage = false
                    )
                }
            }.onFailure {
                _uiState.update {
                    (it as CharacterListUiState.Success).copy(isLoadingNextPage = false)
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery = query.trim()
    }

    fun onSearchSubmitted() {
        loadFirstPage()
    }
}