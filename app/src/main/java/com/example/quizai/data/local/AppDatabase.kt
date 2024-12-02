package com.example.quizai.data.local

import com.example.quizai.data.local.entity.Converters
import com.example.quizai.data.local.entity.QuizEntity
import com.example.quizai.data.local.entity.QuizQuestionEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizai.data.local.dao.QuizDao
import com.example.quizai.data.local.entity.QuizHighScoreEntity

@Database(
    entities = [
        QuizEntity::class, 
        QuizQuestionEntity::class,
        QuizHighScoreEntity::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "education_app_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 