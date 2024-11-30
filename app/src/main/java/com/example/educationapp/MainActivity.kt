package com.example.educationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.educationapp.model.Document
import com.example.educationapp.model.Quiz
import com.example.educationapp.ui.DocumentDetailScreen
import com.example.educationapp.ui.HomeScreen
import com.example.educationapp.ui.LibraryScreen
import com.example.educationapp.ui.ProfileScreen
import com.example.educationapp.ui.theme.EducationAppTheme
import com.example.educationapp.ui.quiz.QuizScreen
import com.example.educationapp.model.QuizQuestion
import com.example.educationapp.model.QuizResult
import com.example.educationapp.ui.study.StudyScreen
import com.example.educationapp.ui.study.StudyViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EducationAppTheme(dynamicColor = false) {
                val navController = rememberNavController()
                // Track current route
                var currentRoute by remember { mutableStateOf("home") }

                // Update currentRoute when navigation happens
                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        currentRoute = backStackEntry.destination.route ?: "home"
                    }
                }

                val pickDocument = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri ->
                    uri?.let {
                        navController.navigate("document_detail")
                    }
                }

                Scaffold(
                    bottomBar = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            // Top border
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.Black.copy(alpha = 0.2f))
                            )
                            
                            NavigationBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 0.dp
                            ) {
                                // Home tab
                                NavigationBarItem(
                                    icon = { 
                                        Icon(
                                            Icons.Filled.Home, 
                                            contentDescription = "Home",
                                            modifier = Modifier.size(24.dp)
                                        ) 
                                    },
                                    label = { Text("Home") },
                                    selected = currentRoute == "home",
                                    onClick = {
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                )
                                
                                // Study tab
                                NavigationBarItem(
                                    icon = { 
                                        Icon(
                                            Icons.Filled.Star, 
                                            contentDescription = "Study",
                                            modifier = Modifier.size(24.dp)
                                        ) 
                                    },
                                    label = { Text("Study") },
                                    selected = currentRoute == "study",
                                    onClick = {
                                        navController.navigate("study") {
                                            popUpTo("home")
                                        }
                                    }
                                )
                                
                                // Center tab
                                NavigationBarItem(
                                    icon = { 
                                        Icon(
                                            Icons.Filled.Add, 
                                            contentDescription = "Upload",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = { Text("Upload") },
                                    selected = false,
                                    onClick = { pickDocument.launch("application/pdf") }
                                )
                                
                                // Library tab
                                NavigationBarItem(
                                    icon = { 
                                        Icon(
                                            Icons.Filled.Email, 
                                            contentDescription = "Library",
                                            modifier = Modifier.size(24.dp)
                                        ) 
                                    },
                                    label = { Text("Library") },
                                    selected = currentRoute == "library",
                                    onClick = {
                                        navController.navigate("library") {
                                            popUpTo("home")
                                        }
                                    }
                                )
                                
                                // Profile tab
                                NavigationBarItem(
                                    icon = { 
                                        Icon(
                                            Icons.Filled.Person, 
                                            contentDescription = "Profile",
                                            modifier = Modifier.size(24.dp)
                                        ) 
                                    },
                                    label = { Text("Profile") },
                                    selected = currentRoute == "profile",
                                    onClick = {
                                        navController.navigate("profile") {
                                            popUpTo("home")
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable("home") {
                                HomeScreen()
                            }
                            composable("study") {
                                StudyScreen(
                                    onStartQuiz = { quiz ->
                                        navController.navigate("quiz/${quiz.id}")
                                    }
                                )
                            }
                            composable("library") {
                                LibraryScreen(
                                    onDocumentClick = { document ->
                                        navController.navigate("document_detail")
                                    },
                                    onFolderClick = { folder ->
                                        // TODO: Navigate to folder contents
                                    }
                                )
                            }
                            composable("profile") {
                                ProfileScreen()
                            }
                            composable("document_detail") {
                                DocumentDetailScreen(
                                    document = Document(
                                        id = "1",
                                        title = "Sample Document",
                                        content = "Sample content",
                                        summary = "Sample summary"
                                    ),
                                    onGenerateQuiz = { /* TODO */ },
                                    onGenerateFlashcards = { /* TODO */ },
                                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "home",
                                    onNavigate = { route -> navController.navigate(route) }
                                )
                            }
                            composable("quiz/{quizId}") { backStackEntry ->
                                val quizId = backStackEntry.arguments?.getString("quizId")
                                val quizViewModel: StudyViewModel = hiltViewModel()
                                val quiz by quizViewModel.loadQuiz(quizId ?: "").collectAsStateWithLifecycle()

                                quiz?.let { currentQuiz ->
                                    QuizScreen(
                                        quiz = currentQuiz,
                                        onQuizComplete = { result ->
                                            // Handle quiz completion
                                        },
                                        onExit = {
                                            navController.navigateUp()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}