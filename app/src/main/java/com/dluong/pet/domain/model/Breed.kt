package com.dluong.pet.domain.model

data class Breed(
    val countryCode: String,
    val countryCodes: String,
    val id: String,
    val lifeSpan: String,
    val name: String,
    val origin: String,
    val temperament: String,
    val weight: Weight,
    val wikipediaUrl: String
)