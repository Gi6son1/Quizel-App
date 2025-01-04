package com.cs31620.quizel.ui.components.customcomposables

import android.view.Window
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.cs31620.quizel.R

/**
 * Custom composable to hold action check dialogs i.e. "Are you sure you want to do this?"
 * It takes a check message as an input and a main action button to pass into the dialog
 * @param dialogIsOpen whether or not the dialog is open
 * @param dialogOpen method used for closing the dialog
 * @param actionDialogMessage the message to be displayed in the dialog
 * @param mainActionButton the main action button to be displayed in the dialog
 * @param performMainAction method used for saying if the main action should be performed
 */
@Composable
fun ActionCheckDialog(
    dialogIsOpen: Boolean, //checks if the dialog should be open
    dialogOpen: (Boolean) -> Unit = {}, //method used for closing the dialog
    actionDialogMessage: String,
    mainActionButton: @Composable (onClick: () -> Unit, modifier: Modifier) -> Unit, //takes a clickable composable with a modifier
    performMainAction: (Boolean) -> Unit = {false}, //sets to false by default
) {
    if (dialogIsOpen) {
        Dialog(
            onDismissRequest = {}, //sets it so that user cannot dismiss dialog by tapping outside
            properties = DialogProperties(usePlatformDefaultWidth = false) //allows a nonstandard width for the dialog
        ) {
            BackHandler {
                dialogOpen(false) //closes dialog when back button is pressed
            }

            val dialogWindow = getDialogWindow() //used for custom screen dimming

            SideEffect {
                dialogWindow.let { window -> //dims background
                    window?.setDimAmount(0.8f)
                }
            }

            Column( //column to hold the dialog
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .height(200.dp)
            ) {
                Card( //card to hold the message
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .weight(1.1f)
                ) {
                    Text(text = actionDialogMessage,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
                    )
                }
                //row to hold the buttons
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .weight(1f)
                ) {
                    QuizelSimpleButton(
                        onClick = { dialogOpen(false) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colour = MaterialTheme.colorScheme.tertiary,
                        text = Pair(stringResource(R.string.cancel), 20)
                    )

                    mainActionButton( //sets onclick details for button
                        {
                            dialogOpen(false)
                            performMainAction(true)
                        },
                        Modifier //sets modifier details for button
                            .weight(1f)
                            .fillMaxHeight()
                            .shadow(5.dp, ButtonDefaults.shape)
                    )
                }
            }
        }
    }
}

//Method that gets the dialog window
@Composable
fun getDialogWindow(): Window? = (LocalView.current.parent as? DialogWindowProvider)?.window