package com.cs31620.quizel.ui.navigation

sealed class Screen(
    val route: String,
    val argument: String = ""
) {
    data object QuestionBank : Screen("question_bank")
    data object TakeQuiz : Screen("take_quiz")
    data object TestQuestions : Screen("test_questions", "quizData")
    data object QuestionEdit : Screen("question_edit", "questionId")
    data object QuizResults : Screen("quiz_results", "quizResults")

    fun routePath() =
        if (argument.isNotBlank())
            "$route/{$argument}"
        else
            route

    val basePath = "${route}/"
}


val navigation_screens = listOf(
    Screen.QuestionBank,
    Screen.TakeQuiz
)