package com.cs31620.quizel.ui.questionbank

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast
import androidx.constraintlayout.compose.atMost
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.cs31620.quizel.R
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.customcomposables.ActionCheckDialog
import com.cs31620.quizel.ui.components.Answer
import com.cs31620.quizel.ui.components.customcomposables.InvalidInformationDialog
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.parentscaffolds.TopLevelBackgroundScaffold
import com.cs31620.quizel.ui.components.customcomposables.QuizelSimpleButton
import com.cs31620.quizel.ui.components.customcomposables.TextInputDialog
import com.cs31620.quizel.ui.navigation.Screen

/***
 * The top level for the question edit screen
 * @param navController the navigation controller
 * @param questionsViewModel the viewmodel for the questions table
 * @param questionId the id of the question being edited, defaulted to null (for error handling)
 */
@Composable
fun QuestionEditScreenTopLevel(
    navController: NavHostController,
    questionsViewModel: QuestionsViewModel,
    questionId: Int? = null
) {
    val retrievedQuestion by questionsViewModel.getQuestionById(questionId)
        .observeAsState(Question()) //retrieves question from viewmodel
    Log.d("QuestionEditScreenTopLevel", "QuestionId: $questionId")
    QuestionEditScreen(
        question = retrievedQuestion,
        addNewQuestion = { question ->
            questionsViewModel.addNewQuestion(question)
        },
        updateQuestion = { question ->
            questionsViewModel.updateSelectedQuestion(question)
        },
        returnToBank = { doReturn ->
            if (doReturn) {
                navController.navigate(Screen.QuestionBank.route) {
                    popUpTo(navController.graph.findStartDestination().id) //pops up backstack until it returns to question bank screen
                    launchSingleTop = true
                }
            }
        }
    )
}

/**
 * Question edit screen
 * @param question the question to be edited, could be null if a new question is being added
 * @param addNewQuestion the function to add a new question
 * @param updateQuestion the function to update a question
 * @param returnToBank the function to return to the question bank screen
 */
@Composable
private fun QuestionEditScreen(
    question: Question?,
    addNewQuestion: (Question) -> Unit = {},
    updateQuestion: (Question) -> Unit = {},
    returnToBank: (Boolean) -> Unit = {}
) {
    TopLevelBackgroundScaffold { innerPadding ->
        var title by rememberSaveable(question) { mutableStateOf(question?.title ?: "") } //holds the title of the question
        val context = LocalContext.current //used to get strings

        var description by rememberSaveable(question) { //holds the question description
            mutableStateOf(
                question?.description ?: ""
            )
        }

        val answers = remember(question) { //holds the list of answers for the question
            mutableStateListOf<Answer>().also { list ->
                question?.answers?.let { answers ->
                    list.addAll(answers)
                }
            }
        }

        var showDiscardQuestionDialog by rememberSaveable { mutableStateOf(false) } //variable to hold whether to show the discard question dialog
        var showAddAnswerDialog by rememberSaveable { mutableStateOf(false) } //variable to hold whether to show the add answer dialog
        var showInvalidInfoDialog by rememberSaveable { mutableStateOf(false) } //variable to hold whether to show the invalid info dialog

        var invalidInfoDialogTitle by rememberSaveable { mutableStateOf("") } //variable to hold the title of the invalid info dialog
        var invalidInfoDialogDescription by rememberSaveable { mutableStateOf("") } //variable to hold the description of the invalid info dialog


        ConstraintLayout( //holds the components of the screen
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 100.dp)
        ) {
            val (titleBox, descriptionBox, answerBox, buttonRow) = createRefs()

            OutlinedTextField( //title box
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, MaterialTheme.shapes.medium)
                    .constrainAs(titleBox) {
                        top.linkTo(parent.top)
                        bottom.linkTo(descriptionBox.top, margin = 8.dp)
                    },
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
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright
                ),
                shape = MaterialTheme.shapes.medium,

                )
            OutlinedTextField( //description box
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, MaterialTheme.shapes.medium)
                    .height(120.dp)
                    .constrainAs(descriptionBox) {
                        top.linkTo(titleBox.bottom)
                        bottom.linkTo(answerBox.top, margin = 8.dp)
                    },
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_question),
                        fontSize = 18.sp
                    )
                },
                textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright
                ),
                shape = MaterialTheme.shapes.medium
            )
            Surface( //answer box
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, MaterialTheme.shapes.medium)
                    .constrainAs(answerBox) {
                        top.linkTo(descriptionBox.bottom)
                        bottom.linkTo(buttonRow.top, margin = 8.dp)
                        height = Dimension.preferredWrapContent
                    },
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = MaterialTheme.shapes.medium
            ) {
                Column { //holds the components of the answer box
                    Row( //holds the title and add answer button
                        modifier = Modifier
                            .height(60.dp)
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
                            enabled = answers.size < 10, //only enabled when there's less than 10 answers
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .weight(0.9f)
                                .fillMaxSize(),
                            icon = Icons.Filled.Add,
                            text = Pair(stringResource(R.string.answer), 22)
                        )
                    }
                    if (answers.isEmpty()) { //if there are no answers, display a message
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
                                textAlign = TextAlign.Center
                            )
                        }
                    } else { //otherwise display the answers
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 5.dp, end = 5.dp, top = 12.dp)
                                .verticalScroll(rememberScrollState())
                                .weight(8f),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            answers.forEachIndexed { index, answer -> //each answer is a button, on click -> sets as correct answer
                                Button(
                                    onClick = { setCorrectAnswer(answers, index) },
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    elevation = ButtonDefaults.buttonElevation(10.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceBright)
                                ) {
                                    Row( //holds the correct answer icon, answer text, and delete button
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
                                            text = answer.text, color = MaterialTheme.colorScheme.onSurface,
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
                                                    .fillMaxSize(),
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.surfaceBright)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Row( //holds the save and discard buttons
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(buttonRow) {
                        top.linkTo(answerBox.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                            .atLeast(70.dp)
                            .atMost(70.dp)
                    }
            ) {
                QuizelSimpleButton( //discard button, on click -> show discard question dialog
                    onClick = { showDiscardQuestionDialog = true },
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    colour = MaterialTheme.colorScheme.error,
                    text = Pair(stringResource(R.string.discard_changes), 18)
                )
                QuizelSimpleButton( //save button, on click -> save question and return to bank
                    onClick = {
                        if (description.isBlank()) { //if there is no question description, show invalid info dialog
                            invalidInfoDialogTitle =
                                context.getString(R.string.no_question_inputted)
                            invalidInfoDialogDescription =
                                context.getString(R.string.please_enter_a_question)
                            showInvalidInfoDialog = true
                            return@QuizelSimpleButton
                        }

                        answers.forEach { answer -> //if there is no correct answer, show invalid info dialog
                            if (answer.isCorrect) { //if there is a correct answer, save the question
                                if (question == null) { //original question = null -> new question
                                    addNewQuestion(
                                        Question(
                                            title = title,
                                            description = description,
                                            answers = answers
                                        )
                                    )
                                } else { //original question != null -> update question
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
                    enabled = answers.isNotEmpty(), //only enabled when there are answers
                    colour = MaterialTheme.colorScheme.secondary,
                    text = Pair(stringResource(R.string.save_changes), 18)
                )
            }
        }


        BackHandler { //if back button is pressed, show discard question dialog
            showDiscardQuestionDialog = true
        }

        TextInputDialog(dialogIsOpen = showAddAnswerDialog, //add answer dialog
            dialogOpen = { isOpen -> showAddAnswerDialog = isOpen },
            isAnswer = true,
            placeholder = stringResource(R.string.enter_answer),
            response = { answer -> addAnswerToAnswerList(answers, answer as Answer) }) //add answer to answer list once saved

        ActionCheckDialog(dialogIsOpen = showDiscardQuestionDialog, //discard question dialog
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
            performMainAction = { returnToBank(true) } //if main action confirmed, go to question bank

        )

        InvalidInformationDialog( //invalid information dialog used when there is no correct answer selected or question entered
            dialogIsOpen = showInvalidInfoDialog,
            dialogOpen = { isOpen -> showInvalidInfoDialog = isOpen },
            title = invalidInfoDialogTitle,
            description = invalidInfoDialogDescription
        )
    }
}

/**
 * Small method for setting a answer as correct in the answer list,
 * sets all of the other answers as incorrect
 */
private fun setCorrectAnswer(answers: SnapshotStateList<Answer>, answerIndex: Int) {
    val answer = answers[answerIndex]
    answers.forEach { it.isCorrect = false }
    val index = answers.indexOf(answer)
    if (index != -1) {
        answers[answerIndex] = answer.copy(isCorrect = true)
    }
}

/**
 * Small method for adding an answer to the answer list
 * Only adds it when the answer list is not full
 */
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