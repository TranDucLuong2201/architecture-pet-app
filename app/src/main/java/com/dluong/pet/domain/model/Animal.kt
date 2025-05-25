package com.dluong.pet.domain.model

data class Animal(
    val breeds: List<Breed>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)