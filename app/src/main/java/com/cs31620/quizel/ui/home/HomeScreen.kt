package com.cs31620.quizel.ui.home

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cs31620.quizel.ui.components.TopLevelScaffold
import com.cs31620.quizel.ui.theme.QuizelTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(modifier: Modifier){
    TopLevelScaffold()
}

@Preview
@Composable
fun HomeScreenPreview(){
    QuizelTheme { HomeScreen(modifier = Modifier) }
}