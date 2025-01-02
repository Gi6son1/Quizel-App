package com.cs31620.quizel.ui.components.customcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cs31620.quizel.ui.components.Score

@Composable
fun RecentScoresDisplay(modifier: Modifier = Modifier, scoresList: List<Score>){
    if (scoresList.isEmpty()){
        Text(text = "There are no recent scores to display", modifier = Modifier.padding(horizontal = 10.dp),
            textAlign = TextAlign.Center)
    } else {
        Column(modifier = modifier.padding(horizontal = 10.dp)) {
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
                if (index != scoresList.lastIndex) {
                    HorizontalDivider(color = if (scoresList[index + 1].numQuestions == score.numQuestions) Color.LightGray else Color.Black)
                }
            }
        }
    }
}