package com.example.educationapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.educationapp.model.Document

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetailScreen(
    document: Document,
    onGenerateQuiz: () -> Unit,
    onGenerateFlashcards: () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                // Home tab
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true, // You'll want to make this dynamic later
                    onClick = { /* TODO: Navigate to Home */ }
                )
                
                // Study tab
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Star, contentDescription = "Study") },
                    label = { Text("Study") },
                    selected = false,
                    onClick = { /* TODO: Navigate to Study */ }
                )
                
                // FAB for upload
                NavigationBarItem(
                    icon = { 
                        FloatingActionButton(
                            onClick = { /* TODO: Handle upload */ },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Upload")
                        }
                    },
                    selected = false,
                    onClick = { /* TODO: Handle upload */ },
                    label = { Text("Upload") }
                )
                
                // Library tab
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "Library") },
                    label = { Text("Library") },
                    selected = false,
                    onClick = { /* TODO: Navigate to Library */ }
                )
                
                // Profile tab
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { /* TODO: Navigate to Profile */ }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = document.title,
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Summary",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = document.summary ?: "No summary available",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onGenerateQuiz) {
                    Text("Generate Quiz")
                }
                Button(onClick = onGenerateFlashcards) {
                    Text("Generate Flashcards")
                }
            }
        }
    }
} 