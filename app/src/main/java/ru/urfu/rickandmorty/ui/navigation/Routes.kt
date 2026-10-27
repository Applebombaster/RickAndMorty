package ru.urfu.rickandmorty.ui.navigation

object Routes {
    const val CHARACTERS = "characters"
    const val FAVORITES = "favorites"
    const val SETTINGS = "settings"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    
    const val CHARACTER_DETAIL = "character/{id}"
    const val ARG_CHARACTER_ID = "id"
    fun characterDetail(id: Int) = "character/$id"
}