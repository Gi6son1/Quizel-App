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

@Composable
fun QuizelSimpleButton(
    onClick: () -> Unit = {},
    text: Pair<String, Int>? = null,
    icon: ImageVector? = null,
    image: Pair<Painter, Int>? = null,
    colour: Color,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    shape: Shape = ButtonDefaults.shape,
    contentPadding: PaddingValues = PaddingValues(0.dp),

){
    Button(
        onClick = onClick,
        modifier = modifier,
        elevation = ButtonDefaults.buttonElevation(10.dp),
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
                        .alpha(if (enabled) 1f else 0.5f),
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
                    overflow = TextOverflow.Ellipsis,
                )
            }

    }
}
