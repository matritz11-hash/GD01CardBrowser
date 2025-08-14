package com.example.gd01.model

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val id: String,
    val name: String,
    val type: String,
    val rarity: String? = null,
    val setCode: String = "GD01",
    val number: Int? = null,
    val imageUrl: String? = null,
    val text: String? = null
)
