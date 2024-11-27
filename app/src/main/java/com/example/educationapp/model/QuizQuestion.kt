package com.example.educationapp.model

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val questionsHistory: List<Boolean> = emptyList() // true for correct, false for incorrect
) 