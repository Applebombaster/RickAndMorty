package ru.urfu.rickandmorty.data.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,      // "Alive", "Dead", "unknown"
    val species: String,     // "Human", "Alien", ...
    val gender: String,      // "Male", "Female", "unknown"
    val origin: String,      // название планеты, откуда персонаж
    val location: String,    // текущее местоположение
    val imageUrl: String
)