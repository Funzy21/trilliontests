package com.trilliontests.presentation.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.trilliontests.model.QuizQuestion

@Composable
fun MatchingQuestionView(
    question: QuizQuestion,
    onAnswerSubmitted: (String) -> Unit
) {
    var selectedItemIndex by remember { mutableStateOf<String?>(null) }
    var matches by remember { mutableStateOf(question.choices.keys.associateWith { "" }) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left column (options/keys)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                question.choices.keys.forEach { key ->
                    MatchingItem(
                        text = key,
                        isSelected = selectedItemIndex == key,
                        onClick = {
                            selectedItemIndex = if (selectedItemIndex == key) null else key
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Right column (values)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                question.choices.values.forEach { value ->
                    MatchingItem(
                        text = value,
                        isSelected = matches.containsValue(value) && selectedItemIndex != null,
                        onClick = {
                            selectedItemIndex?.let { key ->
                                matches = matches.toMutableMap().apply {
                                    this[key] = value
                                }
                                selectedItemIndex = null
                                
                                // Check if all matches are made
                                if (matches.values.none { it.isEmpty() }) {
                                    val answer = matches.entries.joinToString(",") { "${it.key},${it.value}" }
                                    onAnswerSubmitted(answer)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchingItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surface
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
} 