package com.cs31620.quizel.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.cs31620.quizel.datasource.QuizelRepository
import com.cs31620.quizel.ui.components.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class QuestionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuizelRepository = QuizelRepository(application)
    var questionsList: LiveData<List<Question>> = liveData { emit(repository.getAllQuestions()) }
        private set

    fun deleteSelectedQuestion(question: Question?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (question != null) {
                Log.d("QuestionsViewModel", "Deleting question: ${question.title}")
                repository.deleteQuestion(question)
            }
            questionsList = liveData { emit(repository.getAllQuestions()) }
        }
    }

}