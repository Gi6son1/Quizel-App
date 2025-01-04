package com.cs31620.quizel.ui.takequiz

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast
import androidx.constraintlayout.compose.atMost
import androidx.navigation.NavHostController
import com.cs31620.quizel.R
import com.cs31620.quizel.model.ScoresViewModel
import com.cs31620.quizel.ui.components.Score
import com.cs31620.quizel.ui.components.parentscaffolds.TopLevelBackgroundScaffold
import com.cs31620.quizel.ui.components.customcomposables.QuizelSimpleButton
import com.cs31620.quizel.ui.components.customcomposables.RecentScoresDisplay
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.components.customcomposables.TextInputDialog

/**
 * The top level for the quiz results screen
 * @param navController the navigation controller
 * @param quizResults the results of the quiz, stored as a string
 * @param scoresViewModel the viewmodel for the scores table
 */
@Composable
fun QuizResultsScreenTopLevel(
    navController: NavHostController,
    quizResults: String,
    scoresViewModel: ScoresViewModel
) {
    val (finalScore, totalQuestions) = quizResults.split(",")
        .map { it.toInt() } //splits the string into the final score and the total questions
    val scoresList by scoresViewModel.scoresList.observeAsState(listOf()) //get the list of recent scores from the viewmodel

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
            val recentScore = scoresList.first() //updates most recent score with the given name
            Log.d("QuizResultsScreenTopLevel", "changeName: $name for score $recentScore")
            scoresViewModel.updateScoreWithUsername(score = recentScore, username = name)
        }
    )
}

/**
 * The quiz results screen
 * @param totalQuestions the total number of questions in the quiz
 * @param finalScore the final score of the quiz
 * @param restartQuiz the function to restart the quiz
 * @param goHome the function to go home
 * @param recentScores the list of recent scores
 * @param changeName the function to change the name of the user
 */
@Composable
private fun QuizResultsScreen(
    totalQuestions: Int = 0,
    finalScore: Int = 0,
    restartQuiz: (Boolean) -> Unit = {},
    goHome: (Boolean) -> Unit = {},
    recentScores: List<Score>,
    changeName: (String) -> Unit = {},
) {
    TopLevelBackgroundScaffold(showTitle = false) //hides the app title
    { innerPadding ->

        BackHandler { //if user uses back button, go back to menu
            goHome(true)
        }

        var showChangeNameDialog by rememberSaveable { mutableStateOf(false) } //whether to show the change name dialog

        ConstraintLayout( //holds the components of the screen
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                .wrapContentSize()

        ) {
            val (title, results, changeNameButton, retryButton, menuButton) = createRefs() //holds the references to the components

            Text( //title of the screen
                text = stringResource(R.string.your_quiz_results),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(results.top)
                    }
                    .wrapContentWidth(),
                fontSize = 80.sp,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            ScoreArea( //holds the user's score and recent scores
                modifier = Modifier.constrainAs(results) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(changeNameButton.top, margin = 10.dp)
                    height = Dimension.preferredWrapContent
                },
                finalScore = finalScore,
                totalQuestions = totalQuestions,
                scoreList = recentScores
            )

            QuizelSimpleButton( //button to change name
                onClick = { showChangeNameDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(changeNameButton) {
                        top.linkTo(results.bottom)
                        bottom.linkTo(retryButton.top, margin = 10.dp)
                        height = Dimension.fillToConstraints
                            .atLeast(70.dp)
                            .atMost(70.dp)
                    },
                shape = MaterialTheme.shapes.medium,
                colour = MaterialTheme.colorScheme.surfaceDim,
                text = Pair(stringResource(R.string.change_name), 25),
                icon = Icons.Filled.Edit
            )

            QuizelSimpleButton( //button to retry quiz
                onClick = {
                    restartQuiz(true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(retryButton) {
                        top.linkTo(changeNameButton.bottom)
                        bottom.linkTo(menuButton.top, margin = 10.dp)
                        height = Dimension.fillToConstraints
                            .atLeast(70.dp)
                            .atMost(70.dp)
                    },
                shape = MaterialTheme.shapes.medium,
                colour = MaterialTheme.colorScheme.primary,
                text = Pair(stringResource(R.string.retry_quiz), 25),
                icon = Icons.AutoMirrored.Filled.KeyboardReturn
            )

            QuizelSimpleButton( //button to go home
                onClick = {
                    goHome(true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(menuButton) {
                        top.linkTo(retryButton.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                            .atLeast(70.dp)
                            .atMost(70.dp)
                    },
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

/**
 * The score area of the quiz results screen
 * @param modifier the modifier for the score area
 * @param finalScore the final score of the quiz
 * @param totalQuestions the total number of questions in the quiz
 * @param scoreList the list of recent scores
 */
@Composable
private fun ScoreArea(
    modifier: Modifier,
    finalScore: Int,
    totalQuestions: Int,
    scoreList: List<Score>
) {
    Card(
        modifier = modifier.shadow(
            10.dp,
            MaterialTheme.shapes.medium
        ) //holds the score and recentScore area
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = "${(finalScore / totalQuestions.toFloat() * 100).toInt()}%", //displayes score as percentage
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(),
                fontSize = 60.sp
            )

            Text(
                text = stringResource(
                    R.string.you_got_out_of_questions_correct, //displays final score with respect to total questions
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
                //recent scores title
                text = stringResource(R.string.recent_scores),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 35.sp,
            )
            RecentScoresDisplay(scoresList = scoreList) //displays the recent scores
            Text(
                text = stringResource(
                    R.string.your_score_is_saved_under,
                    if (!scoreList.isEmpty())
                        scoreList.first().username
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
}