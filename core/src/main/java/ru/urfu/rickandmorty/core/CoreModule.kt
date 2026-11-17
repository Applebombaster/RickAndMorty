package ru.urfu.rickandmorty.core

import android.content.Context
import ru.urfu.rickandmorty.core.local.UserPreferencesRepository

object CoreModule {
    lateinit var prefsRepository: UserPreferencesRepository
        private set

    fun init(context: Context) {
        prefsRepository = UserPreferencesRepository(context.applicationContext)
    }
}