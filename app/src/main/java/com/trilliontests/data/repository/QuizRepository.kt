package com.trilliontests.data.repository

import com.trilliontests.data.local.dao.QuizDao
import com.trilliontests.data.local.entity.QuizEntity
import com.trilliontests.data.local.entity.QuizQuestionEntity
import com.trilliontests.data.local.entity.QuizHighScoreEntity
import com.trilliontests.model.QuestionType
import com.trilliontests.model.Quiz
import com.trilliontests.model.QuizQuestion
import com.trilliontests.utils.JsonUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class QuizRepository @Inject constructor(
    private val quizDao: QuizDao,
    @ApplicationContext private val context: Context
) {
    private val quizzes: List<Quiz> by lazy {
        try {
            val quizData = JsonUtils.parseJsonFromAssets<QuizData>(context, "quizzes.json")
            quizData.quizzes
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getAllQuizzes(): Flow<List<Quiz>> = flowOf(quizzes)

    fun getQuiz(quizId: String): Flow<Quiz?> =
        flowOf(quizzes.find { it.id == quizId })

    // Keep these for when you implement actual database storage
    suspend fun insertQuiz(quiz: Quiz) {
        quizDao.insertQuizWithQuestions(
            quiz.toEntity(),
            quiz.questions.map { it.toEntity(quiz.id) }
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

// Keep your existing mapping extension functions
private fun QuizEntity.toDomain(questions: List<QuizQuestion> = emptyList()) = Quiz(
    id = id,
    title = title,
    subject = subject,
    grade = grade,
    questionCount = questionCount,
    concepts = concepts,
    description = description,
    questions = questions
)

private fun Quiz.toEntity() = QuizEntity(
    id = id,
    title = title,
    subject = subject,
    grade = grade,
    questionCount = questionCount,
    concepts = concepts,
    description = description
)

private fun QuizQuestionEntity.toDomain() = QuizQuestion(
    id = id,
    question = question,
    options = options,
    correctAnswer = correctAnswer,
    type = type
)

private fun QuizQuestion.toEntity(quizId: String) = QuizQuestionEntity(
    id = id,
    quizId = quizId,
    question = question,
    options = options,
    correctAnswer = correctAnswer,
    type = type
)

// Data class to match JSON structure
private data class QuizData(
    val quizzes: List<Quiz>
)