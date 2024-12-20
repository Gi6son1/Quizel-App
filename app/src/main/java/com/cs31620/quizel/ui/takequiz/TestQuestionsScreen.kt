package com.cs31620.quizel.ui.takequiz

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
        currentScore = currentScore,
        totalQuestions = totalQuestions,
        navigateToNextQuestion = { dataString ->
            val destination = "${Screen.TestQuestions.basePath}${dataString}"
            navController.navigate(destination){
                launchSingleTop = true
            }
        }
    )
}

@Composable
fun TestQuestionsScreen(
    remainingQuestionList: List<String>,
    currentQuestion: Question?,
    currentScore: Int,
    totalQuestions: Int,
    navigateToNextQuestion: (String) -> Unit = {},
) {
    TopLevelBackgroundScaffold { innerPadding ->
        if (currentQuestion == null){
            Log.e("TestQuestionsScreen", "Current question is null")
        }

        if (currentQuestion == null){
            navigateToNextQuestion(quizDataToString(currentScore, totalQuestions, remainingQuestionList))
        }


        var selectedAnswer by rememberSaveable { mutableStateOf<Answer?>(null) }
        var showSkipDialog by rememberSaveable { mutableStateOf(false) }

        val shuffledAnswers = rememberSaveable (currentQuestion) {
            currentQuestion?.answers?.shuffled() ?: emptyList()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp, top = 100.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxWidth(),
                progress = {1 - remainingQuestionList.size.toFloat()/totalQuestions}
            )
            Surface(
                modifier = Modifier
                    .weight(11f)
                    .shadow(10.dp, MaterialTheme.shapes.medium),
                shape = MaterialTheme.shapes.medium,
            )
            {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = if (currentQuestion?.title.isNullOrBlank())
                        {
                            "Question ${totalQuestions - remainingQuestionList.size}"

                        } else {
                            currentQuestion.title
                        }
                    )
                    HorizontalDivider()
                    Text(
                        text = currentQuestion!!.description
                    )
                }
            }
            Surface(modifier = Modifier
                .weight(10f)
                .shadow(10.dp, MaterialTheme.shapes.medium),
                color = Color.LightGray,
                shape = MaterialTheme.shapes.medium,
                )
            {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 5.dp, end = 5.dp, top = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(shuffledAnswers) { answer ->
                        Button(
                            onClick = {
                                selectedAnswer = if (selectedAnswer != answer) {
                                    answer
                                } else {
                                    null
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .height(80.dp)
                                .shadow(10.dp, MaterialTheme.shapes.medium),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedAnswer == answer) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainer
                                }
                            )
                        ) {
                            Text(text = answer.text,
                                color = if (selectedAnswer == answer){
                                    Color.White
                                } else {
                                    Color.Black
                                },
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
            if (selectedAnswer != null) {
                Button(
                    onClick = {
                        val newScore = if (selectedAnswer!!.isCorrect) currentScore + 1 else currentScore
                        navigateToNextQuestion(
                            quizDataToString(
                                newScore,
                                totalQuestions,
                                remainingQuestionList)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .shadow(10.dp, MaterialTheme.shapes.medium),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Submit icon",
                        modifier = Modifier
                            .wrapContentSize(),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Submit Answer")
                }
            } else {
                Button(
                    onClick = { showSkipDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .shadow(10.dp, MaterialTheme.shapes.medium)
                        .border(4.dp, Color.LightGray),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Submit icon",
                        modifier = Modifier
                            .wrapContentSize(),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Skip Question",
                        color = Color.Black)
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
                    navigateToNextQuestion(quizDataToString(currentScore, totalQuestions, remainingQuestionList))
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
        listOf("1", "2", "3", "4", "5", "6", "7", "8"),
        currentQuestion = exampleQuestionWith10Answers,
        currentScore = 2,
        totalQuestions = 10,
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