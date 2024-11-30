package com.example.educationapp.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.educationapp.model.Quiz
import com.example.educationapp.model.QuizQuestion
import com.example.educationapp.model.QuizResult
import com.example.educationapp.ui.study.StudyViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    quiz: Quiz,
    onQuizComplete: (QuizResult) -> Unit,
    onExit: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {
    var currentQuestionIndex by remember { mutableStateOf(-1) } // -1 for countdown
    var quizResult by remember { mutableStateOf(QuizResult(quiz.questions.size, 0)) }
    var questionsHistory by remember { mutableStateOf(mutableListOf<Boolean>()) }
    
    when {
        currentQuestionIndex == -1 -> {
            CountdownScreen { currentQuestionIndex = 0 }
        }
        currentQuestionIndex < quiz.questions.size -> {
            QuestionScreen(
                question = quiz.questions[currentQuestionIndex],
                questionsHistory = questionsHistory.takeLast(5),
                currentQuestionIndex = currentQuestionIndex,
                totalQuestions = quiz.questions.size,
                onAnswerSelected = { isCorrect ->
                    questionsHistory.add(isCorrect)
                    if (isCorrect) {
                        quizResult = quizResult.copy(correctAnswers = quizResult.correctAnswers + 1)
                    }
                    currentQuestionIndex++
                }
            )
        }
        else -> {
            QuizCompleteScreen(
                result = quizResult,
                onRetry = {
                    currentQuestionIndex = -1
                    quizResult = QuizResult(quiz.questions.size, 0)
                    questionsHistory.clear()
                },
                onExit = {
                    // Save high score before exiting
                    viewModel.updateHighScore(
                        quizId = quiz.id,
                        score = quizResult.correctAnswers,
                        totalQuestions = quizResult.totalQuestions
                    )
                    onExit()
                }
            )
        }
    }
}

@Composable
private fun CountdownScreen(onCountdownComplete: () -> Unit) {
    var countdown by remember { mutableStateOf(3) }
    
    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
        onCountdownComplete()
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = countdown.toString(),
            style = MaterialTheme.typography.displayLarge
        )
    }
}

@Composable
private fun QuestionScreen(
    question: QuizQuestion,
    questionsHistory: List<Boolean>,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    onAnswerSelected: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Question progress
        Text(
            text = "Question ${currentQuestionIndex + 1} of $totalQuestions",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Question history indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            questionsHistory.forEach { isCorrect ->
                Icon(
                    imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = if (isCorrect) "Correct" else "Incorrect",
                    tint = if (isCorrect) MaterialTheme.colorScheme.primary 
                          else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Answer options in a grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(question.options) { option ->
                AnswerCard(
                    answer = option,
                    onClick = {
                        onAnswerSelected(option == question.correctAnswer)
                    }
                )
            }
        }
    }
}

@Composable
private fun AnswerCard(
    answer: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = answer,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun QuizCompleteScreen(
    result: QuizResult,
    onRetry: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Congratulations!",
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "You completed the quiz",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Score: ${result.correctAnswers}/${result.totalQuestions}",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Text(
            text = "Percentage: ${(result.correctAnswers.toFloat() / result.totalQuestions * 100).toInt()}%",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(onClick = onExit) {
                Text("Exit")
            }
            
            Button(onClick = onRetry) {
                Text("Try Again")
            }
        }
    }
} 