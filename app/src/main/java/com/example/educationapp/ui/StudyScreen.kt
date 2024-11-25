package com.example.educationapp.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.educationapp.model.Quiz

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    onStartQuiz: (Quiz) -> Unit
) {
    // Placeholder quizzes
    val quizzes = listOf(
        Quiz(
            id = "1",
            title = "8th Grade Math",
            subject = "Mathematics",
            grade = "8th",
            questionCount = 20,
                concepts = listOf(
                "Linear Equations",
                "Pythagorean Theorem",
                "Data Analysis",
                "Basic Geometry"
            ),
            description = "Test your understanding of key 8th grade math concepts including linear equations, geometry, and data analysis."
        ),
        Quiz(
            id = "2",
            title = "9th Grade Math",
            subject = "Mathematics",
            grade = "9th",
            questionCount = 25,
            concepts = listOf(
                "Polynomial Functions",
                "Trigonometry",
                "Quadratic Equations",
                "Coordinate Geometry"
            ),
            description = "Challenge yourself with advanced algebra, trigonometry, and geometry problems suitable for 9th grade students."
        )
        // Add more quizzes as needed
    )

    var selectedQuiz by remember { mutableStateOf<Quiz?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Available Quizzes",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(quizzes) { quiz ->
                QuizCard(
                    quiz = quiz,
                    onClick = { selectedQuiz = quiz }
                )
            }
        }
    }

    // Quiz detail dialog
    selectedQuiz?.let { quiz ->
        QuizDetailDialog(
            quiz = quiz,
            onDismiss = { selectedQuiz = null },
            onStartQuiz = {
                onStartQuiz(quiz)
                selectedQuiz = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizCard(
    quiz: Quiz,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Placeholder for image
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterHorizontally),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = quiz.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${quiz.questionCount} questions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuizDetailDialog(
    quiz: Quiz,
    onDismiss: () -> Unit,
    onStartQuiz: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(16.dp)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = quiz.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = quiz.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Key Concepts:",
                    style = MaterialTheme.typography.titleMedium
                )

                quiz.concepts.forEach { concept ->
                    Text(
                        text = "• $concept",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onStartQuiz,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start Quiz")
                }
            }
        }
    }
} 