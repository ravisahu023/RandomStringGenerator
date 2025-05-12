package com.example.randomstringgenerator.domain.model

data class RandomStringItem(
    val value: String,
    val length: Int,
    val created: String,
    var isFavourite : Boolean = false
)

