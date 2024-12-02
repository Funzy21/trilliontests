package com.example.quizai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_high_scores")
data class QuizHighScoreEntity(
    @PrimaryKey val quizId: String,
    val highScore: Int,
    val totalQuestions: Int,
    val lastUpdated: Long = System.currentTimeMillis()
) 