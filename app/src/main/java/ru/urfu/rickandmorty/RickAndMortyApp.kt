package ru.urfu.rickandmorty

import android.app.Application
import ru.urfu.rickandmorty.data.NetworkModule

class RickAndMortyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkModule.init(this)
    }
}