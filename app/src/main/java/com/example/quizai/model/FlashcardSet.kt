package com.example.quizai.model

data class FlashcardSet(
    val id: String,
    val title: String,
    val description: String,
    val cardCount: Int,
    val cards: List<Flashcard>
)
