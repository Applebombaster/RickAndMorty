package ru.urfu.rickandmorty.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Глобальный кэш состояния настроек.
 * Живет в памяти, пока приложение не будет убито системой.
 */
object SettingsStateHolder {
    private val _hasActiveSettings = MutableStateFlow(false)
    val hasActiveSettings: StateFlow<Boolean> = _hasActiveSettings.asStateFlow()

    fun updateState(hasSettings: Boolean) {
        _hasActiveSettings.value = hasSettings
    }
}