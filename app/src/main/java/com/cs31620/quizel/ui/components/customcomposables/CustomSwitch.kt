package com.cs31620.quizel.ui.components.customcomposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cs31620.quizel.R

/**
 * Custom composable to hold the switch objects used in the quizel app
 */
@Composable
fun QuizelSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        thumbContent = { //custom switch design -> Tick if on, cross if off
            if (checked) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.toggle_on),
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.toggle_off),
                    tint = MaterialTheme.colorScheme.surfaceBright
                )
            }
        }
    )
}