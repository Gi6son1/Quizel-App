package com.cs31620.quizel.ui.questionbank

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.ui.components.TopLevelScaffold
import com.cs31620.quizel.ui.theme.QuizelTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuestionBankScreen(navController : NavHostController){
    TopLevelScaffold(
        navController = navController
    ) { innerPadding ->
        Surface(
           modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Text(text = "Question Bank Screen", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Preview
@Composable
fun QuestionBankScreenPreview(){
    QuizelTheme { QuestionBankScreen(navController = rememberNavController()) }
}