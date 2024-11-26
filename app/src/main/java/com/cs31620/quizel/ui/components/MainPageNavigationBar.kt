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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs31620.quizel.R
import com.cs31620.quizel.ui.theme.QuizelTheme

@Composable
fun MainPageNavigationBar() {
    val icons = listOf(
        IconGroup(
            filledIcon = Icons.AutoMirrored.Filled.FormatListBulleted,
            outlinedIcon = Icons.AutoMirrored.Outlined.FormatListBulleted,
            label = stringResource(R.string.nav_bar_question_bank_label)
        ),
        IconGroup(
            filledIcon = Icons.Filled.BorderColor,
            outlinedIcon = Icons.Outlined.BorderColor,
            label = stringResource(R.string.nav_bar_take_quiz_label)
        )
    )

    NavigationBar(
        modifier =
        Modifier.border(
            border = BorderStroke(2.dp, Color.Black),
            shape = RoundedCornerShape(15)
        )
    ) {
        icons.forEach { iconGroup ->
            val isSelected = false
            val labelText = iconGroup.label
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = (
                                if (isSelected)
                                    iconGroup.filledIcon
                                else
                                    iconGroup.outlinedIcon
                                ),
                        contentDescription = labelText
                    )
                },
                label = { Text(labelText) },
                selected = isSelected,
                onClick = { /*TODO*/ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPageNavigationBarPreview() {
    QuizelTheme {
        MainPageNavigationBar()
    }

}