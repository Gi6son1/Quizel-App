package com.cs31620.quizel.ui.takequiz

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardReturn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.ui.components.TopLevelBackgroundScaffold
import com.cs31620.quizel.ui.components.TopLevelNavigationScaffold
import com.cs31620.quizel.ui.components.customcomposables.QuizelSwitch
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.theme.QuizelTheme

@Composable
fun QuizResultsScreenTopLevel(
    navController: NavHostController,
    quizResults: String
) {
    val (finalScore, totalQuestions) = quizResults.split(",").map { it.toInt() }

    QuizResultsScreen(totalQuestions = totalQuestions, finalScore = finalScore,
        restartQuiz = { restart ->
            if (restart) {
                navController.navigate(Screen.TestQuestions.route){
                    launchSingleTop = true
                }
            }
        },
        goHome = { goHome ->
            if (goHome) {
                navController.popBackStack(Screen.TakeQuiz.route, inclusive = false)
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun QuizResultsScreen(
    totalQuestions: Int = 0,
    finalScore: Int = 0,
    restartQuiz: (Boolean) -> Unit = {},
    goHome: (Boolean) -> Unit = {}
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

            Column(
                modifier = Modifier.constrainAs(content) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(text = "Your Quiz Results",
                    modifier = Modifier.fillMaxWidth().wrapContentWidth(),
                    fontSize = 80.sp,
                    style = MaterialTheme.typography.displayLarge)
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
                            text = "You got $finalScore out of $totalQuestions questions correct",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(),
                            fontSize = 30.sp
                        )

                    }
                }
                Button(
                    onClick = {
                        restartQuiz(true)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .shadow(10.dp, MaterialTheme.shapes.medium),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardReturn,
                        contentDescription = "Retry icon",
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Retry Quiz",
                        modifier = Modifier.wrapContentSize(),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    onClick = {
                        goHome(true)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .shadow(10.dp, MaterialTheme.shapes.medium),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu icon",
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Return to Home",
                        modifier = Modifier.wrapContentSize(),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}