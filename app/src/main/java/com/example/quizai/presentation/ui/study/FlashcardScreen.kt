package com.example.quizai.presentation.ui.study

import androidx.compose.animation.core.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quizai.model.Flashcard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashcardScreen(
    flashcards: List<Flashcard>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { flashcards.size })
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress text
        Text(
            text = "${pagerState.currentPage + 1} / ${flashcards.size}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Progress bar
        LinearProgressIndicator(
            progress = (pagerState.currentPage + 1).toFloat() / flashcards.size,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Flashcard pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            FlashcardItem(
                flashcard = flashcards[page],
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun FlashcardItem(
    flashcard: Flashcard,
    modifier: Modifier = Modifier
) {
    var showAnswer by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Word card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(horizontal = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = flashcard.front,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        IconButton(
                            onClick = { showAnswer = !showAnswer }
                        ) {
                            Icon(
                                imageVector = if (showAnswer) 
                                    Icons.Default.KeyboardArrowUp 
                                else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (showAnswer) "Hide answer" else "Show answer",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
            
            // Answer card
            AnimatedVisibility(
                visible = showAnswer,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300, easing = EaseOutQuad)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300, easing = EaseInQuad)
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = flashcard.back,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}
