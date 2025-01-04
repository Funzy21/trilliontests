package com.example.trilliontests.presentation.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trilliontests.presentation.ui.theme.AppIcons
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Composable
fun LoginStreakCard(
    streakDays: Int,
    completedDays: List<Boolean>,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val today = remember { LocalDate.now() }
    val weekDates = remember {
        (-3..3).map { today.plusDays(it.toLong()) }
    }

    Card(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Header with streak info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = AppIcons.streak(),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxSize()
                        )
                    }
                    Column {
                        Text(
                            text = "$streakDays-week streak",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Study in the next 7 days to keep your streak going!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Week days row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDates.forEach { date ->
                    DayColumn(
                        date = date,
                        isToday = date == today,
                        isCompleted = completedDays.getOrNull(date.dayOfMonth - 1) ?: false
                    )
                }
            }

            if (isExpanded) {
                // TODO: Add monthly calendar view when expanded
                Spacer(modifier = Modifier.height(16.dp))
                MonthlyCalendarView(
                    today = today,
                    completedDays = completedDays
                )
            }
        }
    }
}

@Composable
private fun DayColumn(
    date: LocalDate,
    isToday: Boolean,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = when {
                isToday -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier
                .clip(CircleShape)
                .let {
                    if (isToday) {
                        it.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    } else {
                        it
                    }
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        
        if (isCompleted) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(8.dp)
            ) { }
        }
    }
}

@Composable
private fun MonthlyCalendarView(
    today: LocalDate,
    completedDays: List<Boolean>,
    modifier: Modifier = Modifier
) {
    // TODO: Implement monthly calendar view
    // This will be implemented in the next iteration
    Text(
        text = "Monthly view coming soon...",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
} 