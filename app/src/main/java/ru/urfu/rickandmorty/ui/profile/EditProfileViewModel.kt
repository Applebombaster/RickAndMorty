package ru.urfu.rickandmorty.ui.profile

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.urfu.rickandmorty.data.NetworkModule
import ru.urfu.rickandmorty.data.local.UserProfile
import ru.urfu.rickandmorty.receiver.ReminderReceiver
import java.util.Calendar

data class EditProfileUiState(
    val fullName: String = "",
    val jobTitle: String = "",
    val resumeUrl: String = "",
    val avatarUri: String = "",
    val favoriteTime: String = "",
    val isTimeError: Boolean = false,
    val isSaveEnabled: Boolean = true
)

class EditProfileViewModel : ViewModel() {
    private val prefsRepo = NetworkModule.prefsRepository

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            prefsRepo.userProfileFlow.collect { savedProfile ->
                _uiState.value = EditProfileUiState(
                    fullName = savedProfile.fullName,
                    jobTitle = savedProfile.jobTitle,
                    resumeUrl = savedProfile.resumeUrl,
                    avatarUri = savedProfile.avatarUri,
                    favoriteTime = savedProfile.favoriteTime
                )
            }
        }
    }

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(fullName = name)
    }

    fun onJobChanged(job: String) {
        _uiState.value = _uiState.value.copy(jobTitle = job)
    }

    fun onResumeChanged(url: String) {
        _uiState.value = _uiState.value.copy(resumeUrl = url)
    }

    fun onAvatarChanged(uri: String) {
        _uiState.value = _uiState.value.copy(avatarUri = uri)
    }

    fun onTimeChanged(time: String) {
        val isValid = validateTime(time)
        _uiState.value = _uiState.value.copy(
            favoriteTime = time,
            isTimeError = time.isNotEmpty() && !isValid,
            isSaveEnabled = time.isEmpty() || isValid
        )
    }

    private fun validateTime(time: String): Boolean {
        return Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$").matches(time)
    }

    fun saveProfile(context: Context, onSaved: () -> Unit) {
        val state = _uiState.value
        if (state.favoriteTime.isNotEmpty() && !validateTime(state.favoriteTime)) return

        viewModelScope.launch {
            val profile = UserProfile(
                fullName = state.fullName,
                jobTitle = state.jobTitle,
                resumeUrl = state.resumeUrl,
                avatarUri = state.avatarUri,
                favoriteTime = state.favoriteTime
            )
            prefsRepo.saveUserProfile(profile)

            if (state.favoriteTime.isNotEmpty()) {
                scheduleAlarm(context, state.favoriteTime, state.fullName.ifEmpty { "Студент" })
            }
            onSaved()
        }
    }

    private fun scheduleAlarm(context: Context, time: String, name: String) {
        val parts = time.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("STUDENT_NAME", name)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }

            Toast.makeText(context, "Будильник установлен на $time", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка будильника: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}