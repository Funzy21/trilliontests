package com.trilliontests.presentation.ui.notifications

import androidx.lifecycle.ViewModel
import com.trilliontests.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList()
)

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    fun markNotificationAsRead(notificationId: String) {
        val updatedNotifications = _uiState.value.notifications.map { notification ->
            if (notification.id == notificationId) {
                notification.copy(isRead = true)
            } else {
                notification
            }
        }
        _uiState.value = _uiState.value.copy(notifications = updatedNotifications)
    }

    fun clearAllNotifications() {
        _uiState.value = _uiState.value.copy(notifications = emptyList())
    }
} 