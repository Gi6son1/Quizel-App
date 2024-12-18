package com.cs31620.quizel.ui.questionbank

import android.annotation.SuppressLint
import android.app.Application
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
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
import com.cs31620.quizel.datasource.QuizelRepository
import com.cs31620.quizel.model.QuestionsViewModel
import com.cs31620.quizel.ui.components.ActionCheckDialog
import com.cs31620.quizel.ui.components.Answer
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.TopLevelScaffold
import java.time.LocalDateTime
import kotlin.collections.remove

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
        addNewQuestion = { question ->
            questionsViewModel.addNewQuestion(question)
        },
        updateQuestion = { question ->
            questionsViewModel.updateSelectedQuestion(question)
        },
        deleteQuestionsById = { selectedQuestionIds ->
            questionsViewModel.deleteQuestionsById(selectedQuestionIds)
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuestionBankScreen(
    navController: NavHostController,
    questionList: List<Question>,
    deleteSelectedQuestion: (Question?) -> Unit = {},
    updateQuestion: (Question?) -> Unit = {},
    addNewQuestion: (Question?) -> Unit = {},
    deleteQuestionsById: (List<Int>) -> Unit = {}
) {
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
                    if (displaySelectDelete && checkedQuestionStates.containsValue(true)) showDeleteSelectedDialog =
                        true
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
                                    selectedQuestion = question
                                    showQuestionDialog = true
                                    Log.d(
                                        "QuestionBankScreen",
                                        "Question ${question.title} selected"
                                    )
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
                                                    checkedColor = Color.White,
                                                    uncheckedColor = Color.White,
                                                    checkmarkColor = Color.Black
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
                                        if (question.title.isNotBlank()) {
                                            Text(
                                                text = question.title,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color.Black,
                                            )
                                        }
                                        Text(
                                            text = question.description,
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

            QuestionEditDialog(
                question = selectedQuestion,
                dialogIsOpen = showQuestionDialog,
                dialogOpen = { isOpen ->
                    showQuestionDialog = isOpen

                    if (!isOpen) selectedQuestion = null
                },
                updateQuestion = { question ->
                    updateQuestion(question)
                    Log.d("QuestionBankScreen", "Question ${question?.description} updated")
                },
                addNewQuestion = { question ->
                    addNewQuestion(question)
                    Log.d("QuestionBankScreen", "Question ${question?.description} added")
                }
            )

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
                performMainAction = { deleteSelectedQuestions ->
                    if (deleteSelectedQuestions) {
                        val selectedQuestionIds =
                            checkedQuestionStates.filter { it.value }.map { it.key }
                                .also { keysToRemove ->
                                    checkedQuestionStates.apply {
                                        keysToRemove.forEach { key ->
                                            remove(key)
                                            Log.d("QuestionBankScreen", "Question $key removed from original graph")
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
                        Text(text = "Delete", fontSize = 19.sp)
                    }
                },
                actionDialogMessage = "Are you sure you want to delete this question?",
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
fun QuestionBankScreenPreview() {
    QuestionBankScreen(navController = rememberNavController(), emptyList())
}
