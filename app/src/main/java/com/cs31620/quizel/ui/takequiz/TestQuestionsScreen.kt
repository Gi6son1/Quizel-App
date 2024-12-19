package com.cs31620.quizel.ui.takequiz

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.ActionCheckDialog
import com.cs31620.quizel.ui.components.Answer
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.TopLevelBackgroundScaffold
import com.cs31620.quizel.ui.navigation.Screen

@Composable
fun TestQuestionsScreenTopLevel(
    navController: NavHostController,
    questionsViewModel: QuestionsViewModel,
    quizDataAsList: String?
) {
    if (quizDataAsList == null){
        Log.e("TestQuestionsScreenTopLevel", "Quiz data is null")
        navController.popBackStack()
        return
    }
    val (currentScore, totalQuestions, questionList) = stringToQuizData(quizDataAsList)

    val listHead by questionsViewModel.getQuestionById(questionList[0].toInt()).observeAsState(Question())


    TestQuestionsScreen(
        remainingQuestionList = questionList.drop(1),
        currentQuestion = listHead,
        navController = navController,
        currentScore = currentScore,
        totalQuestions = totalQuestions
    )
}

@Composable
fun TestQuestionsScreen(
    remainingQuestionList: List<String>,
    currentQuestion: Question?,
    currentScore: Int,
    totalQuestions: Int,
    navController: NavHostController
) {
    TopLevelBackgroundScaffold { innerPadding ->
        if (currentQuestion == null){
            Log.e("TestQuestionsScreen", "Current question is null")
        }

        if (currentQuestion == null){
            val destination = "${Screen.TestQuestions.basePath}${quizDataToString(currentScore, totalQuestions, remainingQuestionList)}"
            navController.navigate(destination){
                launchSingleTop = true
            }
        }

        var answerSelected by rememberSaveable { mutableStateOf(false) }
        var showSkipDialog by rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp, top = 100.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxWidth()
            )
            Surface(
                modifier = Modifier.weight(11f)
            )
            {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = currentQuestion?.title ?: "Question ${totalQuestions - remainingQuestionList.size}"
                    )
                    HorizontalDivider()
                    Text(
                        text = currentQuestion!!.description
                    )
                }
            }
            Surface(modifier = Modifier.weight(10f))
            {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(currentQuestion!!.answers) { answer ->
                        Button(
                            onClick = {
                                answerSelected = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(text = answer.text)
                        }
                    }
                }
            }
            if (answerSelected) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    Text(text = "Submit Answer")
                }
            } else {
                Button(
                    onClick = { showSkipDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    Text(text = "Skip Question")
                }
            }
        }
        ActionCheckDialog(
            dialogIsOpen = showSkipDialog,
            dialogOpen = { isOpen -> showSkipDialog = isOpen },
            mainActionButton = { onClick, modifier ->
                Button(
                    onClick = onClick,
                    modifier = modifier,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text(text = "Skip", fontSize = 19.sp)
                }
            },
            actionDialogMessage = "Are you sure you want to skip? This will be counted as an incorrect answer",
            performMainAction = { skipQuestion ->
                if (skipQuestion) {
                    val destination = "${Screen.TestQuestions.basePath}${quizDataToString(currentScore, totalQuestions, remainingQuestionList)}"
                    navController.navigate(destination){
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun TestQuestionsScreenPreview() {
    val exampleQuestionWith10Answers = Question(
        title = "France Capital City",
        description = "Select the correct capital of France.",
        answers = mutableListOf(
            Answer("Paris", true),
            Answer("London", false),
            Answer("Berlin", false),
            Answer("Madrid", false),
            Answer("Rome", false),
            Answer("Vienna", false),
            Answer("Brussels", false),
            Answer("Amsterdam", false),
            Answer("Dublin", false),
        )
    )
    TestQuestionsScreen(
        listOf(),
        currentQuestion = exampleQuestionWith10Answers,
        currentScore = 5,
        totalQuestions = 10,
        navController = rememberNavController()
    )
}

fun quizDataToString(
    currentScore: Int,
    totalQuestions: Int,
    questionList: List<String>
    ): String {
    val builder = StringBuilder(3)
        .append(currentScore)
        .append(",")
        .append(totalQuestions)
        .append(",")

    for(s in questionList){
        builder.append(s)
        builder.append(",")
    }

    return builder.substring(0, builder.length-1).toString()
}

fun stringToQuizData(dataString: String) : Triple<Int, Int, List<String>>{
    val dataList = dataString.split(",")
    return Triple(dataList[0].toInt(), dataList[1].toInt(), dataList.drop(2))
}