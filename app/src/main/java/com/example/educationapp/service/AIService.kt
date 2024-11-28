package com.example.educationapp.service

import com.example.educationapp.model.Flashcard
import com.example.educationapp.model.GeneratedQuiz

interface AIService {
    suspend fun generateSummary(content: String): String
    suspend fun generateQuiz(content: String, questionCount: Int = 5): GeneratedQuiz
    suspend fun generateFlashcards(content: String, cardCount: Int = 10): List<Flashcard>
}
//TODO: Implement not as abstract class, but as an actual class
abstract class OpenAIService(private val apiKey: String) : AIService {
    // Implementation will go here - you'll need to sign up for OpenAI API access
    // and implement the actual API calls
} 