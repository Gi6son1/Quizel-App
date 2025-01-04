package com.cs31620.quizel.ui.navigation

/**
 * Class to hold the screens used for navigation
 * @param route the route to the screen that is used for navigation
 * @param argument the argument to be passed to the screen
 */
sealed class Screen(
    val route: String,
    val argument: String = ""
) {
    //the different screens that can be navigated to
    data object QuestionBank : Screen("question_bank")
    data object TakeQuiz : Screen("take_quiz")
    data object TestQuestions : Screen("test_questions", "quizData")
    data object QuestionEdit : Screen("question_edit", "questionId")
    data object QuizResults : Screen("quiz_results", "quizResults")

    //used for navigating to a screen with an argument
    fun routePath() =
        if (argument.isNotBlank())
            "$route/{$argument}"
        else
            route

    val basePath = "${route}/"
}

//list of screens used for navigation in the nav bar
val navigation_screens = listOf(
    Screen.QuestionBank,
    Screen.TakeQuiz
)