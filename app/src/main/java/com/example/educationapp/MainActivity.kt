package com.example.educationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.educationapp.model.Document
import com.example.educationapp.model.Quiz
import com.example.educationapp.ui.DocumentDetailScreen
import com.example.educationapp.ui.HomeScreen
import com.example.educationapp.ui.LibraryScreen
import com.example.educationapp.ui.ProfileScreen
import com.example.educationapp.ui.StudyScreen
import com.example.educationapp.ui.theme.EducationAppTheme
import com.example.educationapp.ui.quiz.QuizScreen
import com.example.educationapp.model.QuizQuestion
import com.example.educationapp.model.QuizResult

@OptIn(ExperimentalMaterial3Api::class)
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
                        Surface(
                            modifier = Modifier.padding(16.dp),
                            shape = RoundedCornerShape(24.dp),
                            tonalElevation = 8.dp
                        ) {
                            NavigationBar(
                                modifier = Modifier.height(64.dp),
                                containerColor = Color.Transparent
                            ) {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                                    label = { Text("Home") },
                                    selected = currentRoute == "home",
                                    onClick = { 
                                        navController.navigate("home") {
                                            // Pop up to the start destination and clear the back stack
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                )
                                
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Star, contentDescription = "Study") },
                                    label = { Text("Study") },
                                    selected = currentRoute == "study",
                                    onClick = { 
                                        navController.navigate("study") {
                                            popUpTo("home")
                                        }
                                    }
                                )
                                
                                NavigationBarItem(
                                    icon = { 
                                        FloatingActionButton(
                                            onClick = { pickDocument.launch("application/pdf") },
                                            modifier = Modifier.size(38.dp),
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ) {
                                            Icon(
                                                Icons.Filled.Add, 
                                                contentDescription = "Upload",
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    },
                                    selected = false,
                                    onClick = { pickDocument.launch("application/pdf") },
                                    label = { Text("Upload") }
                                )
                                
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Email, contentDescription = "Library") },
                                    label = { Text("Library") },
                                    selected = currentRoute == "library",
                                    onClick = { 
                                        navController.navigate("library") {
                                            popUpTo("home")
                                        }
                                    }
                                )
                                
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
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
                                // For now, we'll use dummy data
                                val dummyQuestions = listOf(
                                    QuizQuestion(
                                        id = "1",
                                        question = "What is 2 + 2?",
                                        options = listOf("3", "4", "5", "6"),
                                        correctAnswer = "4"
                                    ),
                                    QuizQuestion(
                                        id = "2",
                                        question = "What is 5 × 5?",
                                        options = listOf("20", "25", "30", "35"),
                                        correctAnswer = "25"
                                    ),
                                    QuizQuestion(
                                        id = "3",
                                        question = "What is 10 ÷ 2?",
                                        options = listOf("3", "4", "5", "6"),
                                        correctAnswer = "5"
                                    ),
                                    QuizQuestion(
                                        id = "4",
                                        question = "What is 15 - 7?",
                                        options = listOf("6", "7", "8", "9"),
                                        correctAnswer = "8"
                                    ),
                                    QuizQuestion(
                                        id = "5",
                                        question = "What is 3 × 4?",
                                        options = listOf("10", "11", "12", "13"),
                                        correctAnswer = "12"
                                    )
                                )
                                
                                QuizScreen(
                                    quiz = Quiz(
                                        id = quizId ?: "1",
                                        title = "Math Quiz",
                                        subject = "Mathematics",
                                        grade = "8th",
                                        questionCount = dummyQuestions.size,
                                        concepts = listOf("Basic Arithmetic"),
                                        description = "Test your math skills",
                                        questions = dummyQuestions
                                    ),
                                    onQuizComplete = { result ->
                                        // Handle quiz completion (e.g., save results)
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