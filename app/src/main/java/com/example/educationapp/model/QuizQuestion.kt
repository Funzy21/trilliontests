package com.example.educationapp.model

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String
) 