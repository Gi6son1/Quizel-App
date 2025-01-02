package com.cs31620.quizel

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.questionbank.QuestionBankScreenTopLevel
import com.cs31620.quizel.ui.questionbank.QuestionEditScreenTopLevel
import com.cs31620.quizel.ui.takequiz.TakeQuizScreenTopLevel
import com.cs31620.quizel.ui.theme.QuizelTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import com.cs31620.quizel.model.ScoresViewModel
import com.cs31620.quizel.ui.takequiz.QuizResultsScreenTopLevel
import com.cs31620.quizel.ui.takequiz.TestQuestionsScreenTopLevel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizelTheme(dynamicColor = false) {
                BuildNavigationGraph()
            }
        }
    }
}

@Composable
private fun BuildNavigationGraph(
    questionsViewModel: QuestionsViewModel = viewModel(),
    scoresViewModel: ScoresViewModel = viewModel()
) {
    val navController = rememberNavController()
    var selectedQuestionId by remember { mutableIntStateOf(0) }
    var quizScore by remember { mutableStateOf("-1") }
    var quizSettings by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = Screen.QuestionBank.route
    ) {
        composable(Screen.QuestionBank.routePath()) {
            QuestionBankScreenTopLevel(navController, questionsViewModel)
        }
        composable(Screen.TakeQuiz.route) {
            TakeQuizScreenTopLevel(navController, questionsViewModel, scoresViewModel)
        }
        composable(Screen.QuestionEdit.route) {
            QuestionEditScreenTopLevel(navController, questionsViewModel, selectedQuestionId)
        }
        composable(
            Screen.QuestionEdit.routePath(),
            arguments = listOf(navArgument(Screen.QuestionEdit.argument){type = NavType.IntType})
        ) { backStackEntry ->
            backStackEntry.arguments?.let {
                if (it.containsKey(Screen.QuestionEdit.argument)){
                    selectedQuestionId = it.getInt(Screen.QuestionEdit.argument)
                }
                QuestionEditScreenTopLevel(navController, questionsViewModel, selectedQuestionId)
            }
        }
        composable(Screen.TestQuestions.route) {
            TestQuestionsScreenTopLevel(navController, questionsViewModel, quizSettings, scoresViewModel)
        }
        composable(
            Screen.TestQuestions.routePath(),
            arguments = listOf(navArgument(Screen.TestQuestions.argument){type = NavType.StringType})
        ) { backStackEntry ->
            backStackEntry.arguments?.let {
                if (it.containsKey(Screen.TestQuestions.argument)){
                    quizSettings = it.getString(Screen.TestQuestions.argument)?: "11"
                }
                TestQuestionsScreenTopLevel(navController, questionsViewModel, quizSettings, scoresViewModel)
            }
        }
        composable(Screen.QuizResults.route) {
            QuizResultsScreenTopLevel(navController, quizScore, scoresViewModel)
        }
        composable(
            Screen.QuizResults.routePath(),
            arguments = listOf(navArgument(Screen.QuizResults.argument){type = NavType.StringType})
        ) { backStackEntry ->
            backStackEntry.arguments?.let {
                if (it.containsKey(Screen.QuizResults.argument)){
                    quizScore = it.getString(Screen.QuizResults.argument)?: "-1"
                }
                QuizResultsScreenTopLevel(navController, quizScore, scoresViewModel)
            }
        }

    }
}


