package com.example.gd01.data

import android.content.Context
import com.example.gd01.model.Card
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class CardRepository(private val context: Context) {
    private val json by lazy { Json { ignoreUnknownKeys = true } }

    fun loadCards(): List<Card> {
        return try {
            val content = context.assets.open("cards_gd01.json").bufferedReader().use { it.readText() }
            json.decodeFromString(content)
        } catch (e: Exception) {
            demoCards()
        }
    }

    private fun demoCards(): List<Card> = listOf(
        Card("GD01-001", "RX-78-2 Gundam", "Mobile Suit", "R", number = 1,
            text = "Le mobile suit emblématique."),
        Card("GD01-002", "Amuro Ray", "Pilote", "U", number = 2),
        Card("GD01-003", "Beam Saber", "Option", "C", number = 3)
    )
}
