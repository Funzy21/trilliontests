package com.example.educationapp.model

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val questionsHistory: List<Boolean> = emptyList() // true for correct, false for incorrect
) 