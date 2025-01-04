package com.cs31620.quizel.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Class to hold the icons used in the navigation bar
 * @param filledIcon the filled icon
 * @param outlinedIcon the outlined icon
 * @param label the label for the icon
 */
data class IconGroup(
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val label: String
)