package com.example.educationapp.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.educationapp.model.Document

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetailScreen(
    document: Document,
    onGenerateQuiz: () -> Unit,
    onGenerateFlashcards: () -> Unit,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp
            ) {
                NavigationBar(
                    modifier = Modifier.height(64.dp),
                    containerColor = Color.Transparent
                ) {
                    // Home tab
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentRoute == "home",
                        onClick = { onNavigate("home") }
                    )
                    
                    // Study tab
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Star, contentDescription = "Study") },
                        label = { Text("Study") },
                        selected = currentRoute == "study",
                        onClick = { onNavigate("study") }
                    )
                    
                    // FAB for upload
                    NavigationBarItem(
                        icon = { 
                            FloatingActionButton(
                                onClick = { onNavigate("upload") },
                                modifier = Modifier.size(48.dp),
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Upload")
                            }
                        },
                        selected = currentRoute == "upload",
                        onClick = { onNavigate("upload") },
                        label = { Text("Upload") }
                    )
                    
                    // Library tab
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Email, contentDescription = "Library") },
                        label = { Text("Library") },
                        selected = currentRoute == "library",
                        onClick = { onNavigate("library") }
                    )
                    
                    // Profile tab
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        selected = currentRoute == "profile",
                        onClick = { onNavigate("profile") }
                    )
                }
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