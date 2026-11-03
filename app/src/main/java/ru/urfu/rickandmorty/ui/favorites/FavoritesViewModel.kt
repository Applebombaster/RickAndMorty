package ru.urfu.rickandmorty.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.urfu.rickandmorty.data.NetworkModule
import ru.urfu.rickandmorty.data.local.toDomain
import ru.urfu.rickandmorty.domain.model.Character

class FavoritesViewModel : ViewModel() {
    private val dao = NetworkModule.favoriteDao

    // Получаем список избранного из базы. Room сам обновляет Flow при изменениях!
    val favorites: StateFlow<List<Character>> = dao.getAllFavorites()
        .map { entities -> entities.map { it.toDomain() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFavorite(character: Character) {
        viewModelScope.launch {
            dao.deleteFavorite(character.id)
        }
    }
}