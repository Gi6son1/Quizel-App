package com.cs31620.quizel.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs31620.quizel.R

@Composable
fun MainPageTopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text(
            stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayLarge,
            fontSize = 100.sp)},
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier.height(110.dp)
    )
}

@Preview
@Composable
fun MainPageTopAppBarPreview() {
    MainPageTopAppBar()

}