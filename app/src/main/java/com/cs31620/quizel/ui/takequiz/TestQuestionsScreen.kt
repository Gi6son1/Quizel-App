package com.cs31620.quizel.ui.takequiz

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.TopLevelBackgroundScaffold

@Composable
fun TestQuestionsScreenTopLevel(
    navController: NavHostController,
    questionsViewModel: QuestionsViewModel
) {
    val questionsList by questionsViewModel.questionsList.observeAsState(listOf())
    TestQuestionsScreen(
        questionList = questionsList)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TestQuestionsScreen(questionList: List<Question>){
    TopLevelBackgroundScaffold(
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.Transparent
        ) {
            Text(text = "YIPEEEEEEE", modifier = Modifier.padding(start = 8.dp))
        }
    }
}