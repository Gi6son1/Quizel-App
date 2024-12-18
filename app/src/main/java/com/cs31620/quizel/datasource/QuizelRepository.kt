package com.cs31620.quizel.datasource

import android.app.Application
import com.cs31620.quizel.ui.components.Question

class QuizelRepository(application: Application) {
    private val questionDao = QuizelRoomDatabase.getDatabase(application)!!.questionDao()

    suspend fun insert(question: Question) {
        questionDao.insertSingleQuestion(question)
    }

    suspend fun insertMultipleQuestions(questions: List<Question>) {
        questionDao.insertMultipleQuestions(questions)
    }

    fun getAllQuestions() = questionDao.getAllQuestions()

    fun getQuestionById(questionId: Int?) = questionDao.getQuestionById(questionId)

    suspend fun updateQuestion(question: Question) {
        questionDao.updateQuestion(question)

    }

    suspend fun deleteQuestion(question: Question) {
        questionDao.deleteQuestion(question)
    }

    suspend fun deleteQuestionsByIds(questionIds: List<Int>) {
        questionDao.deleteQuestionsByIds(questionIds)
    }
}