package com.cs31620.quizel.model

import android.app.Application
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cs31620.quizel.datasource.QuizelRepository
import com.cs31620.quizel.ui.components.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuizelRepository = QuizelRepository(application)

    var questionsList: LiveData<List<Question>> = repository.getAllQuestions()
        private set

    fun deleteSelectedQuestion(question: Question?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (question != null) {
                Log.d("QuestionsViewModel", "Deleting question: ${question.description}")
                repository.deleteQuestion(question)
            }
        }
    }

    fun addNewQuestion(question: Question?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (question != null) {
                repository.insert(question)
            }
        }
    }

    fun getQuestionById(questionId: Int?): LiveData<Question?>{
        return repository.getQuestionById(questionId)
    }

    fun updateSelectedQuestion(question: Question?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (question != null) {
                repository.updateQuestion(question)
            }
        }
    }

    fun deleteQuestionsById(questionIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteQuestionsByIds(questionIds)
        }
    }
}