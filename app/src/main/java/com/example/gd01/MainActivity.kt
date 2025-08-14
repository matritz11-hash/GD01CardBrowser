package com.example.gd01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CardBrowserApp()
            }
        }
    }
}

@Composable
fun CardBrowserApp() {
    var query by remember { mutableStateOf("") }
    val cards = listOf("Gundam RX-78", "Zaku II", "Wing Gundam", "Strike Freedom")
    val filtered = cards.filter { it.contains(query, ignoreCase = true) }

    Column(Modifier.padding(16.dp)) {
        BasicTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(Modifier.height(8.dp))
        filtered.forEach {
            Text(it, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    CardBrowserApp()
}
