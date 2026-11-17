package ru.urfu.rickandmorty.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.urfu.rickandmorty.core.CoreModule
import ru.urfu.rickandmorty.core.local.UserProfile

class ProfileViewModel : ViewModel() {
    private val prefsRepo = CoreModule.prefsRepository

    val profile: StateFlow<UserProfile> = prefsRepo.userProfileFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )
}
