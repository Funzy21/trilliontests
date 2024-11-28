package com.example.educationapp.ui.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationapp.data.repository.QuizRepository
import com.example.educationapp.model.Quiz
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {
    val quizzes: StateFlow<List<Quiz>> = quizRepository.getAllQuizzes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
    fun loadQuiz(quizId: String): StateFlow<Quiz?> = quizRepository.getQuiz(quizId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
} 