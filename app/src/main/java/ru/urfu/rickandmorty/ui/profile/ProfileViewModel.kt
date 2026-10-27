package ru.urfu.rickandmorty.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.urfu.rickandmorty.data.NetworkModule
import ru.urfu.rickandmorty.data.local.UserProfile

class ProfileViewModel : ViewModel() {
    private val prefsRepo = NetworkModule.prefsRepository

    val profile: StateFlow<UserProfile> = prefsRepo.userProfileFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )
}
