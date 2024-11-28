package com.example.educationapp.ui.study

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.educationapp.model.Quiz

@Composable
fun StudyScreen(
    viewModel: StudyViewModel = hiltViewModel(),
    onStartQuiz: (Quiz) -> Unit
) {
    val quizzes by viewModel.quizzes.collectAsStateWithLifecycle()
    
    // Rest of your existing UI code, but use quizzes from viewModel
    // instead of hardcoded data
} 