package com.cs31620.quizel.ui.takequiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast
import androidx.constraintlayout.compose.atMost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.R
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.model.ScoresViewModel
import com.cs31620.quizel.ui.components.Score
import com.cs31620.quizel.ui.components.parentscaffolds.TopLevelNavigationScaffold
import com.cs31620.quizel.ui.components.customcomposables.QuizelSimpleButton
import com.cs31620.quizel.ui.components.customcomposables.QuizelSwitch
import com.cs31620.quizel.ui.components.customcomposables.RecentScoresDisplay
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.theme.QuizelTheme

/**
 * The top level for the take quiz screen
 * @param navController the navigation controller
 * @param questionsViewModel the viewmodel for the questions table
 * @param scoresViewModel the viewmodel for the scores table
 */
@Composable
fun TakeQuizScreenTopLevel(
    navController: NavHostController,
    questionsViewModel: QuestionsViewModel,
    scoresViewModel: ScoresViewModel
) {
    val questionList by questionsViewModel.questionsList.observeAsState(listOf()) //get the list of questions from the viewmodel
    val scoresList by scoresViewModel.scoresList.observeAsState(listOf()) //get the list of recent scores from the viewmodel

    TakeQuizScreen(navController = navController,
        beginQuiz = { quizSettingsString ->
            val destination = "${Screen.TestQuestions.basePath}${quizSettingsString}"
            navController.navigate(destination) {
                launchSingleTop = true
            }
            },
        enableBeginButton = !questionList.isEmpty(), //only enables the begin button if there are questions in the database
        recentScores = scoresList
        )
}

/**
 * The take quiz screen
 * @param navController the navigation controller for passing to the scaffold
 * @param beginQuiz the function to begin the quiz
 * @param enableBeginButton whether to enable the begin button
 * @param recentScores the list of recent scores
 */
@Composable
private fun TakeQuizScreen(navController: NavHostController,
                           beginQuiz: (String) -> Unit = {},
                           enableBeginButton: Boolean = false,
                           recentScores: List<Score>) {
    TopLevelNavigationScaffold(
        navController = navController
    ) { innerPadding ->

        var showProgressBar by rememberSaveable { mutableStateOf(true) } //whether to show the progress bar
        var showCurrentScore by rememberSaveable { mutableStateOf(true) } //whether to show the current score

            ConstraintLayout( modifier = Modifier //holds the components of the screen
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 90.dp)
                .wrapContentSize(),
            ) {
                val (recentScoresDisplay, quizOptions, beginButton) = createRefs() //holds the references to the components

                Card(modifier = Modifier.shadow(10.dp, MaterialTheme.shapes.medium).fillMaxWidth() //holds the recent scores area
                    .constrainAs(recentScoresDisplay){
                        top.linkTo(parent.top)
                        bottom.linkTo(quizOptions.top, margin = 10.dp)
                        height = Dimension.preferredWrapContent
                    }
                    ) {
                    Column(
                        modifier = Modifier.padding(vertical = 15.dp, horizontal = 10.dp)
                    ){
                        Text(
                            text = stringResource(R.string.recent_scores),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(),
                            fontSize = 60.sp
                        )
                        RecentScoresDisplay(modifier = Modifier.fillMaxWidth(), scoresList = recentScores)
                    }
                }
                Card(modifier = Modifier.shadow(10.dp, MaterialTheme.shapes.medium).fillMaxWidth() //holds the quiz options area
                    .constrainAs(quizOptions){
                        top.linkTo(recentScoresDisplay.bottom)
                        bottom.linkTo(beginButton.top, margin = 10.dp)
                        height = Dimension.fillToConstraints.atLeast(170.dp).atMost(175.dp)

                    }) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text( //quiz options title
                            text = stringResource(R.string.quiz_options),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(),
                            fontSize = 60.sp
                        )

                        Row(modifier = Modifier //the switch for showing the progress bar
                            .padding(horizontal = 10.dp)
                            .height(50.dp)) {
                            Text(text = stringResource(R.string.show_current_score), modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentHeight()
                            , fontSize = 20.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            QuizelSwitch(
                                checked = showCurrentScore,
                                onCheckedChange = { showCurrentScore = it })
                        }

                        HorizontalDivider()

                        Row(modifier = Modifier //the switch for showing the current score
                            .padding(horizontal = 10.dp)
                            .height(50.dp)) {
                            Text(text = stringResource(R.string.show_progress_bar), modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentHeight()
                            , fontSize = 20.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            QuizelSwitch(
                                checked = showProgressBar,
                                onCheckedChange = { showProgressBar = it })
                        }

                    }
                }
                QuizelSimpleButton( //button to begin quiz
                    onClick = {
                        val quizSettingsString = //passes the settings for the quiz to the test questions screen
                            "${if (showProgressBar) "1" else "0"},${if (showCurrentScore) "1" else "0"}"

                        beginQuiz(quizSettingsString)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(beginButton) {
                            top.linkTo(quizOptions.bottom)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints.atLeast(70.dp).atMost(70.dp)
                        },
                    shape = MaterialTheme.shapes.medium,
                    colour = MaterialTheme.colorScheme.primary,
                    text = Pair(stringResource(R.string.begin_quiz), 25),
                    icon = Icons.AutoMirrored.Filled.Send,
                    enabled = enableBeginButton
                )
            }
        }
    }



@Preview
@Composable
private fun TakeQuizScreenPreview() {
    QuizelTheme { TakeQuizScreen(navController = rememberNavController(), recentScores = listOf()) }
}