package com.cs31620.quizel.ui.takequiz

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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.ActionCheckDialog
import com.cs31620.quizel.ui.components.Answer
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.TopLevelBackgroundScaffold
import com.cs31620.quizel.ui.navigation.Screen

@Composable
fun TestQuestionsScreenTopLevel(
    navController: NavHostController,
    questionsViewModel: QuestionsViewModel
) {
    val questionList by questionsViewModel.questionsList.observeAsState(listOf())
    val shuffledList by rememberSaveable { mutableStateOf(questionList.shuffled()) }

    TestQuestionsScreen(
        questionList = shuffledList,

        quizResultsScreen = { quizResults ->
            val destination = "${Screen.QuizResults.basePath}${quizResults}"
            navController.navigate(destination){
                launchSingleTop = true
            }
        },

        exitQuiz = { exit ->
            if (exit) {
                navController.navigate(Screen.TakeQuiz.route) {
                    launchSingleTop = true
                }
            }
        }
    )
}

@Composable
private fun TestQuestionsScreen(
    questionList: List<Question>,
    quizResultsScreen: (String) -> Unit = {},
    exitQuiz: (Boolean) -> Unit = {},
) {
    TopLevelBackgroundScaffold { innerPadding ->

        var currentQuestion by rememberSaveable { mutableStateOf(questionList.first()) }
        var currentQuestionNumber by rememberSaveable { mutableIntStateOf(1) }
        var currentScore by rememberSaveable { mutableIntStateOf(0) }
        val totalQuestions = questionList.size

        var selectedAnswer by rememberSaveable (currentQuestion) { mutableStateOf<Answer?>(null) }
        var showSkipDialog by rememberSaveable { mutableStateOf(false) }
        var showExitQuizDialog by rememberSaveable { mutableStateOf(false) }

        val shuffledAnswers = rememberSaveable (currentQuestion) {
            currentQuestion.answers.shuffled()
        }

        fun transferResultsToString() = "$currentScore,$totalQuestions"

        fun nextQuestion(gotCorrect: Boolean = false) {
            currentScore = if (gotCorrect) currentScore + 1 else currentScore
            if (currentQuestionNumber == totalQuestions) {
                quizResultsScreen(transferResultsToString())
            } else {
                currentQuestion = questionList[currentQuestionNumber]
                currentQuestionNumber++
                selectedAnswer = null
                showSkipDialog = false
            }
        }

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp)
        ) {
            val (questionAnswerSubmitColumn, homeButton, currentScoreText) = createRefs()

            Button(
                onClick =
                    {
                        showExitQuizDialog = true
                    },
                modifier = Modifier
                    .constrainAs(homeButton){
                        top.linkTo(parent.top, margin = 20.dp)
                        start.linkTo(parent.start, margin = 20.dp)
                    }
            ) {
                Text("Exit")
            }

            Text(
                text = "Score: $currentScore/$totalQuestions",
                modifier = Modifier
                    .constrainAs(currentScoreText) {
                        top.linkTo(parent.top, margin = 20.dp)
                        end.linkTo(parent.end, margin = 20.dp)
                    }
            )


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp)
                    .constrainAs(questionAnswerSubmitColumn) {
                        top.linkTo(homeButton.bottom, margin = 80.dp)
                        top.linkTo(currentScoreText.bottom, margin = 80.dp)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxWidth(),
                    progress = {currentQuestionNumber/totalQuestions.toFloat()}
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
                            text = if (currentQuestion.title.isBlank())
                            {
                                "Question $currentQuestionNumber"

                            } else {
                                currentQuestion.title
                            }
                        )
                        HorizontalDivider()
                        Text(
                            text = currentQuestion.description
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
                            if (selectedAnswer!!.isCorrect){
                                nextQuestion(true)
                            } else {
                                nextQuestion()
                            }
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
        }


        ActionCheckDialog(
            dialogIsOpen = showSkipDialog,
            dialogOpen = { isOpen -> showSkipDialog = isOpen },
            mainActionButton = { onClick, modifier ->
                Button(
                    onClick = onClick,
                    modifier = modifier,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Skip", fontSize = 19.sp)
                }
            },
            actionDialogMessage = "Are you sure you want to skip? This will be counted as an incorrect answer",
            performMainAction = { skipQuestion ->
                if (skipQuestion) {
                    nextQuestion()
                }
            }
        )

        ActionCheckDialog(
            dialogIsOpen = showExitQuizDialog,
            dialogOpen = { isOpen -> showExitQuizDialog = isOpen },
            mainActionButton = { onClick, modifier ->
                Button(
                    onClick = onClick,
                    modifier = modifier,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Exit Quiz", fontSize = 19.sp)
                }
            },
            actionDialogMessage = "Are you sure you want to exit? Your quiz progress will not be saved",
            performMainAction = { exit ->
                exitQuiz(exit)
            }
        )
    }
}

@Preview
@Composable
private fun TestQuestionsWithoutRecursionScreenPreview() {
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
        questionList = listOf(exampleQuestionWith10Answers)
    )
}
