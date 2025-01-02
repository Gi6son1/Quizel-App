package com.cs31620.quizel.model

import android.app.Application
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cs31620.quizel.datasource.QuizelRepository
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScoresViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuizelRepository = QuizelRepository(application)

    var scoresList: LiveData<List<Score>> = repository.getAllRecentScores()
        private set

    var mostRecentUsername: LiveData<String> = repository.getMostRecentUsername()

    fun addNewScore(score: Score?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (score != null) {
                repository.insertScore(score)
            }
        }
    }

    fun updateScoreWithUsername(score: Score?, username: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (score != null) {
                score.username = username ?: "User"
                repository.updateScore(score)
            }
        }
    }
}