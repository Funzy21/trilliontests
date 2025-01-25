package com.trilliontests.presentation.ui.study

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.trilliontests.model.Flashcard
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.material.icons.filled.Clear
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashcardScreen(
    flashcards: List<Flashcard>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    var currentCardIndex by remember { mutableStateOf(0) }
    var masteredCards by remember { mutableStateOf(setOf<String>()) }
    var learningCards by remember { mutableStateOf(setOf<String>()) }
    
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Top bar with close and settings buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = { /* TODO: Settings */ }) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            // Title with progress
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Biology Review",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Mastered: ${masteredCards.size} | Learning: ${learningCards.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Current flashcard
            if (currentCardIndex < flashcards.size) {
                SwipeableFlashcard(
                    flashcard = flashcards[currentCardIndex],
                    onSwipeLeft = {
                        learningCards = learningCards + flashcards[currentCardIndex].id
                        currentCardIndex++
                    },
                    onSwipeRight = {
                        masteredCards = masteredCards + flashcards[currentCardIndex].id
                        currentCardIndex++
                    }
                )
            } else {
                // Show completion screen
                FlashcardCompletionScreen(
                    totalCards = flashcards.size,
                    masteredCount = masteredCards.size,
                    onRestart = {
                        currentCardIndex = 0
                        masteredCards = emptySet()
                        learningCards = emptySet()
                    }
                )
            }

            // Progress indicator
            LinearProgressIndicator(
                progress = currentCardIndex.toFloat() / flashcards.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun SwipeableFlashcard(
    flashcard: Flashcard,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableStateOf(0f) }
    var isFlipped by remember { mutableStateOf(false) }
    
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        label = "card_flip"
    )

    val swipeThreshold = with(LocalDensity.current) { 96.dp.toPx() }
    val alpha = (offsetX.absoluteValue / swipeThreshold).coerceIn(0f, 1f)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Swipe indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Clear,
                contentDescription = "Still Learning",
                tint = Color.Red.copy(alpha = if (offsetX < 0) alpha else 0f),
                modifier = Modifier.size(48.dp)
            )
            Icon(
                Icons.Default.ThumbUp,
                contentDescription = "Mastered",
                tint = Color(0xFF4CAF50).copy(alpha = if (offsetX > 0) alpha else 0f),
                modifier = Modifier.size(48.dp)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .offset(x = animateDpAsState(
                    targetValue = offsetX.dp,
                    animationSpec = spring()
                ).value)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            if (offsetX.absoluteValue >= swipeThreshold) {
                                if (offsetX > 0) onSwipeRight() else onSwipeLeft()
                            }
                            offsetX = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                        }
                    )
                }
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .clickable { isFlipped = !isFlipped },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (rotation <= 90f) {
                    // Front of card
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Question",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = flashcard.front,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                } else {
                    // Back of card
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .graphicsLayer { rotationY = 180f },
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Answer",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = flashcard.back,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FlashcardCompletionScreen(
    totalCards: Int,
    masteredCount: Int,
    onRestart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.ThumbUp,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Great job!",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Text(
            text = "You've mastered $masteredCount out of $totalCards cards",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = onRestart) {
            Text("Practice Again")
        }
    }
}
