package ru.urfu.rickandmorty.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val studentName = intent.getStringExtra("STUDENT_NAME") ?: "Студент"
        NotificationHelper.showNotification(context, studentName)
    }
}