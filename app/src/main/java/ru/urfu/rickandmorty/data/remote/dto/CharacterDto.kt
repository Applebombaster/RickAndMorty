package ru.urfu.rickandmorty.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val origin: LocationDto,
    val location: LocationDto,
    @SerializedName("image") val imageUrl: String
)

data class LocationDto(
    val name: String,
    val url: String
)