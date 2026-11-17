package ru.urfu.rickandmorty.data.remote.dto

data class CharacterListResponseDto(
    val info: PageInfoDto,
    val results: List<CharacterDto>
)

data class PageInfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)