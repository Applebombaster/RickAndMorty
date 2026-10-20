package ru.urfu.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.urfu.rickandmorty.ui.navigation.RickAndMortyApp
import ru.urfu.rickandmorty.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                RickAndMortyApp()
            }
        }
    }
}