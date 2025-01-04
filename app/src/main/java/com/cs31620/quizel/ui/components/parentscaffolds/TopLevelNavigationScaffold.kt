package com.cs31620.quizel.ui.components.parentscaffolds

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.ui.navigation.MainPageNavigationBar

/**
 * Custom composable scaffold that hold the navigation bar for the app
 * @param navController the navigation controller for the app
 * @param pageContent the content of the page, passed to the scaffold
 */
@Composable
fun TopLevelNavigationScaffold(
    navController: NavHostController,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .clip(RoundedCornerShape(15)) //sets custom shape for nav bar, making it rounded and therefore cooler
            )
            {
                MainPageNavigationBar(navController)
            }
        },
        content = { innerPadding ->
            TopLevelBackgroundScaffold {
                pageContent(innerPadding)
            }
        }
    )
}

@Preview
@Composable
private fun TopLevelNavigationScaffoldPreview() {
    TopLevelNavigationScaffold(navController = rememberNavController())
}
