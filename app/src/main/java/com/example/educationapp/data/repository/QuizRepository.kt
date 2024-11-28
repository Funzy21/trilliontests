package com.example.educationapp.data.repository

import com.example.educationapp.data.local.dao.QuizDao
import com.example.educationapp.data.local.entity.QuizEntity
import com.example.educationapp.data.local.entity.QuizQuestionEntity
import com.example.educationapp.model.Quiz
import com.example.educationapp.model.QuizQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizDao: QuizDao
) {
    fun getAllQuizzes(): Flow<List<Quiz>> = quizDao.getAllQuizzes()
        .map { entities -> entities.map { it.toDomain() } }

    fun getQuiz(quizId: String): Flow<Quiz?> = combine(
        quizDao.getQuiz(quizId),
        quizDao.getQuizQuestions(quizId)
    ) { quizEntity, questionEntities ->
        quizEntity?.toDomain(questionEntities.map { it.toDomain() })
    }

    suspend fun insertQuiz(quiz: Quiz) {
        quizDao.insertQuizWithQuestions(
            quiz.toEntity(),
            quiz.questions.map { it.toEntity(quiz.id) }
        )
    }
}

// Extension functions for mapping between domain and entity models
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
    correctAnswer = correctAnswer
)

private fun QuizQuestion.toEntity(quizId: String) = QuizQuestionEntity(
    id = id,
    quizId = quizId,
    question = question,
    options = options,
    correctAnswer = correctAnswer
)