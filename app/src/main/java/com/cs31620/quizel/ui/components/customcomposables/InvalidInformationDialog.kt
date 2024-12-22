package com.cs31620.quizel.ui.components.customcomposables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            title = { Text(text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp
            ) },
            text = { Text(text = description) },
            confirmButton = {
                TextButton(onClick = { dialogOpen(false) }) {
                    Text(text = "I understand", fontWeight = FontWeight.Bold)
                }
            },
            modifier = Modifier.shadow(5.dp, MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Preview
@Composable
private fun InvalidInformationDialogPreview(){
    InvalidInformationDialog("Invalid Info", "This is an invalid info message", true)
}