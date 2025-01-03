package com.trilliontests.presentation.ui.quiz

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
import com.trilliontests.model.QuestionType
import com.trilliontests.model.Quiz
import com.trilliontests.model.QuizQuestion
import com.trilliontests.model.QuizResult
import com.trilliontests.presentation.ui.study.StudyViewModel
import kotlinx.coroutines.delay
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color

@Composable
fun QuizScreen(
    quiz: Quiz,
    onQuizComplete: (QuizResult) -> Unit,
    onExit: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {
    var currentQuestionIndex by remember { mutableStateOf(-1) } // -1 for countdown
    var quizResult by remember { mutableStateOf(QuizResult(quiz.questionSet.size, 0)) }
    var questionsHistory by remember { mutableStateOf(mutableListOf<Boolean>()) }
    
    when {
        currentQuestionIndex == -1 -> {
            CountdownScreen { currentQuestionIndex = 0 }
        }
        currentQuestionIndex < quiz.questionSet.size -> {
            QuestionScreen(
                question = quiz.questionSet[currentQuestionIndex],
                questionsHistory = questionsHistory.takeLast(5),
                currentQuestionIndex = currentQuestionIndex,
                totalQuestions = quiz.questionSet.size,
                onAnswerSelected = { isCorrect ->
                    questionsHistory.add(isCorrect)
                    if (isCorrect) {
                        quizResult = quizResult.copy(correctAnswers = quizResult.correctAnswers + 1)
                    }
                },
                onNextQuestion = {
                    currentQuestionIndex++
                }
            )
        }
        else -> {
            QuizCompleteScreen(
                result = quizResult,
                onRetry = {
                    currentQuestionIndex = -1
                    quizResult = QuizResult(quiz.questionSet.size, 0)
                    questionsHistory.clear()
                },
                onExit = {
                    // Save high score before exiting
                    viewModel.updateHighScore(
                        quizId = quiz.id.toString(),
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
    onAnswerSelected: (Boolean) -> Unit,
    onNextQuestion: () -> Unit
) {
    var showExplanation by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

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
        
        // Question history indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            questionsHistory.forEach { isCorrect ->
                Icon(
                    imageVector = if (isCorrect)
                        Icons.Default.Check
                    else Icons.Default.Close,
                    contentDescription = if (isCorrect) "Correct" else "Incorrect",
                    tint = if (isCorrect) Color(color = (0xFF34A853))
                          else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        when (question.type) {
            QuestionType.MATCHING -> {
                MatchingQuestionView(
                    question = question,
                    onAnswerSubmitted = { answer ->
                        if (selectedAnswer == null) {
                            selectedAnswer = answer
                            showExplanation = true
                            onAnswerSelected(answer == question.correctAnswer)
                        }
                    }
                )
            }
            else -> {
                // Multiple choice and true/false handling
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(question.choices.entries.toList()) { (key, value) ->
                        AnswerCard(
                            key = key,
                            answer = value,
                            isSelected = key == selectedAnswer,
                            hasAnswered = selectedAnswer != null,
                            correctAnswer = question.correctAnswer,
                            enabled = selectedAnswer == null,
                            onClick = {
                                if (selectedAnswer == null) {
                                    selectedAnswer = key
                                    showExplanation = true
                                    onAnswerSelected(key == question.correctAnswer)
                                }
                            }
                        )
                    }
                }

                AnimatedVisibility(
                    visible = showExplanation,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(300)
                    ) + fadeIn(animationSpec = tween(300)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = question.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Button(
                                    onClick = {
                                        selectedAnswer = null
                                        showExplanation = false
                                        onNextQuestion()
                                    },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Next Question")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerCard(
    key: String,
    answer: String,
    isSelected: Boolean,
    hasAnswered: Boolean,
    correctAnswer: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val isCorrectAnswer = key == correctAnswer
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f),
        enabled = enabled,
        colors = CardDefaults.cardColors(
            containerColor = when {
                hasAnswered && isCorrectAnswer -> Color(0xFFB8F4B8)
                hasAnswered && isSelected && !isCorrectAnswer -> Color(0xFFFFCDD2)
                else -> Color(0xFFE6ECFA)
            }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = key,
                    style = MaterialTheme.typography.labelLarge,
                    color = when {
                        hasAnswered && isCorrectAnswer -> Color(0xFF2E7D32)
                        hasAnswered && isSelected && !isCorrectAnswer -> Color(0xFFB71C1C)
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = answer,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = when {
                        hasAnswered && isCorrectAnswer -> Color(0xFF2E7D32)
                        hasAnswered && isSelected && !isCorrectAnswer -> Color(0xFFB71C1C)
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }
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