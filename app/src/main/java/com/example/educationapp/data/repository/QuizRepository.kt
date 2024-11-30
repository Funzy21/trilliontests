package com.example.educationapp.data.repository

import com.example.educationapp.data.local.dao.QuizDao
import com.example.educationapp.data.local.entity.QuizEntity
import com.example.educationapp.data.local.entity.QuizQuestionEntity
import com.example.educationapp.model.Quiz
import com.example.educationapp.model.QuizQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizDao: QuizDao
) {
    // Sample math quiz for demo purposes only
    private val sampleQuestions = listOf(
        QuizQuestion(
            id = "1",
            question = "What is 2 + 2?",
            options = listOf("3", "4", "5", "6"),
            correctAnswer = "4"
        ),
        QuizQuestion(
            id = "2",
            question = "What is 5 × 5?",
            options = listOf("20", "25", "30", "35"),
            correctAnswer = "25"
        ),
        QuizQuestion(
            id = "3",
            question = "What is 10 ÷ 2?",
            options = listOf("3", "4", "5", "6"),
            correctAnswer = "5"
        ),
        QuizQuestion(
            id = "4",
            question = "What is 15 - 7?",
            options = listOf("6", "7", "8", "9"),
            correctAnswer = "8"
        ),
        QuizQuestion(
            id = "5",
            question = "What is 3 × 4?",
            options = listOf("10", "11", "12", "13"),
            correctAnswer = "12"
        )
    )

    private val sampleQuiz = Quiz(
        id = "sample_math",
        title = "Math Quiz",
        subject = "Mathematics",
        grade = "8th",
        questionCount = sampleQuestions.size,
        concepts = listOf("Basic Arithmetic"),
        description = "Test your basic math skills!",
        questions = sampleQuestions
    )

    // Temporarily return sample data instead of database data
    fun getAllQuizzes(): Flow<List<Quiz>> = flowOf(listOf(sampleQuiz))

    fun getQuiz(quizId: String): Flow<Quiz?> = flowOf(
        if (quizId == "sample_math") sampleQuiz else null
    )

    // Keep these for when you implement actual database storage
    suspend fun insertQuiz(quiz: Quiz) {
        quizDao.insertQuizWithQuestions(
            quiz.toEntity(),
            quiz.questions.map { it.toEntity(quiz.id) }
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
    correctAnswer = correctAnswer
)

private fun QuizQuestion.toEntity(quizId: String) = QuizQuestionEntity(
    id = id,
    quizId = quizId,
    question = question,
    options = options,
    correctAnswer = correctAnswer
)