package com.example.gd01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.gd01.model.Card
import com.example.gd01.ui.CardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(vm: CardViewModel = viewModel()) {
    val state by vm.state.collectAsState()

    MaterialTheme(colorScheme = lightColorScheme()) {
        Scaffold(topBar = {
            TopAppBar(title = { Text("Cartes Gundam – GD01") })
        }) { padding ->
            Column(Modifier.padding(padding).fillMaxSize().padding(12.dp)) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = vm::onQueryChange,
                    label = { Text("Rechercher par nom/ID/texte…") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                TypeFilterDropdown(
                    types = state.types,
                    current = state.typeFilter,
                    onSelected = vm::onTypeChange
                )
                Spacer(Modifier.height(12.dp))
                CardList(state.filtered)
            }
        }
    }
}

@Composable
fun TypeFilterDropdown(types: List<String>, current: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            readOnly = true,
            value = current,
            onValueChange = {},
            label = { Text("Type de carte") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            types.forEach { type ->
                DropdownMenuItem(text = { Text(type) }, onClick = {
                    onSelected(type)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun CardList(cards: List<Card>) {
    if (cards.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aucune carte trouvée.", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(cards, key = { it.id }) { c -> CardRow(c) }
    }
}

@Composable
fun CardRow(card: Card) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            val painter = rememberAsyncImagePainter(card.imageUrl)
            Image(
                painter = painter,
                contentDescription = card.name,
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(card.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(card.id + " · " + card.type, style = MaterialTheme.typography.bodyMedium)
                if (!card.rarity.isNullOrBlank()) {
                    AssistChip(onClick = {}, label = { Text(card.rarity!!) })
                }
            }
        }
    }
}
