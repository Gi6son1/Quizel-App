package com.cs31620.quizel

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.questionbank.QuestionBankScreen
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.questionbank.QuestionBankScreenTopLevel
import com.cs31620.quizel.ui.takequiz.TakeQuizScreen
import com.cs31620.quizel.ui.takequiz.TakeQuizScreenTopLevel
import com.cs31620.quizel.ui.theme.QuizelTheme

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
    questionsViewModel: QuestionsViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.QuestionBank.route
    ) {
        composable(Screen.QuestionBank.route) {
            QuestionBankScreenTopLevel(navController, questionsViewModel)
        }
        composable(Screen.TakeQuiz.route) {
            TakeQuizScreenTopLevel(navController, questionsViewModel)
        }
    }
}

