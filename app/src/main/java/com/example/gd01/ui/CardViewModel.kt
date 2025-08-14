package com.example.gd01.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gd01.data.CardRepository
import com.example.gd01.model.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val allCards: List<Card> = emptyList(),
    val query: String = "",
    val typeFilter: String = "Tous",
    val types: List<String> = listOf("Tous")
) {
    val filtered: List<Card>
        get() {
            val byType = if (typeFilter == "Tous") allCards else allCards.filter { it.type.equals(typeFilter, true) }
            val q = query.trim().lowercase()
            if (q.isEmpty()) return byType
            return byType.filter {
                it.name.lowercase().contains(q) || it.id.lowercase().contains(q) || (it.text?.lowercase()?.contains(q) == true)
            }
        }
}

class CardViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = CardRepository(app)
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init { load() }

    private fun load() = viewModelScope.launch {
        val cards = repo.loadCards()
        val types = buildList {
            add("Tous")
            addAll(cards.map { it.type }.distinct().sorted())
        }
        _state.update { it.copy(allCards = cards, types = types) }
    }

    fun onQueryChange(q: String) { _state.update { it.copy(query = q) } }
    fun onTypeChange(t: String) { _state.update { it.copy(typeFilter = t) } }
}
