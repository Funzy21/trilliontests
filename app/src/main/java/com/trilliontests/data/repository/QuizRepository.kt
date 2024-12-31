package com.trilliontests.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trilliontests.data.local.dao.QuizDao
import com.trilliontests.data.local.entity.QuizEntity
import com.trilliontests.data.local.entity.QuizQuestionEntity
import com.trilliontests.data.local.entity.QuizHighScoreEntity
import com.trilliontests.model.Quiz
import com.trilliontests.model.QuizQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val quizDao: QuizDao,
    private val context: Context
) {
    private val quizzes: List<Quiz> by lazy {
        loadQuizzes()
    }

    private fun loadQuizzes(): List<Quiz> {
        return try {
            val json = context.assets.open("quizzes.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<Map<String, List<Quiz>>>() {}.type
            Gson().fromJson<Map<String, List<Quiz>>>(json, type)["quizzes"] ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getAllQuizzes(): Flow<List<Quiz>> = flowOf(quizzes)

    fun getQuiz(quizId: Int): Flow<Quiz?> =
        flowOf(quizzes.find { it.id == quizId })

    // Update the mapping functions for the new Quiz and QuizQuestion format
    private fun Quiz.toEntity() = QuizEntity(
        id = id.toString(), // Convert Int to String for database
        title = "$topSubject - $subject", // Combine for display
        subject = subject,
        grade = "Level $difficulty", // Convert difficulty to grade display
        questionCount = numberOfQuestions,
        concepts = emptyList(), // We don't have concepts in new format
        description = "Level $difficulty $subject quiz" // Generate description
    )

    private fun QuizQuestion.toEntity(quizId: String) = QuizQuestionEntity(
        id = "${quizId}_${hashCode()}", // Generate an ID since we don't have one
        quizId = quizId,
        question = question,
        options = choices.values.toList(), // Convert Map values to List
        correctAnswer = correctAnswer,
        type = type
    )

    suspend fun insertQuiz(quiz: Quiz) {
        quizDao.insertQuizWithQuestions(
            quiz.toEntity(),
            quiz.questionSet.map { it.toEntity(quiz.id.toString()) }
        )
    }

    fun getQuizHighScore(quizId: String): Flow<QuizHighScoreEntity?> =
        quizDao.getQuizHighScore(quizId)

    suspend fun updateHighScore(quizId: String, score: Int, totalQuestions: Int) {
        quizDao.insertHighScore(
            QuizHighScoreEntity(
                quizId = quizId,
                highScore = score,
                totalQuestions = totalQuestions
            )
        )
    }
}