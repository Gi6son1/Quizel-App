package com.cs31620.quizel.ui.navigation

sealed class Screen (
    val route: String
) {
    data object QuestionBank : Screen("question_bank")
    data object TakeQuiz : Screen("take_quiz")
}

val screens = listOf(
    Screen.QuestionBank,
    Screen.TakeQuiz
)