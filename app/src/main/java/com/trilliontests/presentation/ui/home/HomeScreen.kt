package com.trilliontests.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trilliontests.model.Document
import com.example.trilliontests.presentation.ui.components.LoginStreakCard
import java.time.LocalDate

// Placeholder data classes
data class Reminder(
    val id: String,
    val title: String,
    val date: LocalDate,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDocument: (Document) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Simple top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TrillionTests",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.clickable { },
                    tint = MaterialTheme.colorScheme.onBackground
                )
                
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.clickable { },
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        
        // Small top navigation row
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavigationItem(
                text = "Subject",
                icon = Icons.Default.Email,
                selected = false,
                onClick = { /* TODO: Navigate to subjects */ }
            )
            NavigationItem(
                text = "Progress",
                icon = Icons.Default.Build,
                selected = false,
                onClick = { /* TODO: Navigate to progress */ }
            )
            NavigationItem(
                text = "Community",
                icon = Icons.Default.Face,
                selected = false,
                onClick = { /* TODO: Navigate to community */ }
            )
        }


        // Placeholder data
        val recentDocuments = remember {
            listOf(
                Document("1", "Mathematics Notes", "Content 1", "Summary of math concepts"),
                Document("2", "Physics Formula", "Content 2", "Important physics formulas"),
                Document("3", "History Essay", "Content 3", "World War II essay"),
                Document("4", "Chemistry Lab", "Content 4", "Lab experiment notes"),
                Document("5", "Literature Review", "Content 5", "Shakespeare analysis")
            )
        }

        val reminders = remember {
            listOf(
                Reminder("1", "Math Exam", LocalDate.now().plusDays(5), "Chapter 1-5"),
                Reminder("2", "Physics Project", LocalDate.now().plusDays(10), "Due next week"),
                Reminder("3", "Study Group", LocalDate.now().plusDays(2), "Review session")
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Login streak card
            LoginStreakCard(
                streakDays = 6,
                completedDays = listOf(true, true, true, true, true, false),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // Recent Activity section
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recentDocuments) { document ->
                    Card(
                        modifier = Modifier
                            .width(200.dp)
                            .height(120.dp),
                        onClick = { /* TODO: Navigate to document */ }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                        ) {
                            Text(
                                text = document.title,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = document.summary ?: "No summary available",
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reminders section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reminders",
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = { /* TODO: Add reminder */ }) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Add reminder")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            reminders.forEach { reminder ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    onClick = { /* TODO: Edit reminder */ }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = reminder.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Due: ${reminder.date}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = reminder.description,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationItem(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (selected) MaterialTheme.colorScheme.primary 
                  else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) MaterialTheme.colorScheme.primary 
                   else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        if (selected) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(2.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(1.dp)
                    )
            )
        }
    }
}