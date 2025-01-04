package com.cs31620.quizel.ui.components.customcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cs31620.quizel.ui.components.Score

/**
 * Custom composable to hold the recent scores display used in the quizel app
 * This takes a list of scores, and displays them into a user-readable column with dividers between each score
 * @param modifier the modifier to be applied to the column
 * @param scoresList the list of scores to be displayed
 */
@Composable
fun RecentScoresDisplay(
    modifier: Modifier = Modifier,
    scoresList: List<Score>
){
    if (scoresList.isEmpty()){ //if no scores in the db, display a message
        Text(text = "There are no recent scores to display", modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
            textAlign = TextAlign.Center)
    } else { //otherwise display the scores as a column
        Column(modifier = modifier
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState()))  //scrollable in case the the whole list doesn't fit on the screen
        { //only 8 scores max, so no need for a lazy column
            scoresList.forEachIndexed { index, score ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = score.username, modifier = Modifier.padding(horizontal = 5.dp))
                    Text(
                        text = "${score.score}/${score.numQuestions}",
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
                if (index != scoresList.lastIndex) { //only add a divider underneath if it's not the last score
                    HorizontalDivider(color =
                    if (scoresList[index + 1].numQuestions == score.numQuestions)
                        MaterialTheme.colorScheme.surfaceDim   //sets different colour divider to show divide between different quizzes
                    else
                        MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}