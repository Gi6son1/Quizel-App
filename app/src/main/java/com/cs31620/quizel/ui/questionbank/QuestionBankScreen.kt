package com.cs31620.quizel.ui.questionbank

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.R
import com.cs31620.quizel.ui.components.ActionCheckDialog
import com.cs31620.quizel.ui.components.Answer
import com.cs31620.quizel.ui.components.Question
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
        ) {
            val (title, selectDelete, questionBankArea) = createRefs()
            var showQuestionDialog by rememberSaveable { mutableStateOf(false) }

            var selectedQuestion by rememberSaveable { mutableStateOf<Question?>(null) }


            var displaySelectDelete by rememberSaveable { mutableStateOf(false) }
            var showDeleteSelectedDialog by rememberSaveable { mutableStateOf(false) }
            var showDeleteQuestionDialog by rememberSaveable { mutableStateOf(false) }

            val questions = mutableListOf(
                Pair(
                    Question(
                        _title = "i'm a title",
                        _description = "i'm a description",
                        _answers = mutableListOf(Answer("yieeeeee", false), Answer("cheese", true))
                    ), false
                ),
                Pair(Question(_description = "i'm a description"), false),
                Pair(Question(_description = "i'm a description"), false),
                Pair(Question(_title = "i'm a title", _description = "i'm a description"), false),
                Pair(
                    Question(
                        _description = "i'm a description",
                        _answers = mutableListOf(Answer("yipee", true), Answer("ydee", false))
                    ), false
                )
            )

            Text(
                modifier = Modifier
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
                onClick = {
                    if (displaySelectDelete && containsSelectedQuestion(questions)) showDeleteSelectedDialog = true
                    displaySelectDelete = !displaySelectDelete
                },
                modifier = Modifier
                    .constrainAs(selectDelete) {
                        top.linkTo(parent.top)
                        start.linkTo(title.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(questionBankArea.top)
                    }
                    .height(50.dp)
                    .shadow(10.dp, MaterialTheme.shapes.medium),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                if (displaySelectDelete) {
                    Image(
                        painter = painterResource(id = R.drawable.bin_icon),
                        contentDescription = "Bin icon",
                    )
                } else {
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.CheckBox,
                            contentDescription = "Select Icon",
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Delete",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentSize()
                        )
                    }
                }
            }


            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .constrainAs(questionBankArea) {
                        top.linkTo(title.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
            ) {
                Button(
                    onClick = {
                        selectedQuestion = Question()
                        showQuestionDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.1f)
                        .shadow(5.dp, RoundedCornerShape(35)),
                    shape = RoundedCornerShape(35),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                )
                {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add question"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "New Question", fontSize = 25.sp)
                }
                Surface(
                    Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .weight(10f)
                        .shadow(5.dp, MaterialTheme.shapes.medium),
                    color = Color.LightGray,
                    shape = MaterialTheme.shapes.medium

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (questionPair in questions) {
                            Button(
                                onClick = {
                                    selectedQuestion = questionPair.first
                                    showQuestionDialog = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(5.dp, MaterialTheme.shapes.medium)
                                    .height(70.dp),
                                shape = MaterialTheme.shapes.medium,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                                contentPadding = PaddingValues(0.dp)
                            )
                            {
                                Row {
                                    if (displaySelectDelete){
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .weight(1f),
                                            shape = RectangleShape,
                                            color = MaterialTheme.colorScheme.error
                                        ) {
                                            Checkbox(
                                                checked = questionPair.second,
                                                onCheckedChange = {
                                                    //questionPair.second = it
                                                },
                                                colors = CheckboxDefaults.colors(checkedColor = Color.White,
                                                    uncheckedColor = Color.White)
                                            )
                                        }
                                    } else {
                                        Button(
                                            onClick = { showDeleteQuestionDialog = true },
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .weight(1f),
                                            shape = RectangleShape,
                                            contentPadding = PaddingValues(0.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                MaterialTheme.colorScheme.error
                                            )
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.bin_icon),
                                                contentDescription = "Bin icon",
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .fillMaxSize()
                                            )
                                        }
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(6f)
                                            .fillMaxSize()
                                            .wrapContentHeight()
                                            .padding(horizontal = 5.dp, vertical = 5.dp)
                                    ) {
                                        if (questionPair.first.title.isNotBlank()) {
                                            Text(
                                                text = questionPair.first.title,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color.Black,
                                            )
                                        }
                                        Text(
                                            text = questionPair.first.description,
                                            color = Color.Black
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                        contentDescription = "Arrow right",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .wrapContentSize()
                                            .weight(1f),
                                        tint = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }

            LaunchedEffect(selectedQuestion) {
                if (selectedQuestion != null) {
                    showQuestionDialog = true
                }
            }

            QuestionEditDialog(
                question = selectedQuestion,
                dialogIsOpen = showQuestionDialog,
                dialogOpen = { isOpen ->
                    showQuestionDialog = isOpen

                    if (!isOpen) selectedQuestion = null
                })

            ActionCheckDialog(dialogIsOpen = showDeleteSelectedDialog,
                dialogOpen = { isOpen -> showDeleteSelectedDialog = isOpen },
                mainActionButton = { onClick, modifier ->
                    Button(
                        onClick = onClick,
                        modifier = modifier,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Delete Selected Questions", fontSize = 19.sp)
                    }
                },
                actionDialogMessage = "Are you sure you want to delete the selected questions?",
                performMainAction = { /** TODO deletee questions **/ }
            )

            ActionCheckDialog(dialogIsOpen = showDeleteQuestionDialog,
                dialogOpen = { isOpen -> showDeleteQuestionDialog = isOpen },
                mainActionButton = { onClick, modifier ->
                    Button(
                        onClick = onClick,
                        modifier = modifier,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Delete", fontSize = 19.sp)
                    }
                },
                actionDialogMessage = "Are you sure you want to delete this question?",
                performMainAction = { /** TODO delete questions **/ }
            )
        }
    }
}

@Preview
@Composable
fun QuestionBankScreenPreview() {
    QuestionBankScreen(navController = rememberNavController())
}

fun containsSelectedQuestion(questions: MutableList<Pair<Question, Boolean>>): Boolean {
    questions.forEach { question ->
        if (question.second ) return true
    }
    return false
}