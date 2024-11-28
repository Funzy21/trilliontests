package com.example.educationapp.data.local

import com.example.educationapp.data.local.entity.Converters
import com.example.educationapp.data.local.entity.QuizEntity
import com.example.educationapp.data.local.entity.QuizQuestionEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.educationapp.data.local.dao.QuizDao

@Database(
    entities = [QuizEntity::class, QuizQuestionEntity::class],
    version = 1
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
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 