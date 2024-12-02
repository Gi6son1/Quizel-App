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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.R
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.QuestionEditDialog
import com.cs31620.quizel.ui.components.TopLevelScaffold

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuestionBankScreen(navController: NavHostController) {
    TopLevelScaffold(
        navController = navController
    ) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .border(3.dp, Color.Black),
        ) {
            val (title, selectDelete, questionBankArea) = createRefs()
            var showQuestionDialog by rememberSaveable { mutableStateOf(false) }

            val questionObject = Question(-1)

            Text(
                modifier = Modifier
                    .border(3.dp, Color.Black)
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(questionBankArea.top)

                    },
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 90.sp,

                )
            Button(
                onClick = {  },
                modifier = Modifier
                    .constrainAs(selectDelete) {
                        top.linkTo(parent.top)
                        start.linkTo(title.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(questionBankArea.top)
                    }
            ) { }


            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .border(3.dp, Color.Black)
                    .constrainAs(questionBankArea) {
                        top.linkTo(title.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
            ) {
                Button(
                    onClick = {
                        showQuestionDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.1f),
                    shape = RoundedCornerShape(35),
                )
                {
                    Text(text = "New Question")
                }
                Surface(
                    Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .weight(10f),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(5)

                ) {
                    Text(text = "Question Bank Screen", modifier = Modifier.padding(start = 8.dp))
                }
            }

            QuestionEditDialog(
                question = questionObject,
                dialogIsOpen = showQuestionDialog,
                dialogOpen = { isOpen -> showQuestionDialog = !isOpen })
        }
    }
}

@Preview
@Composable
fun QuestionBankScreenPreview() {
    QuestionBankScreen(navController = rememberNavController())
}

