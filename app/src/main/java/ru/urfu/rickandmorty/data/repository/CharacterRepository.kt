package ru.urfu.rickandmorty.data.repository

import ru.urfu.rickandmorty.data.model.Character

object CharacterRepository {

    private val characters = listOf(
        Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = "Earth (C-137)",
            location = "Citadel of Ricks",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        ),
        Character(
            id = 2,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = "unknown",
            location = "Citadel of Ricks",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        Character(
            id = 3,
            name = "Summer Smith",
            status = "Alive",
            species = "Human",
            gender = "Female",
            origin = "Earth (Replacement Dimension)",
            location = "Earth (Replacement Dimension)",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/3.jpeg"
        ),
        Character(
            id = 4,
            name = "Beth Smith",
            status = "Alive",
            species = "Human",
            gender = "Female",
            origin = "Earth (Replacement Dimension)",
            location = "Earth (Replacement Dimension)",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/4.jpeg"
        ),
        Character(
            id = 5,
            name = "Jerry Smith",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = "Earth (Replacement Dimension)",
            location = "Earth (Replacement Dimension)",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/5.jpeg"
        ),
        Character(
            id = 6,
            name = "Abadango Cluster Princess",
            status = "Alive",
            species = "Alien",
            gender = "Female",
            origin = "Abadango",
            location = "Abadango",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/6.jpeg"
        ),
        Character(
            id = 7,
            name = "Abradolf Lincler",
            status = "unknown",
            species = "Human",
            gender = "Male",
            origin = "Earth (Replacement Dimension)",
            location = "Testicle Monster Dimension",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/7.jpeg"
        ),
        Character(
            id = 8,
            name = "Adjudicator Rick",
            status = "Dead",
            species = "Human",
            gender = "Male",
            origin = "unknown",
            location = "Citadel of Ricks",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/8.jpeg"
        ),
        Character(
            id = 9,
            name = "Agency Director",
            status = "Dead",
            species = "Human",
            gender = "Male",
            origin = "Earth (Replacement Dimension)",
            location = "Earth (Replacement Dimension)",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/9.jpeg"
        ),
        Character(
            id = 10,
            name = "Alan Rails",
            status = "Dead",
            species = "Human",
            gender = "Male",
            origin = "unknown",
            location = "Worldender's lair",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/10.jpeg"
        ),
        Character(
            id = 11,
            name = "Albert Einstein",
            status = "Dead",
            species = "Human",
            gender = "Male",
            origin = "Earth (C-137)",
            location = "Earth (C-137)",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/11.jpeg"
        ),
        Character(
            id = 12,
            name = "Alexander",
            status = "Dead",
            species = "Human",
            gender = "Male",
            origin = "Earth (C-137)",
            location = "Anatomy Park",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/12.jpeg"
        ),
        Character(
            id = 13,
            name = "Alien Googah",
            status = "unknown",
            species = "Alien",
            gender = "unknown",
            origin = "unknown",
            location = "Earth (Replacement Dimension)",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/13.jpeg"
        ),
        Character(
            id = 14,
            name = "Alien Morty",
            status = "unknown",
            species = "Alien",
            gender = "Male",
            origin = "unknown",
            location = "Citadel of Ricks",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/14.jpeg"
        ),
        Character(
            id = 15,
            name = "Alien Rick",
            status = "unknown",
            species = "Alien",
            gender = "Male",
            origin = "unknown",
            location = "Citadel of Ricks",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/15.jpeg"
        )
    )

    /** Вернуть всех персонажей — для списка. */
    fun getCharacters(): List<Character> = characters

    /** Найти персонажа по id — для экрана деталей. */
    fun getCharacterById(id: Int): Character? = characters.find { it.id == id }
}