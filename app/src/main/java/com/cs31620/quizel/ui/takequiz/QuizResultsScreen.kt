package com.cs31620.quizel.ui.takequiz

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.cs31620.quizel.R
import com.cs31620.quizel.model.ScoresViewModel
import com.cs31620.quizel.ui.components.Score
import com.cs31620.quizel.ui.components.parentscaffolds.TopLevelBackgroundScaffold
import com.cs31620.quizel.ui.components.customcomposables.QuizelSimpleButton
import com.cs31620.quizel.ui.components.customcomposables.RecentScoresDisplay
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.components.customcomposables.TextInputDialog

@Composable
fun QuizResultsScreenTopLevel(
    navController: NavHostController,
    quizResults: String,
    scoresViewModel: ScoresViewModel
) {
    val (finalScore, totalQuestions) = quizResults.split(",").map { it.toInt() }
    val scoresList by scoresViewModel.scoresList.observeAsState(listOf())

    QuizResultsScreen(totalQuestions = totalQuestions, finalScore = finalScore,
        restartQuiz = { restart ->
            if (restart) {
                navController.navigate(Screen.TestQuestions.route) {
                    launchSingleTop = true
                }
            }
        },
        goHome = { goHome ->
            if (goHome) {
                navController.popBackStack(Screen.TakeQuiz.route, inclusive = false)
            }
        },
        recentScores = scoresList,
        changeName = { name ->
            val recentScore = scoresList.first()
            Log.d("QuizResultsScreenTopLevel", "changeName: $name for score $recentScore")
            scoresViewModel.updateScoreWithUsername(score = recentScore, username = name)
        }
    )
}

@Composable
private fun QuizResultsScreen(
    totalQuestions: Int = 0,
    finalScore: Int = 0,
    restartQuiz: (Boolean) -> Unit = {},
    goHome: (Boolean) -> Unit = {},
    recentScores: List<Score>,
    changeName: (String) -> Unit = {},
) {
    TopLevelBackgroundScaffold(showTitle = false) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp)
        )
        {
            val (content) = createRefs()
            var showChangeNameDialog by rememberSaveable { mutableStateOf(false) }

            Column(
                modifier = Modifier.constrainAs(content) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                },
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = stringResource(R.string.your_quiz_results),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(),
                    fontSize = 80.sp,
                    style = MaterialTheme.typography.displayLarge
                )
                Card(modifier = Modifier.shadow(10.dp, MaterialTheme.shapes.medium)) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "${(finalScore / totalQuestions.toFloat() * 100).toInt()}%",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(),
                            fontSize = 60.sp
                        )

                        Text(
                            text = stringResource(
                                R.string.you_got_out_of_questions_correct,
                                finalScore,
                                totalQuestions
                            ),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(),
                            fontSize = 30.sp
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))

                        Text(
                            text = stringResource(R.string.recent_scores),
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 35.sp,
                        )
                        RecentScoresDisplay(scoresList = recentScores)
                        Text(
                            text = stringResource(
                                R.string.your_score_is_saved_under,
                                if (!recentScores.isEmpty())
                                    recentScores.first().username
                                else
                                    R.string.user
                            ),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .fillMaxWidth()
                        )
                    }
                }
                QuizelSimpleButton(
                    onClick = { showChangeNameDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = MaterialTheme.shapes.medium,
                    colour = Color.LightGray,
                    text = Pair(stringResource(R.string.change_name), 25),
                    icon = Icons.Filled.Edit
                )

                QuizelSimpleButton(
                    onClick = {
                        restartQuiz(true)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = MaterialTheme.shapes.medium,
                    colour = MaterialTheme.colorScheme.primary,
                    text = Pair(stringResource(R.string.retry_quiz), 25),
                    icon = Icons.AutoMirrored.Filled.KeyboardReturn
                )

                QuizelSimpleButton(
                    onClick = {
                        goHome(true)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = MaterialTheme.shapes.medium,
                    colour = MaterialTheme.colorScheme.primary,
                    text = Pair(stringResource(R.string.return_to_home), 25),
                    icon = Icons.Filled.Menu
                )

                TextInputDialog(
                    dialogIsOpen = showChangeNameDialog,
                    dialogOpen = { isOpen -> showChangeNameDialog = isOpen },
                    placeholder = stringResource(R.string.enter_name),
                    response = { name ->
                        changeName(name as String)
                    }
                )


            }
        }
    }
}
