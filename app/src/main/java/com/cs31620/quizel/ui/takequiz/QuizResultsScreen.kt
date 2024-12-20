package com.cs31620.quizel.ui.takequiz

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.ui.components.TopLevelBackgroundScaffold
import com.cs31620.quizel.ui.components.TopLevelNavigationScaffold
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.theme.QuizelTheme

@Composable
fun QuizResultsScreenTopLevel(
    navController: NavHostController,
    quizResults: String
) {
    QuizResultsScreen(navController = navController, quizResults = quizResults)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun QuizResultsScreen(
    navController: NavHostController,
    quizResults: String = ""
) {
    TopLevelBackgroundScaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(text = "QuizResultsScreen", modifier = Modifier.padding(start = 8.dp))
            Text(text = quizResults)
            Button(
                onClick = {
                    navController.navigate(Screen.TestQuestions.route) {
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) { Text(text = "Begin Quiz") }
        }
    }
}

@Preview
@Composable
private fun TakeQuizScreenPreview() {
    QuizelTheme { QuizResultsScreen(navController = rememberNavController()) }
}