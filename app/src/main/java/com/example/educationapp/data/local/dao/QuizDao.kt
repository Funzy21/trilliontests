package com.example.educationapp.data.local.dao

import com.example.educationapp.data.local.entity.QuizEntity
import com.example.educationapp.data.local.entity.QuizQuestionEntity
import com.example.educationapp.data.local.entity.QuizHighScoreEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query("SELECT * FROM quizzes")
    fun getAllQuizzes(): Flow<List<QuizEntity>>
    
    @Query("SELECT * FROM quiz_questions WHERE quizId = :quizId")
    fun getQuizQuestions(quizId: String): Flow<List<QuizQuestionEntity>>
    
    @Query("SELECT * FROM quizzes WHERE id = :quizId")
    fun getQuiz(quizId: String): Flow<QuizEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestionEntity>)
    
    @Transaction
    suspend fun insertQuizWithQuestions(quiz: QuizEntity, questions: List<QuizQuestionEntity>) {
        insertQuiz(quiz)
        insertQuestions(questions)
    }
    
    @Query("SELECT * FROM quiz_high_scores WHERE quizId = :quizId")
    fun getQuizHighScore(quizId: String): Flow<QuizHighScoreEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighScore(highScore: QuizHighScoreEntity)
} 