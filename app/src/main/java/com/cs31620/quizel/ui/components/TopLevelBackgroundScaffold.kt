package com.cs31620.quizel.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs31620.quizel.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopLevelBackgroundScaffold(
    pageContent : @Composable (innerPadding : PaddingValues) -> Unit = {}
) {
    Scaffold(
        content = { innerPadding ->
            AppBackground(innerPadding)
            pageContent(innerPadding) }
    )
}

@Preview
@Composable
fun TopLevelBackgroundScaffoldPreview() {
    TopLevelBackgroundScaffold()
}

@Composable
fun AppBackground(innerPadding: PaddingValues){
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.app_background),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(innerPadding),
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.titleLarge,
        fontSize = 100.sp,
        )
}