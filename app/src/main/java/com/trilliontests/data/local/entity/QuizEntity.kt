package com.trilliontests.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.trilliontests.model.QuestionType

@Entity(tableName = "quizzes")
data class QuizEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subject: String,
    val grade: String,
    val questionCount: Int,
    val concepts: List<String>,
    val description: String
)

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey val id: String,
    val quizId: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val type: QuestionType
)

class Converters {
    @TypeConverter
    fun fromList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toList(value: String): List<String> = value.split(",")
    
    @TypeConverter
    fun fromQuestionType(value: QuestionType): String = value.name
    
    @TypeConverter
    fun toQuestionType(value: String): QuestionType = QuestionType.valueOf(value)
} 