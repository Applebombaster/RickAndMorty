package ru.urfu.rickandmorty.ui.navigation

/**
 * Все маршруты приложения в одном месте — чтобы не искать строки по всему коду.
 */
object Routes {
    const val CHARACTERS = "characters"
    const val FAVORITES = "favorites"
    const val SETTINGS = "settings"

    /** Маршрут деталей. `{id}` — аргумент, передаётся при навигации. */
    const val CHARACTER_DETAIL = "character/{id}"
    const val ARG_CHARACTER_ID = "id"

    /** Хелпер для сборки конкретного маршрута: "character/5". */
    fun characterDetail(id: Int) = "character/$id"
}