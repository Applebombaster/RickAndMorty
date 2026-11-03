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

data class UserProfile(
    val fullName: String = "",
    val jobTitle: String = "",
    val resumeUrl: String = "",
    val avatarUri: String = "",
    val favoriteTime: String = ""
)

class UserPreferencesRepository(private val context: Context) {
    companion object {
        private val KEY_NAME = stringPreferencesKey("filter_name")
        private val KEY_STATUS = stringPreferencesKey("filter_status")
        private val KEY_SPECIES = stringPreferencesKey("filter_species")
        private val KEY_PROFILE_NAME = stringPreferencesKey("profile_name")
        private val KEY_PROFILE_JOB = stringPreferencesKey("profile_job")
        private val KEY_PROFILE_RESUME = stringPreferencesKey("profile_resume")
        private val KEY_PROFILE_AVATAR = stringPreferencesKey("profile_avatar")
        private val KEY_PROFILE_TIME = stringPreferencesKey("profile_time")
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

    val userProfileFlow: Flow<UserProfile> = context.dataStore.data.map { prefs ->
        UserProfile(
            fullName = prefs[KEY_PROFILE_NAME] ?: "",
            jobTitle = prefs[KEY_PROFILE_JOB] ?: "",
            resumeUrl = prefs[KEY_PROFILE_RESUME] ?: "",
            avatarUri = prefs[KEY_PROFILE_AVATAR] ?: "",
            favoriteTime = prefs[KEY_PROFILE_TIME] ?: ""
        )
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[KEY_PROFILE_NAME] = profile.fullName
            prefs[KEY_PROFILE_JOB] = profile.jobTitle
            prefs[KEY_PROFILE_RESUME] = profile.resumeUrl
            prefs[KEY_PROFILE_AVATAR] = profile.avatarUri
            prefs[KEY_PROFILE_TIME] = profile.favoriteTime
        }
    }
}