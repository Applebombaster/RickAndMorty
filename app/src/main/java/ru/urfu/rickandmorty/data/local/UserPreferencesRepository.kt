package ru.urfu.rickandmorty.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class FilterSettings(
    val name: String = "",
    val status: String = "",
    val species: String = ""
)

class UserPreferencesRepository(private val context: Context) {
    companion object {
        private val KEY_NAME = stringPreferencesKey("filter_name")
        private val KEY_STATUS = stringPreferencesKey("filter_status")
        private val KEY_SPECIES = stringPreferencesKey("filter_species")
    }

    val filterSettingsFlow: Flow<FilterSettings> = context.dataStore.data.map { prefs ->
        FilterSettings(
            name = prefs[KEY_NAME] ?: "",
            status = prefs[KEY_STATUS] ?: "",
            species = prefs[KEY_SPECIES] ?: ""
        )
    }

    suspend fun saveFilterSettings(settings: FilterSettings) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NAME] = settings.name
            prefs[KEY_STATUS] = settings.status
            prefs[KEY_SPECIES] = settings.species
        }
    }
}