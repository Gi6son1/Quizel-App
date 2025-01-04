package com.cs31620.quizel.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cs31620.quizel.datasource.QuizelRepository
import com.cs31620.quizel.ui.components.Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The viewmodel class used by the classes that need to access the scores table in the database
 */
class ScoresViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuizelRepository = QuizelRepository(application)

    var scoresList: LiveData<List<Score>> = repository.getAllRecentScores()
        private set

    var mostRecentUsername: LiveData<String> = repository.getMostRecentUsername()

    fun addNewScore(score: Score) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertScore(score)
        }
    }

    fun updateScoreWithUsername(score: Score?, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (score != null) { //checks if null first (in case the database hasn't added the score in time when this method is called)
                score.username = username
                repository.updateScore(score)
            }
        }
    }
}