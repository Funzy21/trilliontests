package com.trilliontests.model

enum class QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE
}

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val type: QuestionType
) {
    // Convenience property to validate options based on question type
    val isValid: Boolean
        get() = when (type) {
            QuestionType.MULTIPLE_CHOICE -> options.size >= 2
            QuestionType.TRUE_FALSE -> options.containsAll(listOf("True", "False"))
        }
} 