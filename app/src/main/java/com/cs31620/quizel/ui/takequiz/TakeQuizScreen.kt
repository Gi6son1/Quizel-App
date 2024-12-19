package com.cs31620.quizel.ui.takequiz

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.TopLevelNavigationScaffold
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.questionbank.QuestionBankScreen
import com.cs31620.quizel.ui.theme.QuizelTheme


/**
 * THIS PAGE RANDOMISES THE QUESTION IDS
 *
 * THE IDS ARE FED INTO THE TESTQUESTIONS SCREEN, FIRST ONE IS SHOWN
 *
 * AFTER QUESTION, THE SCREEN IS CALLED AGAIN WITH THE SAME LIST EXCEPT THE HEAD
 * SCORE IS RECORDED SOMEWHERE WHILE THIS IS HAPPENING
 */


@Composable
fun TakeQuizScreenTopLevel(
    navController: NavHostController,
    questionsViewModel: QuestionsViewModel
) {
    val questionsList by questionsViewModel.questionsList.observeAsState(listOf())
    TakeQuizScreen(navController = navController,
        questionList = questionsList)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TakeQuizScreen(navController: NavHostController, questionList: List<Question>){
    TopLevelNavigationScaffold(
        navController = navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.Transparent
        ) {
            Text(text = "Take Quiz Screen", modifier = Modifier.padding(start = 8.dp))
            Button(
                onClick = {
                    Log.d("TakeQuizScreen", "Question List: $questionList")

                    var randomisedList = questionList.shuffled()
                    var quizDataString = quizDataToString(
                        0,
                        randomisedList.size,
                        randomisedList.map { it.id.toString() }
                    )
                    Log.d("TakeQuizScreen", "Quiz Data String: $quizDataString")

                    val destination = "${Screen.TestQuestions.basePath}${quizDataString}"
                    navController.navigate(destination) {
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
fun TakeQuizScreenPreview(){
    QuizelTheme { TakeQuizScreen(navController = rememberNavController(), questionList = emptyList()) }
}