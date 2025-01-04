package com.cs31620.quizel.ui.components.customcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.graphics.Shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs31620.quizel.R

/**
 * Custom composable to hold simple buttons used in the quizel app
 * It takes onClick functionality, colour, and optional text, icons, and images, with default values if they don't get passed in
 *
 */
@Composable
fun QuizelSimpleButton(
    onClick: () -> Unit = {},
    text: Pair<String, Int>? = null, //a Pair of String and Int, which is the text and the font size
    icon: ImageVector? = null,
    image: Pair<Painter, Int>? = null, //a Pair of Painter and Int, which is the image and the padding to control size
    colour: Color,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    shape: Shape = ButtonDefaults.shape,
    contentPadding: PaddingValues = PaddingValues(0.dp), //sets padding to 0 meaning more text can fit into each button

){
    Button( //uses normal button composable with custom attributes
        onClick = onClick,
        modifier = modifier,
        elevation = ButtonDefaults.buttonElevation(10.dp), //sets button elevation so that it has a shadow and visible tap
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = colour),
        enabled = enabled,
        contentPadding = contentPadding
    ) {
        if (icon != null || image != null) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.button_icon),
                    modifier = Modifier
                        .size(25.dp)
                        .wrapContentSize()
                        .alpha(if (enabled) 1f else 0.5f), //if button is disabled, give it half transparency
                    tint = if (colour == MaterialTheme.colorScheme.surfaceDim || colour == MaterialTheme.colorScheme.surfaceBright) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surfaceBright
                )
            } else if (image != null) {
                Image(
                    painter = image.first,
                    contentDescription = stringResource(R.string.button_icon),
                    modifier = Modifier
                        .padding(image.second.dp)
                        .wrapContentSize()
                        .alpha(if (enabled) 1f else 0.5f)
                )
            }
            if (text != null) {
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
            if (text != null) {
                Text(
                    text = text.first, fontSize = text.second.sp, modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentSize()
                        .alpha(if (enabled) 1f else 0.5f),
                    color = if (colour == MaterialTheme.colorScheme.surfaceDim ||
                        colour == MaterialTheme.colorScheme.surfaceBright) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surfaceBright,
                    overflow = TextOverflow.Ellipsis, //prevents the text from overflowing over the button, turning it into a ... instead
                )
            }

    }
}
