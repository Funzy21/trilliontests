package com.example.quizai.model

data class StudySet(
    val id: String,
    val title: String,
    val subject: String,
    val description: String,
    val cardCount: Int = 0
) 