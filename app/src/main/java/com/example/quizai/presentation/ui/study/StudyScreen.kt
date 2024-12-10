package com.example.quizai.presentation.ui.study

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quizai.model.Quiz

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    viewModel: StudyViewModel = hiltViewModel(),
    onStartQuiz: (Quiz) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Quizzes", "Flashcards")
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        
        // Content
        when (selectedTab) {
            0 -> QuizList(
                onStartQuiz = onStartQuiz,
                viewModel = viewModel
            )
            1 -> FlashcardScreen(
                flashcards = listOf(
                    Flashcard(
                        id = "1",
                        front = "Ephemeral",
                        back = "Lasting for a very short time; short-lived or temporary"
                    ),
                    Flashcard(
                        id = "2",
                        front = "Ubiquitous",
                        back = "Present, appearing, or found everywhere"
                    ),
                    Flashcard(
                        id = "3",
                        front = "Surreptitious",
                        back = "Kept secret, especially because it would not be approved of; secretive or stealthy"
                    ),
                    Flashcard(
                        id = "4",
                        front = "Perfunctory",
                        back = "Carried out with minimal effort or reflection; done merely as a duty"
                    ),
                    Flashcard(
                        id = "5",
                        front = "Mellifluous",
                        back = "Sweet or musical; pleasant to hear"
                    )
                )
            )
        }
    }
}

@Composable
private fun QuizList(
    onStartQuiz: (Quiz) -> Unit,
    viewModel: StudyViewModel
) {
    val quizzes by viewModel.quizzes.collectAsStateWithLifecycle()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(quizzes) { quiz ->
            QuizCard(
                quiz = quiz,
                onStartQuiz = onStartQuiz,
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizCard(
    quiz: Quiz,
    onStartQuiz: (Quiz) -> Unit,
    viewModel: StudyViewModel
) {
    val highScore by viewModel.getQuizHighScore(quiz.id).collectAsStateWithLifecycle()

    Card(
        onClick = { onStartQuiz(quiz) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = quiz.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = quiz.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${quiz.questionCount} questions • ${quiz.subject}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = "High Score",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = highScore?.let { "$it/${quiz.questionCount}" } ?: "-",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
} 