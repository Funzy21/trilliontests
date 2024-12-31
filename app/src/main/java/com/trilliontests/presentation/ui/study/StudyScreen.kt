package com.trilliontests.presentation.ui.study

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
import com.trilliontests.model.Quiz
import com.trilliontests.model.FlashcardSet
import com.trilliontests.model.QuestionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    viewModel: StudyViewModel = hiltViewModel(),
    onStartQuiz: (Quiz) -> Unit = {},
    onStartFlashcards: (FlashcardSet) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Quizzes", "Flashcards")
    
    Column(modifier = Modifier.fillMaxSize()) {
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
        
        when (selectedTab) {
            0 -> QuizList(
                onStartQuiz = onStartQuiz,
                viewModel = viewModel
            )
            1 -> FlashcardSetList(
                onStartFlashcards = onStartFlashcards,
                viewModel = viewModel
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

@Composable
private fun FlashcardSetList(
    onStartFlashcards: (FlashcardSet) -> Unit,
    viewModel: StudyViewModel
) {
    val flashcardSets by viewModel.flashcardSets.collectAsStateWithLifecycle()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(flashcardSets) { set ->
            FlashcardSetCard(
                flashcardSet = set,
                onStartFlashcards = onStartFlashcards
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
    val highScore by viewModel.getQuizHighScore(quiz.id.toString()).collectAsStateWithLifecycle()

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
                    text = "${quiz.topSubject} - ${quiz.subject}",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Level ${quiz.difficulty} ${quiz.testType}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${quiz.numberOfQuestions} questions",
                        style = MaterialTheme.typography.bodySmall
                    )
                    // Add quiz type chip
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = getQuizTypeLabel(quiz),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
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
                    text = highScore?.let { "$it/${quiz.numberOfQuestions}" } ?: "-",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

private fun getQuizTypeLabel(quiz: Quiz): String {
    return when {
        quiz.questionSet.any { it.type == QuestionType.MATCHING } -> "Matching"
        quiz.questionSet.all { it.type == QuestionType.TRUE_FALSE } -> "True/False"
        quiz.questionSet.all { it.type == QuestionType.MULTIPLE_CHOICE } -> "Multiple Choice"
        else -> quiz.testType
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlashcardSetCard(
    flashcardSet: FlashcardSet,
    onStartFlashcards: (FlashcardSet) -> Unit
) {
    Card(
        onClick = { onStartFlashcards(flashcardSet) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = flashcardSet.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = flashcardSet.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${flashcardSet.cardCount} cards",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
} 