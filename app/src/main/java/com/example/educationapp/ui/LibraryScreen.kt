package com.example.educationapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.educationapp.model.Document
import com.example.educationapp.model.Folder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onDocumentClick: (Document) -> Unit = {},
    onFolderClick: (Folder) -> Unit = {}
) {
    var showNewFolderDialog by remember { mutableStateOf(false) }
    var currentPath by remember { mutableStateOf<Folder?>(null) }
    
    // Placeholder data - replace with actual data from a repository
    val folders = remember {
        listOf(
            Folder("1", "Mathematics"),
            Folder("2", "Physics"),
            Folder("3", "Literature")
        )
    }
    
    val documents = remember {
        listOf(
            Document("1", "Calculus Notes", "content", "Summary"),
            Document("2", "Physics Formulas", "content", "Summary"),
            Document("3", "Shakespeare Analysis", "content", "Summary")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar with navigation and actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentPath != null) {
                IconButton(onClick = { currentPath = null }) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            }
            Text(
                text = currentPath?.name ?: "Library",
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = { showNewFolderDialog = true }) {
                Icon(Icons.Filled.Add, "Create new folder")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid of folders and documents
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Show folders only in root
            if (currentPath == null) {
                items(folders) { folder ->
                    FolderItem(
                        folder = folder,
                        onClick = { onFolderClick(folder) }
                    )
                }
            }

            // Show documents
            items(documents) { document ->
                DocumentItem(
                    document = document,
                    onClick = { onDocumentClick(document) }
                )
            }
        }
    }

    if (showNewFolderDialog) {
        NewFolderDialog(
            onDismiss = { showNewFolderDialog = false },
            onConfirm = { folderName ->
                // TODO: Create new folder
                showNewFolderDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderItem(
    folder: Folder,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.AddCircle,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = folder.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DocumentItem(
    document: Document,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.Face,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = document.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun NewFolderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var folderName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Folder") },
        text = {
            TextField(
                value = folderName,
                onValueChange = { folderName = it },
                label = { Text("Folder Name") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(folderName) },
                enabled = folderName.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 