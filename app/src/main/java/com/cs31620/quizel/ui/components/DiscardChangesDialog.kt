package com.cs31620.quizel.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun DiscardChangesDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    closeQuestionDialog: (Boolean) -> Unit = {},
) {
    if (dialogIsOpen) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {


            ConstraintLayout(modifier = Modifier.border(3.dp, Color.Black)) {
                val (message, cancel, discard) = createRefs()
                Card(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .constrainAs(message) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(cancel.top)
                            bottom.linkTo(discard.top)
                        }
                ) {
                    Text(text = "Are you sure you want to discard any changes?")
                }
                Button(onClick = { dialogOpen(false) },
                    modifier = Modifier.constrainAs(cancel) {
                        top.linkTo(message.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(discard.start)
                    }) {
                    Text(text = "Cancel")
                }
                Button(onClick = {
                    dialogOpen(false)
                    closeQuestionDialog(true)
                },
                    modifier = Modifier.constrainAs(discard) {
                        top.linkTo(message.bottom)
                        start.linkTo(cancel.end)
                        end.linkTo(parent.end)
                    }) {
                    Text(text = "Discard")
                }
            }


        }
    }
}
