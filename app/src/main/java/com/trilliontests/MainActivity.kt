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
import androidx.compose.ui.Alignment
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
import com.trilliontests.presentation.ui.home.HomeScreen
import com.trilliontests.presentation.ui.LibraryScreen
import com.trilliontests.presentation.ui.ProfileScreen
import com.trilliontests.presentation.ui.theme.EducationAppTheme
import com.trilliontests.presentation.ui.quiz.QuizScreen
import com.trilliontests.presentation.ui.study.StudyScreen
import com.trilliontests.presentation.ui.study.StudyViewModel
import com.trilliontests.presentation.ui.auth.AuthViewModel
import com.trilliontests.presentation.ui.auth.SignInScreen
import com.trilliontests.presentation.ui.study.FlashcardScreen
import com.trilliontests.presentation.ui.theme.AppIcons
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.trilliontests.presentation.ui.onboarding.OnboardingViewModel
import com.trilliontests.presentation.ui.onboarding.OnboardingScreen
import com.trilliontests.presentation.ui.auth.AuthState
import com.trilliontests.presentation.ui.help.HelpScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import com.trilliontests.presentation.ui.notifications.NotificationsScreen

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val navItems = listOf("home", "profile", "study", "help")

    // Extension function that handles the animation for switching between navigation items
    private fun AnimatedContentTransitionScope<NavBackStackEntry>.getEnterTransition(): EnterTransition {
        val currentIndex = navItems.indexOf(initialState.destination.route)
        val targetIndex = navItems.indexOf(targetState.destination.route)
        return when {
            targetIndex > currentIndex -> slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
            targetIndex < currentIndex -> slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
            else -> fadeIn(animationSpec = tween(300))
        }
    }

    // Extension function that handles the animation for switching between navigation items
    private fun AnimatedContentTransitionScope<NavBackStackEntry>.getExitTransition(): ExitTransition {
        val currentIndex = navItems.indexOf(initialState.destination.route)
        val targetIndex = navItems.indexOf(targetState.destination.route)
        return when {
            targetIndex > currentIndex -> slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
            targetIndex < currentIndex -> slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
            else -> fadeOut(animationSpec = tween(300))
        }
    }

    @Composable
    private fun BottomNavBar(
        currentRoute: String,
        onNavigate: (String) -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Main navigation bar
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp
            ) {
                NavigationBar(
                    modifier = Modifier.height(56.dp),
                    containerColor = Color.Transparent
                ) {
                    // Home tab
                    NavigationBarItem(
                        icon = {
                            Icon(
                                AppIcons.home(),
                                contentDescription = "Home",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        selected = currentRoute == "home",
                        onClick = { onNavigate("home") },
                        label = null
                    )
                    
                    // Study tab
                    NavigationBarItem(
                        icon = {
                            Icon(
                                AppIcons.subject(),
                                contentDescription = "Study",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        selected = currentRoute == "study",
                        onClick = { onNavigate("study") },
                        label = null
                    )
                    
                    // Empty space for FAB
                    NavigationBarItem(
                        icon = { Box(modifier = Modifier.size(20.dp)) },
                        selected = false,
                        onClick = { },
                        label = null
                    )
                    
                    // Help tab
                    NavigationBarItem(
                        icon = {
                            Icon(
                                AppIcons.help(),
                                contentDescription = "Help",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        selected = currentRoute == "help",
                        onClick = { onNavigate("help") },
                        label = null
                    )
                    
                    // Profile tab
                    NavigationBarItem(
                        icon = {
                            Icon(
                                AppIcons.profile(),
                                contentDescription = "Profile",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        selected = currentRoute == "profile",
                        onClick = { onNavigate("profile") },
                        label = null
                    )
                }
            }

            // Floating Action Button
            FloatingActionButton(
                onClick = { /* TODO: Handle upload */ },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-16).dp)
                    .size(48.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Upload",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

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
                            BottomNavBar(
                                currentRoute = currentRoute,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        popUpTo("home") {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
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
                            modifier = Modifier.padding(paddingValues),
                            enterTransition = { getEnterTransition() },
                            exitTransition = { getExitTransition() },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(300)
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(300)
                                )
                            }
                        ) {
                            composable("auth") {
                                val viewModel: AuthViewModel = hiltViewModel()
                                val authState by viewModel.authState.collectAsState()
                                
                                SignInScreen(
                                    onSignIn = {
                                        navController.navigate("onboarding") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("onboarding") {
                                val viewModel: OnboardingViewModel = hiltViewModel()
                                
                                OnboardingScreen(
                                    onComplete = {
                                        viewModel.completeOnboarding()
                                        navController.navigate("home") {
                                            popUpTo("onboarding") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("home") {
                                HomeScreen(
                                    onNavigateToDocument = { document ->
                                        navController.navigate("document_detail")
                                    },
                                    onNavigateToNotifications = {
                                        navController.navigate("notifications")
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
                            composable("help") {
                                HelpScreen()
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
                            composable("notifications") {
                                NotificationsScreen(
                                    onNavigateBack = {
                                        navController.popBackStack()
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

