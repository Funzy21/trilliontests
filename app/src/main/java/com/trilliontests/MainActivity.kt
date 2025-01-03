package com.trilliontests

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trilliontests.model.Document
import com.trilliontests.presentation.ui.DocumentDetailScreen
import com.trilliontests.presentation.ui.HomeScreen
import com.trilliontests.presentation.ui.LibraryScreen
import com.trilliontests.presentation.ui.ProfileScreen
import com.trilliontests.presentation.ui.theme.EducationAppTheme
import com.trilliontests.presentation.ui.quiz.QuizScreen
import com.trilliontests.presentation.ui.study.StudyScreen
import com.trilliontests.presentation.ui.study.StudyViewModel
import com.trilliontests.presentation.ui.auth.AuthViewModel
import com.trilliontests.presentation.ui.auth.SignInScreen
import com.trilliontests.presentation.ui.study.FlashcardScreen
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EducationAppTheme(dynamicColor = false) {
                val navController = rememberNavController()
                var currentRoute by remember { mutableStateOf("auth") }
                val authViewModel: AuthViewModel = hiltViewModel()
                val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()

                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        currentRoute = backStackEntry.destination.route ?: "auth"
                    }
                }

                // Only show bottom navigation when authenticated or in offline mode
                val showBottomNav = currentRoute != "auth"

                Scaffold(
                    bottomBar = {
                        if (showBottomNav) {
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

                                    /* Center tab - SHELVED FOR NOW, WILL BE USED FOR UPLOADING DOCUMENTS
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
                                        onClick = { /* TODO: Handle upload */ }
                                    )
                                    */

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
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "auth",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable("auth") {
                                SignInScreen(
                                    onSignIn = {
                                        // Navigate to home screen after successful sign in
                                        navController.navigate("home") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("home") {
                                HomeScreen()
                            }
                            composable("study") {
                                StudyScreen(
                                    onStartQuiz = { quiz ->
                                        navController.navigate("quiz/${quiz.id}")
                                    },
                                    onStartFlashcards = { set ->
                                        navController.navigate("flashcards/${set.id}")
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
                                ProfileScreen(
                                    onSignOut = {
                                        navController.navigate("auth") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
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
                                val quiz by quizViewModel.loadQuiz(quizId ?: "0").collectAsStateWithLifecycle()

                                quiz?.let { currentQuiz ->
                                    QuizScreen(
                                        quiz = currentQuiz,
                                        onQuizComplete = { /* TODO: Handle quiz completion */ },
                                        onExit = { navController.popBackStack() }
                                    )
                                }
                            }
                            composable("flashcards/{setId}") { backStackEntry ->
                                val setId = backStackEntry.arguments?.getString("setId")
                                val viewModel: StudyViewModel = hiltViewModel()
                                val flashcardSet by viewModel.getFlashcardSet(setId ?: "").collectAsStateWithLifecycle()
                                
                                flashcardSet?.let { set ->
                                    FlashcardScreen(
                                        flashcards = set.cards,
                                        onBack = { navController.popBackStack() }
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