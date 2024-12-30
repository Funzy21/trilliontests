package com.trilliontests.model

data class Quiz(
    val id: String,
    val title: String,
    val subject: String,
    val grade: String,
    val questionCount: Int,
    val concepts: List<String>,
    val description: String,
    val questions: List<QuizQuestion> = emptyList()
) {
    val multipleChoiceCount: Int
        get() = questions.count { it.type == QuestionType.MULTIPLE_CHOICE }
    
    val trueFalseCount: Int
        get() = questions.count { it.type == QuestionType.TRUE_FALSE }
}
