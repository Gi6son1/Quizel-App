package com.cs31620.quizel.ui.takequiz

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.TopLevelNavigationScaffold
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.theme.QuizelTheme

@Composable
fun TakeQuizScreenTopLevel(
    navController: NavHostController
) {
    TakeQuizScreen(navController = navController)
}

@Composable
private fun TakeQuizScreen(navController: NavHostController){
    TopLevelNavigationScaffold(
        navController = navController
    ) { innerPadding ->

        var showProgressBar by rememberSaveable { mutableStateOf(true) }
        var showCurrentScore by rememberSaveable { mutableStateOf(true) }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 100.dp))
        {
            Card(modifier = Modifier.weight(1f)){
                Column(modifier = Modifier.padding(8.dp).fillMaxSize()) {
                    Text(text = "Quiz Options")
                    HorizontalDivider()
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Text(text = "Show Current Score")
                        Switch(checked = showCurrentScore, onCheckedChange = { showCurrentScore = it })
                    }
                    HorizontalDivider()
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Text(text = "Show Progress Bar")
                        Switch(checked = showProgressBar, onCheckedChange = { showProgressBar = it })
                    }

                }

            }

            Button(
                onClick = {
                    val quizSettingsString = "${if (showProgressBar) "1" else "0"},${if (showCurrentScore) "1" else "0"}"


                    val destination = "${Screen.TestQuestions.basePath}${quizSettingsString}"
                    navController.navigate(destination) {
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
                    .weight(1f)
            ) { Text(text = "Begin Quiz") }
        }
    }
}

@Preview
@Composable
private fun TakeQuizScreenPreview(){
    QuizelTheme { TakeQuizScreen(navController = rememberNavController()) }
}