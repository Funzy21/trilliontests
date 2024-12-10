package com.example.quizai.presentation.ui.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizai.data.repository.QuizRepository
import com.example.quizai.model.Quiz
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow

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
        
    fun getQuizHighScore(quizId: String): StateFlow<Int?> = 
        quizRepository.getQuizHighScore(quizId)
            .map { it?.highScore }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
            
    fun updateHighScore(quizId: String, score: Int, totalQuestions: Int) {
        viewModelScope.launch {
            quizRepository.updateHighScore(quizId, score, totalQuestions)
        }
    }
    
    private val _flashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val flashcards: StateFlow<List<Flashcard>> = _flashcards
    
    init {
        // Load sample flashcards
        _flashcards.value = listOf(
            Flashcard(
                id = "1",
                front = "Obvio",
                back = "Obvious",
                pronunciation = "ob.vi.o"
            ),
            Flashcard(
                id = "2",
                front = "Gracias",
                back = "Thank you",
                pronunciation = "gra.sias"
            ),
            // Add more sample flashcards
        )
    }
} 