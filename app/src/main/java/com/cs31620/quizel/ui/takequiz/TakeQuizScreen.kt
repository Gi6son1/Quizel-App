package com.cs31620.quizel.ui.takequiz

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.TopLevelNavigationScaffold
import com.cs31620.quizel.ui.components.customcomposables.QuizelSimpleButton
import com.cs31620.quizel.ui.components.customcomposables.QuizelSwitch
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.theme.QuizelTheme

@Composable
fun TakeQuizScreenTopLevel(
    navController: NavHostController
) {
    TakeQuizScreen(navController = navController,
        beginQuiz = { quizSettingsString ->
            val destination = "${Screen.TestQuestions.basePath}${quizSettingsString}"
            navController.navigate(destination) {
                launchSingleTop = true
            }
            })
}

@Composable
private fun TakeQuizScreen(navController: NavHostController,
                           beginQuiz: (String) -> Unit = {}) {
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
                Card(modifier = Modifier.shadow(10.dp, MaterialTheme.shapes.medium)) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Quiz Options",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(),
                            fontSize = 60.sp
                        )

                        Row(modifier = Modifier.padding(horizontal = 10.dp).height(50.dp)) {
                            Text(text = "Show Current Score", modifier = Modifier.fillMaxHeight().wrapContentHeight()
                            , fontSize = 20.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            QuizelSwitch(
                                checked = showCurrentScore,
                                onCheckedChange = { showCurrentScore = it })
                        }

                        HorizontalDivider()

                        Row(modifier = Modifier.padding(horizontal = 10.dp).height(50.dp)) {
                            Text(text = "Show Progress Bar", modifier = Modifier.fillMaxHeight().wrapContentHeight()
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
                    modifier = Modifier.fillMaxWidth().height(70.dp),
                    shape = MaterialTheme.shapes.medium,
                    colour = MaterialTheme.colorScheme.primary,
                    text = Pair("Begin Quiz", 25),
                    icon = Icons.AutoMirrored.Filled.Send
                )
            }
        }
    }
}


@Preview
@Composable
private fun TakeQuizScreenPreview() {
    QuizelTheme { TakeQuizScreen(navController = rememberNavController()) }
}