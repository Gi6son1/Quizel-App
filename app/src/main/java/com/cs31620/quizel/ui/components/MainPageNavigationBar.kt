package com.cs31620.quizel.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.outlined.BorderColor
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.R
import com.cs31620.quizel.ui.navigation.Screen
import com.cs31620.quizel.ui.navigation.screens
import com.cs31620.quizel.ui.theme.QuizelTheme

@Composable
fun MainPageNavigationBar(
    navController: NavController,
) {
    val icons = mapOf(
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

    NavigationBar{
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        screens.forEach { screen ->
            val isSelected = currentDestination?.route == screen.route
            val labelText = icons[screen]!!.label

            NavigationBarItem(
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
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPageNavigationBarPreview() {
    QuizelTheme {
        MainPageNavigationBar(navController = rememberNavController())
    }

}