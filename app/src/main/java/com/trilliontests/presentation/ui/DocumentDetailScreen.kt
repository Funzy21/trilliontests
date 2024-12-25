package com.trilliontests.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trilliontests.model.Document

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