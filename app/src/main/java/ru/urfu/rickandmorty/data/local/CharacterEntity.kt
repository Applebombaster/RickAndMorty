package ru.urfu.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.urfu.rickandmorty.domain.model.Character

@Entity(tableName = "favorite_characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val origin: String,
    val location: String,
    val imageUrl: String
)

// Функции для перевода из БД в обычный объект и обратно
fun CharacterEntity.toDomain(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    origin = origin,
    location = location,
    imageUrl = imageUrl
)

fun Character.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    origin = origin,
    location = location,
    imageUrl = imageUrl
)