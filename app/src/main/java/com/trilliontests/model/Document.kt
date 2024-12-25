package com.trilliontests.model

data class Document(
    val id: String,
    val title: String,
    val content: String,
    val summary: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis(),
    val tags: List<String> = emptyList()
)

//This is for future use; class for the AI generated quiz feature
data class GeneratedQuiz(
    val id: String,
    val documentId: String,
    val questions: List<Question>
)

data class Question(
    val id: String,
    val question: String,
    val correctAnswer: String,
    val options: List<String>
)

data class Flashcard(
    val id: String,
    val front: String,
    val back: String
) 