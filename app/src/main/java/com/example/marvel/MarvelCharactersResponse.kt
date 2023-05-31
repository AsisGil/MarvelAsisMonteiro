package com.example.marvel

import com.example.marvel.entities.MarvelCharacter

data class MarvelCharactersResponse(
    val data: MarvelCharacterData
)

data class MarvelCharacterData(
    val results: List<MarvelCharacter>
)
