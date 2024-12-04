package com.cs31620.quizel.ui.components

import android.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InvalidInformationDialog(
    title: String,
    description: String,
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
){
    if (dialogIsOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen(false) },
            title = { Text(text = title) },
            text = { Text(text = description) },
            confirmButton = {
                TextButton(onClick = { dialogOpen(false) }) {
                    Text(text = "I understand")
                }
            }
        )
    }
}

@Preview
@Composable
fun InvalidInformationDialogPreview(){
    InvalidInformationDialog("Invalid Info", "This is an invalid info message", true)
}