package com.example.quizai.di

import android.content.Context
import com.example.quizai.data.local.AppDatabase
import com.example.quizai.data.local.dao.QuizDao
import com.example.quizai.data.repository.QuizRepository
import com.example.quizai.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    
    @Provides
    @Singleton
    fun provideQuizDao(database: AppDatabase): QuizDao {
        return database.quizDao()
    }
    
    @Provides
    @Singleton
    fun provideQuizRepository(quizDao: QuizDao): QuizRepository {
        return QuizRepository(quizDao)
    }
    
    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }
} 