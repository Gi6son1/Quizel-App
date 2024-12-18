package com.cs31620.quizel.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cs31620.quizel.datasource.QuizelRepository
import com.cs31620.quizel.ui.components.Question
import kotlinx.coroutines.flow.flow

class QuestionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuizelRepository = QuizelRepository(application)
    var questionsList: LiveData<List<Question>> = liveData { emit(repository.getAllQuestions()) }
        private set

}