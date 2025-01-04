package com.cs31620.quizel.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cs31620.quizel.datasource.QuizelRepository
import com.cs31620.quizel.ui.components.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The viewmodel class used by the classes that need to access the questions table in the database
 * It uses the SQL methods in the repository class to get the data, and update where needed
 */
class QuestionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuizelRepository = QuizelRepository(application)

    var questionsList: LiveData<List<Question>> = repository.getAllQuestions()
        private set

    fun deleteSelectedQuestion(question: Question?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (question != null) { //checks if null first to avoid crashing
                repository.deleteQuestion(question)
            }
        }
    }

    fun addNewQuestion(question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSingleQuestion(question)
        }
    }

    fun getQuestionById(questionId: Int?): LiveData<Question?> {
        return repository.getQuestionById(questionId)
    }

    fun updateSelectedQuestion(question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateQuestion(question)
        }
    }

    fun deleteQuestionsById(questionIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteQuestionsByIds(questionIds)
        }
    }
}