package ru.urfu.rickandmorty.ui.list

import ru.urfu.rickandmorty.domain.model.Character

data class CharacterUi(
    val id: Int,
    val name: String,
    val subtitle: String,
    val location: String,
    val imageUrl: String,
    val status: String,
    val species: String
)

fun Character.toUi(): CharacterUi = CharacterUi(
    id = id,
    name = name,
    subtitle = "$status • $species",
    location = location,
    imageUrl = imageUrl,
    status = status,
    species = species
)

fun CharacterUi.toDomain(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = "",
    origin = "",
    location = location,
    imageUrl = imageUrl
)