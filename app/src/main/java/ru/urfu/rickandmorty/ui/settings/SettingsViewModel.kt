package ru.urfu.rickandmorty.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.urfu.rickandmorty.data.NetworkModule
import ru.urfu.rickandmorty.data.local.FilterSettings
import ru.urfu.rickandmorty.data.local.SettingsStateHolder

class SettingsViewModel : ViewModel() {
    private val prefsRepo = NetworkModule.prefsRepository

    private val _settings = MutableStateFlow(FilterSettings())
    val settings: StateFlow<FilterSettings> = _settings.asStateFlow()

    init {
        viewModelScope.launch {
            prefsRepo.filterSettingsFlow.collect { savedSettings ->
                _settings.value = savedSettings
                // Проверяем, активны ли фильтры, и обновляем бейдж
                checkAndUpdateBadge(savedSettings)
            }
        }
    }

    fun onNameChanged(name: String) {
        _settings.value = _settings.value.copy(name = name)
        checkAndUpdateBadge(_settings.value)
    }

    fun onStatusChanged(status: String) {
        _settings.value = _settings.value.copy(status = status)
        checkAndUpdateBadge(_settings.value)
    }

    fun onSpeciesChanged(species: String) {
        _settings.value = _settings.value.copy(species = species)
        checkAndUpdateBadge(_settings.value)
    }

    fun saveSettings(onSaved: () -> Unit) {
        viewModelScope.launch {
            prefsRepo.saveFilterSettings(_settings.value)
            onSaved()
        }
    }

    private fun checkAndUpdateBadge(currentSettings: FilterSettings) {
        // Если хотя бы одно поле не пустое — бейдж горит
        val hasActive = currentSettings.name.isNotBlank() ||
                currentSettings.status.isNotBlank() ||
                currentSettings.species.isNotBlank()
        SettingsStateHolder.updateState(hasActive)
    }
}