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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.R
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.customcomposables.ActionCheckDialog
import com.cs31620.quizel.ui.components.Answer
import com.cs31620.quizel.ui.components.customcomposables.InvalidInformationDialog
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.TopLevelBackgroundScaffold
import com.cs31620.quizel.ui.components.customcomposables.QuizelSimpleButton
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
        addNewQuestion = { question ->
            questionsViewModel.addNewQuestion(question)
            Log.d("QuestionBankScreen", "Question ${question?.description} added")
        },
        updateQuestion = { question ->
            questionsViewModel.updateSelectedQuestion(question)
            Log.d("QuestionBankScreen", "Question ${question?.description} updated")
        },
        returnToBank = { doReturn ->
            if (doReturn){
                navController.navigate(Screen.QuestionBank.route){
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
            }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun QuestionEditScreen(
    question: Question?,
    addNewQuestion: (Question?) -> Unit = {},
    updateQuestion: (Question?) -> Unit = {},
    returnToBank: (Boolean) -> Unit = {}
) {
    TopLevelBackgroundScaffold { innerPadding ->
        var title by rememberSaveable(question) { mutableStateOf(question?.title ?: "") }
        val context = LocalContext.current

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
                        text = stringResource(R.string.enter_title_optional),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 22.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium,

            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(2f)
                    .fillMaxWidth()
                    .shadow(5.dp, MaterialTheme.shapes.medium),
                placeholder = { Text(text = stringResource(R.string.enter_question), fontSize = 18.sp) },
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
                            text = stringResource(R.string.potential_answers),
                            modifier = Modifier
                                .weight(1.1f)
                                .fillMaxSize()
                                .padding(top = 5.dp)
                                .wrapContentSize(),
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 43.sp

                        )
                        QuizelSimpleButton(
                            onClick = { showAddAnswerDialog = true },
                            colour = MaterialTheme.colorScheme.secondary,
                            enabled = answers.size < 10,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .weight(0.9f)
                                .fillMaxSize(),
                            icon = Icons.Filled.Add,
                            text = Pair(stringResource(R.string.answer), 22)
                        )
                    }
                    if (answers.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(8f)
                        ) {
                            Text(
                                text = stringResource(R.string.oh_no_empty_potential_answers),
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
                                        .height(60.dp),
                                    elevation = ButtonDefaults.buttonElevation(10.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(Color.White)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.green_tick),
                                            contentDescription = stringResource(R.string.correct_answer_icon),
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
                                            fontSize = 20.sp,
                                            overflow = TextOverflow.Ellipsis,
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
                                                contentDescription = stringResource(R.string.delete_icon),
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
                QuizelSimpleButton(
                    onClick = { showDiscardQuestionDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colour = MaterialTheme.colorScheme.error,
                    text = Pair(stringResource(R.string.discard_changes), 18)
                )
                QuizelSimpleButton(
                    onClick = {
                        if (description.isBlank()) {
                            invalidInfoDialogTitle =
                                context.getString(R.string.no_question_inputted)
                            invalidInfoDialogDescription =
                                context.getString(R.string.please_enter_a_question)
                            showInvalidInfoDialog = true
                            return@QuizelSimpleButton
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
                                returnToBank(true)
                                return@QuizelSimpleButton
                            }
                        }
                        invalidInfoDialogTitle =
                            context.getString(R.string.no_correct_answer_selected)
                        invalidInfoDialogDescription =
                            context.getString(R.string.correct_answer_instructions)
                        showInvalidInfoDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    enabled = answers.isNotEmpty(),
                    colour = MaterialTheme.colorScheme.secondary,
                text = Pair(stringResource(R.string.save_changes), 18)
                )
            }
        }


        BackHandler {
            showDiscardQuestionDialog = true
        }

        TextInputDialog(dialogIsOpen = showAddAnswerDialog,
            dialogOpen = { isOpen -> showAddAnswerDialog = isOpen },
            isAnswer = true,
            placeholder = stringResource(R.string.enter_answer),
            response = { answer -> addAnswerToAnswerList(answers, answer as Answer) })

        ActionCheckDialog(dialogIsOpen = showDiscardQuestionDialog,
            dialogOpen = { isOpen -> showDiscardQuestionDialog = isOpen },
            mainActionButton = { onClick, modifier ->
                QuizelSimpleButton(
                    onClick = onClick,
                    modifier = modifier,
                    colour = MaterialTheme.colorScheme.error,
                    text = Pair(stringResource(R.string.discard_changes), 19)
                )
            },
            actionDialogMessage = stringResource(R.string.discard_any_changes_check),
            performMainAction = { returnToBank(true) }

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
    QuestionEditScreen(question = Question())
}