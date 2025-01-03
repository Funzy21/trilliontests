package com.trilliontests.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilliontests.data.repository.QuizRepository
import com.trilliontests.model.Document
import com.trilliontests.model.Quiz
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val recentDocuments: List<Document> = emptyList(),
    val reminders: List<Reminder> = emptyList(),
    val streakDays: Int = 0,
    val completedDays: List<Boolean> = emptyList(),
    val recentQuizzes: List<Quiz> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Load recent quizzes
                quizRepository.getAllQuizzes().collect { quizzes ->
                    _uiState.value = _uiState.value.copy(
                        recentQuizzes = quizzes.take(5), // Show only last 5 quizzes
                        isLoading = false
                    )
                }

                // TODO: Load documents from DocumentRepository
                // TODO: Load reminders from ReminderRepository
                // TODO: Load streak data from UserRepository
                
                // For now, using placeholder data
                _uiState.value = _uiState.value.copy(
                    recentDocuments = listOf(
                        Document("1", "Mathematics Notes", "Content 1", "Summary of math concepts"),
                        Document("2", "Physics Formula", "Content 2", "Important physics formulas"),
                        Document("3", "History Essay", "Content 3", "World War II essay")
                    ),
                    reminders = listOf(
                        Reminder("1", "Math Exam", LocalDate.now().plusDays(5), "Chapter 1-5"),
                        Reminder("2", "Physics Project", LocalDate.now().plusDays(10), "Due next week")
                    ),
                    streakDays = 6,
                    completedDays = listOf(true, true, true, true, true, false)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }

    fun refreshData() {
        loadInitialData()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
} 