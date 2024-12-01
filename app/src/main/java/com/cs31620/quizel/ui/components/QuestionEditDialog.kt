package com.cs31620.quizel.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun QuestionEditDialog(
    title: String,
    question: String,
    answers: MutableList<Answer>,
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
){


    if (dialogIsOpen) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(modifier = Modifier.fillMaxSize()) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val (dialogTitle,
                        questionTitleInput,
                        questionDescInput,
                        potentialAnswerBox,
                        discardButton,
                        saveButton) = createRefs()

                    Text(
                        modifier = Modifier
                            .constrainAs(dialogTitle){
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        text = "Create/Modify Question"
                    )
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier

                    )
                }
            }
        }
    }
}
