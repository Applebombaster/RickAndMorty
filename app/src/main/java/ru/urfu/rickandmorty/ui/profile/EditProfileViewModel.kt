package ru.urfu.rickandmorty.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.urfu.rickandmorty.data.NetworkModule
import ru.urfu.rickandmorty.data.local.UserProfile

class EditProfileViewModel : ViewModel() {
    private val prefsRepo = NetworkModule.prefsRepository

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    init {
        viewModelScope.launch {
            prefsRepo.userProfileFlow.collect { savedProfile ->
                _profile.value = savedProfile
            }
        }
    }

    fun onNameChanged(name: String) { _profile.value = _profile.value.copy(fullName = name) }
    fun onJobChanged(job: String) { _profile.value = _profile.value.copy(jobTitle = job) }
    fun onResumeChanged(url: String) { _profile.value = _profile.value.copy(resumeUrl = url) }
    fun onAvatarChanged(uri: String) { _profile.value = _profile.value.copy(avatarUri = uri) }

    fun saveProfile(onSaved: () -> Unit) {
        viewModelScope.launch {
            prefsRepo.saveUserProfile(_profile.value)
            onSaved()
        }
    }
}
