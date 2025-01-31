package com.trilliontests.model

import java.time.LocalDateTime

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.GENERAL
)

enum class NotificationType {
    GENERAL,
    ACHIEVEMENT,
    REMINDER,
    SYSTEM
} 