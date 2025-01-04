package com.cs31620.quizel.ui.components.parentscaffolds

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.cs31620.quizel.R

/**
 * Custom composable that holds the background for the app, it's applied to every screen
 * @param showTitle whether or not to show the title of the app
 * @param pageContent the content of the page, passed to the scaffold
 */
@Composable
fun TopLevelBackgroundScaffold(
    showTitle: Boolean = true,
    showBackground: Boolean = false,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    Scaffold(
        content = { innerPadding ->
            if (showBackground) AppBackground() //only show the background if it's requested
            if (showTitle) AppTitle(innerPadding) //only show the title if it's requested
            pageContent(innerPadding)
        },
        containerColor = Color.Transparent
    )
}

@Preview
@Composable
private fun TopLevelBackgroundScaffoldPreview() {
    TopLevelBackgroundScaffold()
}

/**
 * Background image for the app
 */
@Composable
private fun AppBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id =
            if(isSystemInDarkTheme())  //changes depending on dark or light mode
                R.drawable.app_background_dark
            else
                R.drawable.app_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
        )
    }
}

/**
 * Title for the app
 */
@Composable
private fun AppTitle(innerPadding: PaddingValues){
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