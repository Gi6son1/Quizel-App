package com.cs31620.quizel.ui.questionbank

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.ui.components.TopLevelScaffold

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuestionBankScreen(navController: NavHostController) {
    TopLevelScaffold(
        navController = navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(10.dp)
                .padding(innerPadding),
            color = Color.Transparent
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Button(onClick = {}, Modifier.weight(1.1f)
                    .fillMaxWidth())
                {
                    Text(text = "New Question")
                }
                Surface(
                    Modifier.weight(10f)
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5)),
                    color = MaterialTheme.colorScheme.surfaceContainer,

                    ) {
                    Text(text = "Question Bank Screen", modifier = Modifier.padding(start = 8.dp))
                }

            }

        }
    }
}

@Preview
@Composable
fun QuestionBankScreenPreview() {
    QuestionBankScreen(navController = rememberNavController())
}