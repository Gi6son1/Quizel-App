package com.cs31620.quizel.ui.questionbank

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.R
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.customcomposables.ActionCheckDialog
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.parentscaffolds.TopLevelNavigationScaffold
import com.cs31620.quizel.ui.components.customcomposables.QuizelSimpleButton
import com.cs31620.quizel.ui.navigation.Screen

@Composable
fun QuestionBankScreenTopLevel(
    navController: NavHostController,
    questionsViewModel: QuestionsViewModel
) {
    val questionsList by questionsViewModel.questionsList.observeAsState(listOf())
    QuestionBankScreen(
        navController = navController,
        questionList = questionsList,
        deleteSelectedQuestion = { selectedQuestion ->
            questionsViewModel.deleteSelectedQuestion(selectedQuestion)
        },
        deleteQuestionsById = { selectedQuestionIds ->
            questionsViewModel.deleteQuestionsById(selectedQuestionIds)
        },
        editQuestion = { question ->
            val destination = "${Screen.QuestionEdit.basePath}${question.id}"
            navController.navigate(destination) {
                launchSingleTop = true
            }
        }
    )
}

@Composable
private fun QuestionBankScreen(
    navController: NavHostController,
    questionList: List<Question>,
    deleteSelectedQuestion: (Question?) -> Unit = {},
    deleteQuestionsById: (List<Int>) -> Unit = {},
    editQuestion: (Question) -> Unit = {}
) {
    TopLevelNavigationScaffold(
        navController = navController
    ) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val (selectDelete, questionBankArea) = createRefs()

            var selectedQuestion by rememberSaveable { mutableStateOf<Question?>(null) }

            val checkedQuestionStates = remember {
                val questionIds = questionList.map { it.id }
                val map = mutableStateMapOf<Int, Boolean>()
                for (id in questionIds) {
                    map[id] = false
                }
                map
            }

            var displaySelectDelete by rememberSaveable { mutableStateOf(false) }
            var showDeleteSelectedDialog by rememberSaveable { mutableStateOf(false) }
            var showDeleteQuestionDialog by rememberSaveable { mutableStateOf(false) }

            val state = rememberLazyListState()

            QuizelSimpleButton(
                onClick = {
                    if (displaySelectDelete && checkedQuestionStates.containsValue(true)) showDeleteSelectedDialog =
                        true
                    displaySelectDelete = !displaySelectDelete
                },
                colour = MaterialTheme.colorScheme.error,
                text = if (!displaySelectDelete) Pair(stringResource(R.string.delete), 20) else null,
                modifier = Modifier
                    .constrainAs(selectDelete) {
                        top.linkTo(parent.top, margin = 25.dp)
                        end.linkTo(parent.end, margin = 10.dp)
                        bottom.linkTo(questionBankArea.top, margin = 15.dp)
                    }
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(10.dp),
                icon = if (!displaySelectDelete) Icons.Outlined.CheckBox else null,
                image = if (displaySelectDelete) Pair(
                    painterResource(id = R.drawable.bin_icon),
                    0
                ) else null
            )

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(10.dp)
                    .constrainAs(questionBankArea) {
                        top.linkTo(selectDelete.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
            ) {
                QuizelSimpleButton(
                    colour = MaterialTheme.colorScheme.secondary,
                    text = Pair(stringResource(R.string.new_question), 25),
                    onClick = {
                        displaySelectDelete = false
                        editQuestion(Question())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(35),
                    icon = Icons.Filled.Add
                )

                Surface(
                    Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .weight(10f)
                        .shadow(5.dp, MaterialTheme.shapes.medium),
                    color = MaterialTheme.colorScheme.surfaceDim,
                    shape = MaterialTheme.shapes.medium

                ) {
                    if (questionList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                text = stringResource(R.string.oh_no_empty_question_bank),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(40.dp)
                                    .wrapContentSize(),
                                style = MaterialTheme.typography.displayMedium,
                                textAlign = TextAlign.Center,
                            )
                        }

                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            state = state
                        ) {
                            items(questionList) { question ->
                                Button(
                                    onClick = {
                                        displaySelectDelete = false
                                        editQuestion(question)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp),
                                    shape = MaterialTheme.shapes.medium,
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
                                    contentPadding = PaddingValues(0.dp),
                                    elevation = ButtonDefaults.buttonElevation(10.dp)
                                )
                                {
                                    Row {
                                        if (displaySelectDelete) {
                                            Surface(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .weight(1f),
                                                shape = RectangleShape,
                                                color = MaterialTheme.colorScheme.error
                                            ) {
                                                Checkbox(
                                                    checked = checkedQuestionStates[question.id] == true,
                                                    onCheckedChange = {
                                                        checkedQuestionStates[question.id] = it
                                                    },
                                                    colors = CheckboxDefaults.colors(
                                                        checkedColor = MaterialTheme.colorScheme.surfaceBright,
                                                        uncheckedColor = MaterialTheme.colorScheme.surfaceBright,
                                                        checkmarkColor = MaterialTheme.colorScheme.onSurface
                                                    )
                                                )
                                            }
                                        } else {
                                            Button(
                                                onClick = {
                                                    selectedQuestion = question
                                                    showDeleteQuestionDialog = true
                                                },
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
                                        Column(
                                            modifier = Modifier
                                                .weight(6f)
                                                .fillMaxSize()
                                                .wrapContentHeight()
                                                .padding(horizontal = 5.dp, vertical = 5.dp)
                                        ) {
                                            if (question.title.isNotBlank()) {
                                                Text(
                                                    text = question.title,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            Text(
                                                text = question.description,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                            contentDescription = stringResource(R.string.edit_icon),
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .wrapContentSize()
                                                .weight(1f),
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ActionCheckDialog(dialogIsOpen = showDeleteSelectedDialog,
                dialogOpen = { isOpen -> showDeleteSelectedDialog = isOpen },
                mainActionButton = { onClick, modifier ->
                    Button(
                        onClick = onClick,
                        modifier = modifier,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = stringResource(R.string.delete), fontSize = 19.sp)
                    }
                },
                actionDialogMessage = stringResource(R.string.delete_selected_questions_check),
                performMainAction = { deleteSelectedQuestions ->
                    if (deleteSelectedQuestions) {
                        val selectedQuestionIds =
                            checkedQuestionStates.filter { it.value }.map { it.key }
                                .also { keysToRemove ->
                                    checkedQuestionStates.apply {
                                        keysToRemove.forEach { key ->
                                            remove(key)
                                            Log.d(
                                                "QuestionBankScreen",
                                                "Question $key removed from original graph"
                                            )
                                        }
                                    }
                                }
                        deleteQuestionsById(selectedQuestionIds)
                        Log.d(
                            "QuestionBankScreen",
                            "Questions with ids $selectedQuestionIds deleted"
                        )
                    }
                }
            )

            ActionCheckDialog(dialogIsOpen = showDeleteQuestionDialog,
                dialogOpen = { isOpen -> showDeleteQuestionDialog = isOpen },
                mainActionButton = { onClick, modifier ->
                    Button(
                        onClick = onClick,
                        modifier = modifier,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = stringResource(R.string.delete), fontSize = 19.sp)
                    }
                },
                actionDialogMessage = stringResource(R.string.delete__question_check),
                performMainAction = { deleteQuestion ->
                    if (deleteQuestion) {
                        deleteSelectedQuestion(selectedQuestion)
                        Log.d(
                            "QuestionBankScreen",
                            "Question ${selectedQuestion?.description} deleted"
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun QuestionBankScreenPreview() {
    QuestionBankScreen(navController = rememberNavController(), emptyList())
}
