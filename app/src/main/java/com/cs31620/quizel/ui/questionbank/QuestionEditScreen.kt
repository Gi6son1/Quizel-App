package com.cs31620.quizel.ui.questionbank

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.R
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.ActionCheckDialog
import com.cs31620.quizel.ui.components.Answer
import com.cs31620.quizel.ui.components.InvalidInformationDialog
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.TopLevelBackgroundScaffold
import com.cs31620.quizel.ui.navigation.Screen

@Composable
fun QuestionEditScreenTopLevel(
    navController: NavHostController,
    questionsViewModel: QuestionsViewModel,
    questionId: Int? = null
) {
    val retrievedQuestion by  questionsViewModel.getQuestionById(questionId).observeAsState(Question())
    Log.d("QuestionEditScreenTopLevel", "QuestionId: $questionId")
    QuestionEditScreen(
        question = retrievedQuestion,
        navController = navController,
        addNewQuestion = { question ->
            questionsViewModel.addNewQuestion(question)
            Log.d("QuestionBankScreen", "Question ${question?.description} added")
        },
        updateQuestion = { question ->
            questionsViewModel.updateSelectedQuestion(question)
            Log.d("QuestionBankScreen", "Question ${question?.description} updated")
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuestionEditScreen(
    question: Question?,
    navController: NavHostController,
    addNewQuestion: (Question?) -> Unit = {},
    updateQuestion: (Question?) -> Unit = {}
) {
    TopLevelBackgroundScaffold { innerPadding ->
        var title by rememberSaveable(question) { mutableStateOf(question?.title ?: "") }

        var description by rememberSaveable(question) {
            mutableStateOf(
                question?.description ?: ""
            )
        }

        val answers = remember(question) {
            mutableStateListOf<Answer>().also { list ->
                question?.answers?.let { answers ->
                    list.addAll(answers)
                }
            }
        }

        var showDiscardQuestionDialog by rememberSaveable { mutableStateOf(false) }
        var showAddAnswerDialog by rememberSaveable { mutableStateOf(false) }
        var showInvalidInfoDialog by rememberSaveable { mutableStateOf(false) }

        var invalidInfoDialogTitle by rememberSaveable { mutableStateOf("") }
        var invalidInfoDialogDescription by rememberSaveable { mutableStateOf("") }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .shadow(5.dp, MaterialTheme.shapes.medium),
                singleLine = true,
                textStyle = TextStyle.Default.copy(
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                ),
                placeholder = {
                    Text(
                        text = "Enter title (Optional)",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 22.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium

            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(2f)
                    .fillMaxWidth()
                    .shadow(5.dp, MaterialTheme.shapes.medium),
                placeholder = { Text(text = "Enter question", fontSize = 18.sp) },
                textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .weight(8f)
                    .padding(bottom = 10.dp)
                    .shadow(5.dp, MaterialTheme.shapes.medium),
                color = Color.LightGray,
                shape = MaterialTheme.shapes.medium
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(5.dp)
                    ) {
                        Text(
                            text = "Potential Answers",
                            modifier = Modifier
                                .weight(1.1f)
                                .fillMaxSize()
                                .padding(top = 5.dp)
                                .wrapContentSize(),
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 43.sp

                        )
                        Button(
                            onClick = { showAddAnswerDialog = true },
                            modifier = Modifier
                                .weight(0.9f)
                                .fillMaxSize()
                                .shadow(
                                    if (answers.size < 10) 5.dp else 0.dp,
                                    MaterialTheme.shapes.medium
                                ),
                            enabled = answers.size < 10,
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add Answer",
                                modifier = Modifier.size(25.dp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(text = "Answer", fontSize = 22.sp)
                        }
                    }
                    if (answers.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(8f)
                        ) {
                            Text(
                                text = "Oh no, empty!\nAdd an answer using the\n+ Answer button",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(40.dp)
                                    .wrapContentSize(),
                                style = MaterialTheme.typography.displayMedium,
                                textAlign = TextAlign.Center,
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 5.dp, end = 5.dp, top = 12.dp)
                                .verticalScroll(rememberScrollState())
                                .weight(8f),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            answers.forEachIndexed { index, answer ->
                                Button(
                                    onClick = { setCorrectAnswer(answers, index) },
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .shadow(5.dp, MaterialTheme.shapes.medium),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(Color.White)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.green_tick),
                                            contentDescription = "Green Tick",
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxSize(),
                                            alpha = if (answer.isCorrect) 1f else 0f
                                        )
                                        Text(
                                            text = answer.text, color = Color.Black,
                                            modifier = Modifier
                                                .weight(6f)
                                                .fillMaxSize()
                                                .wrapContentHeight(),
                                            fontSize = 20.sp
                                        )
                                        Button(
                                            onClick = { answers.remove(answer) },
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
                                }
                            }
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Button(
                    onClick = { showDiscardQuestionDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .shadow(5.dp, ButtonDefaults.shape),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = "Discard Changes", fontSize = 18.sp)
                }
                Button(
                    onClick = {
                        if (description.isBlank()) {
                            invalidInfoDialogTitle = "No Question Inputted"
                            invalidInfoDialogDescription = "Please enter a question"
                            showInvalidInfoDialog = true
                            return@Button
                        }

                        answers.forEach { answer ->
                            if (answer.isCorrect) {
                                if (question == null) {
                                    addNewQuestion(
                                        Question(
                                            title = title,
                                            description = description,
                                            answers = answers
                                        )
                                    )
                                } else {
                                    updateQuestion(
                                        Question(
                                            id = question.id,
                                            title = title,
                                            description = description,
                                            answers = answers
                                        )
                                    )
                                }
                                navController.navigate(Screen.QuestionBank.route) {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                }
                                return@Button
                            }
                        }
                        invalidInfoDialogTitle = "No Correct Answer Selected"
                        invalidInfoDialogDescription =
                            "A question but have one correct answer. Either add a new answer or tap an " +
                                    "existing answer to set it as correct"
                        showInvalidInfoDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .shadow(if (answers.isNotEmpty()) 5.dp else 0.dp, ButtonDefaults.shape),
                    enabled = answers.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Save Changes", fontSize = 18.sp)
                }
            }
        }


        BackHandler {
            showDiscardQuestionDialog = true
        }

        AddAnswerDialog(dialogIsOpen = showAddAnswerDialog,
            dialogOpen = { isOpen -> showAddAnswerDialog = isOpen },
            answer = { answer -> addAnswerToAnswerList(answers, answer) })

        ActionCheckDialog(dialogIsOpen = showDiscardQuestionDialog,
            dialogOpen = { isOpen -> showDiscardQuestionDialog = isOpen },
            mainActionButton = { onClick, modifier ->
                Button(
                    onClick = onClick,
                    modifier = modifier,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = "Discard Changes", fontSize = 19.sp)
                }
            },
            actionDialogMessage = "Are you sure you want to discard any changes?",
            performMainAction = { navController.navigate(Screen.QuestionBank.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            } }
        )

        InvalidInformationDialog(
            dialogIsOpen = showInvalidInfoDialog,
            dialogOpen = { isOpen -> showInvalidInfoDialog = isOpen },
            title = invalidInfoDialogTitle,
            description = invalidInfoDialogDescription
        )
    }
}

private fun setCorrectAnswer(answers: SnapshotStateList<Answer>, answerIndex: Int) {
    val answer = answers[answerIndex]
    answers.forEach { it.isCorrect = false }
    val index = answers.indexOf(answer)
    if (index != -1) {
        answers[answerIndex] = answer.copy(isCorrect = true)
    }
}

private fun addAnswerToAnswerList(answers: SnapshotStateList<Answer>, answer: Answer) {
    if (answers.size < 10) {
        answers.add(answer)
        if (answer.isCorrect) {
            setCorrectAnswer(answers, answers.size - 1)
        }
    }
}

@Preview
@Composable
fun QuestionEditScreenPreview() {
    QuestionEditScreen(question = Question(), navController = rememberNavController())
}