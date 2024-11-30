package com.example.educationapp.ui.study

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.educationapp.model.Quiz

@Composable
fun StudyScreen(
    viewModel: StudyViewModel = hiltViewModel(),
    onStartQuiz: (Quiz) -> Unit
) {
    val quizzes by viewModel.quizzes.collectAsStateWithLifecycle()
    
    StudyScreenContent(
        quizzes = quizzes,
        onStartQuiz = onStartQuiz
    )
}

@Composable
private fun StudyScreenContent(
    quizzes: List<Quiz>,
    onStartQuiz: (Quiz) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(quizzes) { quiz ->
            QuizCard(
                quiz = quiz,
                onStartQuiz = onStartQuiz
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizCard(
    quiz: Quiz,
    onStartQuiz: (Quiz) -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
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