package com.example.quizai.data.repository

import com.example.quizai.data.local.dao.QuizDao
import com.example.quizai.data.local.entity.QuizEntity
import com.example.quizai.data.local.entity.QuizQuestionEntity
import com.example.quizai.data.local.entity.QuizHighScoreEntity
import com.example.quizai.model.Quiz
import com.example.quizai.model.QuizQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val quizDao: QuizDao
) {
    // Sample quizzes for demo purposes only
    private val sampleMathQuestions = listOf(
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

    private val sampleDrivingQuestions = listOf(
        QuizQuestion(
            id = "d1",
            question = "What is the national speed limit on single carriageway roads for cars and motorcycles?",
            options = listOf("50 mph", "60 mph", "70 mph", "80 mph"),
            correctAnswer = "60 mph"
        ),
        QuizQuestion(
            id = "d2",
            question = "When should you use your horn?",
            options = listOf(
                "To greet other drivers",
                "To alert others of your presence",
                "To express frustration",
                "To signal pedestrians"
            ),
            correctAnswer = "To alert others of your presence"
        ),
        QuizQuestion(
            id = "d3",
            question = "What does a red traffic light mean?",
            options = listOf(
                "Stop and wait behind the stop line",
                "Proceed with caution",
                "Stop only if there are pedestrians",
                "Slow down and prepare to stop"
            ),
            correctAnswer = "Stop and wait behind the stop line"
        ),
        QuizQuestion(
            id = "d4",
            question = "What should you do if you see a pedestrian with a white cane?",
            options = listOf(
                "Honk to alert them",
                "Speed up to pass them quickly",
                "Slow down and be prepared to stop",
                "Ignore and continue driving"
            ),
            correctAnswer = "Slow down and be prepared to stop"
        ),
        QuizQuestion(
            id = "d5",
            question = "What is the minimum tread depth for car tyres?",
            options = listOf("1.0 mm", "1.6 mm", "2.0 mm", "2.5 mm"),
            correctAnswer = "1.6 mm"
        )
    )

    private val sampleMathQuiz = Quiz(
        id = "sample_math",
        title = "Sample Math Quiz",
        subject = "Mathematics",
        grade = "8th",
        questionCount = sampleMathQuestions.size,
        concepts = listOf("Basic Arithmetic"),
        description = "Test your basic math skills!",
        questions = sampleMathQuestions
    )

    private val sampleDrivingQuiz = Quiz(
        id = "sample_driving",
        title = "UK Driving Theory",
        subject = "Driving",
        grade = "Theory Test",
        questionCount = sampleDrivingQuestions.size,
        concepts = listOf("Road Safety", "Traffic Rules"),
        description = "Practice questions for the UK driving theory test",
        questions = sampleDrivingQuestions
    )

    // Return both sample quizzes
    fun getAllQuizzes(): Flow<List<Quiz>> {
        return flowOf(listOf(sampleMathQuiz, sampleDrivingQuiz))
        // When ready to switch to database, use this instead:
        // return quizDao.getAllQuizzes().map { entities ->
        //     entities.map { it.toDomain() }
        // }
    }

    fun getQuiz(quizId: String): Flow<Quiz?> {
        return flowOf(
            when (quizId) {
                "sample_math" -> sampleMathQuiz
                "sample_driving" -> sampleDrivingQuiz
                else -> null
            }
        )
        // When ready to switch to database, use this instead:
        // return quizDao.getQuiz(quizId).combine(quizDao.getQuizQuestions(quizId)) { quizEntity, questionEntities ->
        //     quizEntity?.toDomain(questionEntities.map { it.toDomain() })
        // }
    }

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
    correctAnswer = correctAnswer
)

private fun QuizQuestion.toEntity(quizId: String) = QuizQuestionEntity(
    id = id,
    quizId = quizId,
    question = question,
    options = options,
    correctAnswer = correctAnswer
)