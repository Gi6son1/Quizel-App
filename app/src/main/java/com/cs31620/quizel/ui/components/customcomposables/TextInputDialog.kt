package com.cs31620.quizel.ui.components.customcomposables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cs31620.quizel.R
import com.cs31620.quizel.ui.components.Answer

@Composable
fun TextInputDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    response: (Any) -> Unit = {},
    isAnswer: Boolean = false,
    placeholder: String = ""
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
                    .height(230.dp),
            ) {
                Card(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .shadow(5.dp, MaterialTheme.shapes.medium),
                    ) {
                    Column {
                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(63.dp)
                                .wrapContentHeight(align = Alignment.Top),
                            textStyle = TextStyle.Default.copy(fontSize = 27.sp),
                            placeholder = { Text(text = placeholder, fontSize = 27.sp) },
                            singleLine = true,
                        )
                        if (isAnswer)
                        {
                            Row(
                                modifier = Modifier
                                    .height(80.dp)
                                    .fillMaxSize()
                                    .background(Color.LightGray)
                            ) {
                                Text(
                                    text = stringResource(R.string.this_is_the_correct_answer),
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .padding(start = 5.dp, end = 5.dp)
                                        .fillMaxHeight()
                                        .wrapContentHeight(),
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Right
                                )
                                QuizelSwitch(
                                    checked = toggleState,
                                    onCheckedChange = {
                                        toggleState = it
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxSize()
                                        .scale(1.25f)
                                        .padding(end = 10.dp)
                                )
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .height(65.dp)
                ) {
                    QuizelSimpleButton(
                        onClick = { dialogOpen(false) },
                        text = Pair(stringResource(R.string.cancel), 20),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        colour = MaterialTheme.colorScheme.tertiary
                    )
                    QuizelSimpleButton(
                        onClick = {
                            dialogOpen(false)
                            if (inputText.isNotBlank()) {
                                response(
                                    if (isAnswer)
                                        Answer(text = inputText, isCorrect = toggleState)
                                    else
                                        inputText
                                )
                            }
                        },
                        text = Pair(stringResource(R.string.save), 20),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        colour = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddAnswerDialogPreview() {
   TextInputDialog(dialogIsOpen = true)
}