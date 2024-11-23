package com.example.educationapp.model

data class Document(
    val id: String,
    val title: String,
    val content: String,
    val summary: String? = null
)

data class GeneratedQuiz(
    val id: String,
    val documentId: String,
    val questions: List<Question>
)

data class Question(
    val id: String,  /**/
    val question: String,
    val correctAnswer: String,
    val options: List<String>
)

data class Flashcard(
    val id: String,
    val documentId: String,
    val front: String,
    val back: String
) 