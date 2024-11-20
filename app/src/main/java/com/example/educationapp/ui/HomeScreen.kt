package com.example.educationapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.educationapp.model.Document

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Homepage",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Mathematics book",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "This is where the document's summary/synopsis will be.",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* TODO */ }) {
                Text("Generate Quiz")
            }
            Button(onClick = { /* TODO */ }) {
                Text("Generate Flashcards")
            }
        }
    }
} 