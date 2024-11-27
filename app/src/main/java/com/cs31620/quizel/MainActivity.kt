package com.cs31620.quizel

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.ui.questionbank.QuestionBankScreen
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.takequiz.TakeQuizScreen
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
private fun BuildNavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.QuestionBank.route
    ) {
        composable(Screen.QuestionBank.route) { QuestionBankScreen(navController) }
        composable(Screen.TakeQuiz.route) { TakeQuizScreen(navController) }
    }
}

