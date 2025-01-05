package com.cs31620.quizel.ui.takequiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.cs31620.quizel.R
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.model.ScoresViewModel
import com.cs31620.quizel.ui.components.customcomposables.ActionCheckDialog
import com.cs31620.quizel.ui.components.Answer
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.Score
import com.cs31620.quizel.ui.components.parentscaffolds.TopLevelBackgroundScaffold
import com.cs31620.quizel.ui.components.customcomposables.QuizelSimpleButton
import com.cs31620.quizel.ui.navigation.Screen

/**
 * The top level for the test questions screen
 * @param navController the navigation controller
 * @param questionsViewModel the viewmodel for the questions table
 * @param quizViewParameters the parameters for the quiz, stored as a string
 * @param scoresViewModel the viewmodel for the scores table
 */
@Composable
fun TestQuestionsScreenTopLevel(
    navController: NavHostController,
    questionsViewModel: QuestionsViewModel,
    quizViewParameters: String,
    scoresViewModel: ScoresViewModel
) {
    val context = LocalContext.current //required for the default username string

    val questionList by questionsViewModel.questionsList.observeAsState(listOf()) //retrieve the list of questions from the viewmodel
    val shuffledList by rememberSaveable { mutableStateOf(questionList.shuffled()) } //shuffle the list of questions

    val (showProgressBar, showNumberCorrect) = quizViewParameters.split(",")
        .map { it == "1" } //split the string into progress bar and show number correct settings
    val mostRecentUsername: String? by scoresViewModel.mostRecentUsername.observeAsState("") //finds most recently used username

    TestQuestionsScreen(
        questionList = shuffledList,

        quizResultsScreen = { quizResults ->
            val destination = "${Screen.QuizResults.basePath}${quizResults}"
            navController.navigate(destination) {
                launchSingleTop = true
            }
        },

        exitQuiz = { exit ->
            if (exit) {
                navController.popBackStack(Screen.TakeQuiz.route, inclusive = false)
            }
        },
        showNumberCorrect = showNumberCorrect,
        showProgressBar = showProgressBar,
        saveScore = { score ->
            score.username = mostRecentUsername
                ?: context.getString(R.string.user) //if there is no recent username, use the default
            scoresViewModel.addNewScore(score)
        }
    )
}

/**
 * The test questions screen
 * @param questionList the list of questions
 * @param quizResultsScreen the function to navigate to the quiz results screen
 * @param exitQuiz the function to exit the quiz
 * @param showNumberCorrect whether to show the number of questions correct
 * @param showProgressBar whether to show the progress bar
 * @param saveScore the function to save the score
 */
@Composable
private fun TestQuestionsScreen(
    questionList: List<Question>,
    quizResultsScreen: (String) -> Unit = {},
    exitQuiz: (Boolean) -> Unit = {},
    showNumberCorrect: Boolean = true,
    showProgressBar: Boolean = true,
    saveScore: (Score) -> Unit = {}
) {
    TopLevelBackgroundScaffold(showTitle = !showNumberCorrect) //in this screen, the app title is hidden if the show number correct setting is enabled, for space
    { innerPadding ->
        var currentQuestion by rememberSaveable { mutableStateOf(questionList.first()) } //holds the current question, initiated as the first in the list
        var currentQuestionNumber by rememberSaveable { mutableIntStateOf(1) } //holds the current question number, initialised as 1
        var currentScore by rememberSaveable { mutableIntStateOf(0) } //holds the current score, starting at 0
        val totalQuestions by rememberSaveable { mutableIntStateOf(questionList.size) } // holds the total number of questions

        var selectedAnswer by rememberSaveable(currentQuestion) { mutableStateOf<Answer?>(null) } //holds the selected answer
        var showSkipDialog by rememberSaveable { mutableStateOf(false) } //whether to show the skip question dialog
        var showExitQuizDialog by rememberSaveable { mutableStateOf(false) } //whether to show the exit quiz dialog

        val shuffledAnswers = rememberSaveable(currentQuestion) {
            currentQuestion.answers.shuffled() //shuffles the answers for each question
        }

        BackHandler {
            showExitQuizDialog = true //if back is used, show the exit quiz dialog
        }

        fun transferResultsToString() = "$currentScore,$totalQuestions" //function to transfer the results to a string to pass into the quiz results screen

        fun nextQuestion(gotCorrect: Boolean = false) { //function to move to the next question
            currentScore = if (gotCorrect) currentScore + 1 else currentScore //increase score if correct
            if (currentQuestionNumber == totalQuestions) {
                saveScore(Score(score = currentScore, numQuestions = totalQuestions)) //if last question, save score and go to results screen
                quizResultsScreen(transferResultsToString())
            } else {
                currentQuestion = questionList[currentQuestionNumber] //otherwise reset variables for next question
                currentQuestionNumber++
                selectedAnswer = null
                showSkipDialog = false
            }
        }

        ConstraintLayout( //holds the screen components
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp)
        ) {
            val (questionAnswerSubmitColumn, homeButton, currentScoreText) = createRefs() //holds the references to the components

            IconButton( //home button
                onClick =
                {
                    showExitQuizDialog = true
                },
                modifier = Modifier
                    .constrainAs(homeButton) {
                        top.linkTo(parent.top, margin = 25.dp)
                        start.linkTo(parent.start, margin = 20.dp)
                        bottom.linkTo(questionAnswerSubmitColumn.top)
                    }
                    .size(70.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.house_outline),
                    contentDescription = stringResource(R.string.home_button),
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }

            Text( //displays the current score
                text = stringResource(R.string.current_score, currentScore, totalQuestions),
                modifier = Modifier
                    .constrainAs(currentScoreText) {
                        top.linkTo(parent.top, margin = 50.dp)
                        end.linkTo(parent.end, margin = 20.dp)
                        bottom.linkTo(questionAnswerSubmitColumn.top)
                    },
                style = MaterialTheme.typography.titleLarge,
                fontSize = 50.sp,
                color = if (showNumberCorrect) MaterialTheme.colorScheme.onSurface else Color.Transparent
            )


            Column( //holds the question, answers, and submit button, and progress bar
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp)
                    .constrainAs(questionAnswerSubmitColumn) {
                        top.linkTo(homeButton.bottom, margin = 10.dp)
                        top.linkTo(currentScoreText.bottom, margin = 10.dp)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (showProgressBar) { //progress bar if enabled
                    LinearProgressIndicator(
                        modifier = Modifier
                            .weight(0.25f)
                            .fillMaxWidth(),
                        progress = { currentQuestionNumber / totalQuestions.toFloat() },
                        trackColor = MaterialTheme.colorScheme.surface
                    )
                }

                QuestionArea( //surface where question is displayed
                    modifier = Modifier.weight(11f),
                    question = currentQuestion,
                    questionNumber = currentQuestionNumber
                )

                Surface( //answer area
                    modifier = Modifier
                        .weight(10f)
                        .shadow(10.dp, MaterialTheme.shapes.medium),
                    color = MaterialTheme.colorScheme.surfaceDim,
                    shape = MaterialTheme.shapes.medium,
                )
                {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 5.dp, end = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        item{Spacer(modifier = Modifier.height(0.dp))} //adds space at the top of the grid, no height needed as the column spacedBy sorts it
                        item{Spacer(modifier = Modifier.height(0.dp))}
                        items(shuffledAnswers) { answer ->
                            QuizelSimpleButton(
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
                                    .height(80.dp),
                                shape = MaterialTheme.shapes.medium,
                                colour = if (selectedAnswer == answer) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceBright,
                                text = Pair(answer.text, 20),
                            )
                        }
                        item{Spacer(modifier = Modifier.height(0.dp))} //adds space at the top of the grid, no height needed as the column spacedBy sorts it
                        item{Spacer(modifier = Modifier.height(0.dp))}
                    }
                }
                if (selectedAnswer != null) { //if an answer is selected, display submit button
                    QuizelSimpleButton(
                        onClick = {
                            if (selectedAnswer!!.isCorrect) {
                                nextQuestion(true)
                            } else {
                                nextQuestion()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        colour = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium,
                        text = Pair(stringResource(R.string.submit_answer), 20),
                        icon = Icons.AutoMirrored.Filled.Send,
                    )
                } else { //otherwise display skip button
                    QuizelSimpleButton(
                        onClick = { showSkipDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        colour = MaterialTheme.colorScheme.surfaceDim,
                        shape = MaterialTheme.shapes.medium,
                        text = Pair(stringResource(R.string.skip_question), 20),
                        icon = Icons.AutoMirrored.Filled.Send,
                    )
                }
            }
        }


        ActionCheckDialog(//skip question dialog
            dialogIsOpen = showSkipDialog,
            dialogOpen = { isOpen -> showSkipDialog = isOpen },
            mainActionButton = { onClick, modifier ->
                Button(
                    onClick = onClick,
                    modifier = modifier,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(R.string.skip), fontSize = 19.sp)
                }
            },
            actionDialogMessage = stringResource(R.string.skip_question_check),
            performMainAction = { skipQuestion ->
                if (skipQuestion) {
                    nextQuestion()
                }
            }
        )

        ActionCheckDialog( //exit quiz dialog
            dialogIsOpen = showExitQuizDialog,
            dialogOpen = { isOpen -> showExitQuizDialog = isOpen },
            mainActionButton = { onClick, modifier ->
                Button(
                    onClick = onClick,
                    modifier = modifier,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(R.string.exit_quiz), fontSize = 19.sp)
                }
            },
            actionDialogMessage = stringResource(R.string.exit_quiz_check),
            performMainAction = { exit ->
                exitQuiz(exit)
            }
        )
    }
}

/**
 * Question area method
 * @param modifier the modifier for the question area
 * @param question the question to be displayed
 * @param questionNumber the question number
 */
@Composable
private fun QuestionArea(
    modifier: Modifier,
    question: Question,
    questionNumber: Int
){
    Surface( //question area
        modifier = modifier
            .shadow(10.dp, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceDim
    )
    {
        ConstraintLayout( //holds the question and description
            modifier = Modifier.fillMaxSize(),
        ) {
            val (questionTitle, questionDescription, divider) = createRefs()
            Text(
                modifier = Modifier
                    .constrainAs(questionTitle) {
                        top.linkTo(parent.top, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = 15.dp),
                text = if (question.title.isBlank()) {
                    stringResource(R.string.question_number, questionNumber)

                } else {
                    question.title
                },
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
            )
            HorizontalDivider(modifier = Modifier
                .constrainAs(divider) {
                    top.linkTo(questionTitle.bottom, margin = 5.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 30.dp),
                thickness = 2.dp
            )
            Column(modifier = Modifier
                .padding(15.dp)
                .constrainAs(questionDescription) {
                    top.linkTo(divider.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 5.dp)
                    height = Dimension.fillToConstraints
                }
                .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = question.description,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

/**
 * Preview of the test questions screen
 */
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
