package com.cs31620.quizel.ui.questionbank

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cs31620.quizel.ui.components.Answer
import com.cs31620.quizel.ui.components.getDialogWindow

@Composable
fun AddAnswerDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    answer: (Answer) -> Unit = {}
) {
    if (dialogIsOpen) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val dialogWindow = getDialogWindow()

            SideEffect {
                dialogWindow.let { window ->
                    window?.setDimAmount(0.8f)
                }
            }

            var inputText by rememberSaveable { mutableStateOf("") }
            var toggleState by rememberSaveable { mutableStateOf(false) }

            BackHandler {
                dialogOpen(false)
            }

            Column(
                modifier = Modifier
                    .width(350.dp)
                    .height(225.dp)
            ) {
                Card(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .weight(1.5f)
                        .shadow(5.dp, MaterialTheme.shapes.medium),

                ) {
                    Column {
                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1.1f)
                                .wrapContentHeight(align = Alignment.Top),
                            textStyle = TextStyle.Default.copy(fontSize = 23.sp),
                            placeholder = { Text(text = "Enter Answer", fontSize = 23.sp) },
                            singleLine = true
                        )
                        Row(
                            modifier = Modifier
                                .weight(1.5f)
                                .fillMaxSize()
                                .background(Color.LightGray)
                        ) {
                            Text(
                                text = "This is the correct answer",
                                modifier = Modifier
                                    .weight(1.5f)
                                    .padding(start = 5.dp, end = 5.dp)
                                    .fillMaxHeight()
                                    .wrapContentHeight(),
                                fontSize = 22.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Right
                            )
                            Switch(
                                checked = toggleState,
                                onCheckedChange = {
                                    toggleState = it
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .scale(1.25f)
                                    .padding(end = 10.dp),
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .weight(1f)
                ) {
                    Button(
                        onClick = { dialogOpen(false) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .shadow(5.dp, ButtonDefaults.shape),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        Text(text = "Cancel", fontSize = 21.sp)
                    }
                    Button(
                        onClick = {
                            dialogOpen(false)
                            if (inputText.isNotBlank()) {
                                answer(Answer(text = inputText, isCorrect = toggleState))
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .shadow(5.dp, ButtonDefaults.shape),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(text = "Save", fontSize = 21.sp)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddAnswerDialogPreview() {
    AddAnswerDialog(dialogIsOpen = true)
}