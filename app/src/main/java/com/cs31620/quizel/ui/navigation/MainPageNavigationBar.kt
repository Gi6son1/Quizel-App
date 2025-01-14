package com.cs31620.quizel.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.outlined.BorderColor
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.R
import com.cs31620.quizel.ui.theme.QuizelTheme

/**
 * Navigation bar for the main page
 * @param navController the navigation controller
 */
@Composable
fun MainPageNavigationBar(
    navController: NavController,
) {
    val icons = mapOf( //the icons used in the navigation bar
        Screen.QuestionBank to IconGroup(
            filledIcon = Icons.AutoMirrored.Filled.FormatListBulleted,
            outlinedIcon = Icons.AutoMirrored.Outlined.FormatListBulleted,
            label = stringResource(R.string.nav_bar_question_bank_label)
        ),
        Screen.TakeQuiz to IconGroup(
            filledIcon = Icons.Filled.BorderColor,
            outlinedIcon = Icons.Outlined.BorderColor,
            label = stringResource(R.string.nav_bar_take_quiz_label)
        )
    )

    NavigationBar{ //the navigation bar itself
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navigation_screens.forEach { screen ->
            val isSelected = currentDestination?.route == screen.route
            val labelText = icons[screen]!!.label

            NavigationBarItem( //adds each icon to the navigation bar
                icon = {
                    Icon(
                        imageVector = (
                                if (isSelected)
                                    icons[screen]!!.filledIcon
                                else
                                    icons[screen]!!.outlinedIcon
                                ),
                        contentDescription = labelText
                    )
                },
                label = { Text(labelText) },
                selected = isSelected,
                onClick = { //when tapped, takes to screen and also shows the icon as "enabled"
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainPageNavigationBarPreview() {
    QuizelTheme {
        MainPageNavigationBar(navController = rememberNavController())
    }

}