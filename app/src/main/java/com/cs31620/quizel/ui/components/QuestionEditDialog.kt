package com.cs31620.quizel.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun QuestionEditDialog(
    question: Question,
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
) {
    var title by rememberSaveable { mutableStateOf(question.title) }

    var description by rememberSaveable { mutableStateOf(question.description) }

    if (dialogIsOpen) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            var showDiscardQuestionDialog by rememberSaveable { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .border(3.dp, Color.Black)
                            .weight(1f)
                            .fillMaxWidth(),
                        text = "Create/Modify Question",
                        style = MaterialTheme.typography.titleLarge
                    )
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .border(3.dp, Color.Black)
                            .weight(1f)
                            .fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text(text = "Enter title (Optional)") }
                    )
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .border(3.dp, Color.Black)
                            .weight(2f)
                            .fillMaxWidth(),
                        placeholder = { Text(text = "Enter question") }
                    )
                    Surface(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                            .border(3.dp, Color.Black)
                            .weight(8f),
                        color = Color.Gray
                    ) {
                        Text(text = "Answers")
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .border(3.dp, Color.Black)
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Button(
                            onClick = { showDiscardQuestionDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(text = "Discard Changes")
                        }
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(text = "Save Changes")
                        }
                    }
                }
            }

            ActionCheckDialog(dialogIsOpen = showDiscardQuestionDialog,
                dialogOpen = { isOpen -> showDiscardQuestionDialog = isOpen },
                mainActionButton = { onClick, modifier ->
                    Button(
                        onClick = onClick,
                        modifier = modifier,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Discard Changes", fontSize = 21.sp)
                    }
                },
                actionDialogMessage = "Are you sure you want to discard any changes?",
                performMainAction = { closeQuestionDialog -> dialogOpen(closeQuestionDialog) })
        }
    }
}