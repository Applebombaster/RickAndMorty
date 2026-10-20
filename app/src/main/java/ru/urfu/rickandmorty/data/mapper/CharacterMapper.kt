package ru.urfu.rickandmorty.data.mapper

import ru.urfu.rickandmorty.data.remote.dto.CharacterDto
import ru.urfu.rickandmorty.domain.model.Character

fun CharacterDto.toDomain(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    origin = origin.name,
    location = location.name,
    imageUrl = imageUrl
)