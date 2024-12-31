package com.trilliontests.model

data class Quiz(
    val id: Int,
    val topSubject: String,
    val subject: String,
    val difficulty: Int,
    val testType: String,
    val numberOfQuestions: Int,
    val questionSet: List<QuizQuestion> = emptyList()
) {
    val multipleChoiceCount: Int
        get() = questionSet.count { it.type == QuestionType.MULTIPLE_CHOICE }
    
    val trueFalseCount: Int
        get() = questionSet.count { it.type == QuestionType.TRUE_FALSE }
}
