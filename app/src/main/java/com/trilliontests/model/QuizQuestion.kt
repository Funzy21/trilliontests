package com.trilliontests.model

enum class QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE,
    MATCHING
}

data class QuizQuestion(
    val question: String,
    val choices: Map<String, String>,
    val correctAnswer: String,
    val explanation: String,
    val type: QuestionType
) {
    val isValid: Boolean
        get() = when (type) {
            QuestionType.MULTIPLE_CHOICE -> choices.size >= 4
            QuestionType.TRUE_FALSE -> choices.size == 2
            QuestionType.MATCHING -> false // We'll handle matching separately
        }
} 