package com.cs31620.quizel.ui.takequiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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

@Composable
fun TakeQuizScreenTopLevel(
    navController: NavHostController,
    questionsViewModel: QuestionsViewModel,
    scoresViewModel: ScoresViewModel
) {
    val questionList by questionsViewModel.questionsList.observeAsState(listOf())
    val scoresList by scoresViewModel.scoresList.observeAsState(listOf())

    TakeQuizScreen(navController = navController,
        beginQuiz = { quizSettingsString ->
            val destination = "${Screen.TestQuestions.basePath}${quizSettingsString}"
            navController.navigate(destination) {
                launchSingleTop = true
            }
            },
        enableBeginButton = !questionList.isEmpty(),
        recentScores = scoresList
        )
}

@Composable
private fun TakeQuizScreen(navController: NavHostController,
                           beginQuiz: (String) -> Unit = {},
                           enableBeginButton: Boolean = false,
                           recentScores: List<Score>) {
    TopLevelNavigationScaffold(
        navController = navController
    ) { innerPadding ->

        var showProgressBar by rememberSaveable { mutableStateOf(true) }
        var showCurrentScore by rememberSaveable { mutableStateOf(true) }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 100.dp)
        )
        {
            val (content) = createRefs()

            Column(modifier = Modifier.constrainAs(content) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(modifier = Modifier.shadow(10.dp, MaterialTheme.shapes.medium).fillMaxWidth()) {
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
                Card(modifier = Modifier.shadow(10.dp, MaterialTheme.shapes.medium).fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.quiz_options),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(),
                            fontSize = 60.sp
                        )

                        Row(modifier = Modifier
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

                        Row(modifier = Modifier
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
                QuizelSimpleButton(
                    onClick = {
                        val quizSettingsString =
                            "${if (showProgressBar) "1" else "0"},${if (showCurrentScore) "1" else "0"}"

                        beginQuiz(quizSettingsString)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = MaterialTheme.shapes.medium,
                    colour = MaterialTheme.colorScheme.primary,
                    text = Pair(stringResource(R.string.begin_quiz), 25),
                    icon = Icons.AutoMirrored.Filled.Send,
                    enabled = enableBeginButton
                )
            }
        }
    }
}


@Preview
@Composable
private fun TakeQuizScreenPreview() {
    QuizelTheme { TakeQuizScreen(navController = rememberNavController(), recentScores = listOf()) }
}