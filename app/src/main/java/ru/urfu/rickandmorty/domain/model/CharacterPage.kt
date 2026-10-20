package ru.urfu.rickandmorty.domain.model

data class CharacterPage(
    val characters: List<Character>,
    val currentPage: Int,
    val totalPages: Int,
    val hasNextPage: Boolean
)