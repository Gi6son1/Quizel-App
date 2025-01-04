package com.cs31620.quizel.datasource

import android.app.Application
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.Score

/**
 * This class holds the repository for the database, and has all the methods used for accessing it
 * The suspend functions are suspend since they are not needed to be done immediately, and won't slow the program down
 * The non-suspend functions are needed immediately
 */
class QuizelRepository(application: Application) {
    private val questionDao = QuizelRoomDatabase.getDatabase(application)!!.questionDao() //the question database table object
    private val scoreDao = QuizelRoomDatabase.getDatabase(application)!!.scoreDao() //the score database table object

    suspend fun insertSingleQuestion(question: Question) { //used for a adding a new question to the database
        questionDao.insertSingleQuestion(question)
    }

    suspend fun insertMultipleQuestions(questions: List<Question>) { //used for adding multiple questions to the database, this was used for pre-populating the database in development
        questionDao.insertMultipleQuestions(questions)
    }

    fun getAllQuestions() = questionDao.getAllQuestions() //used to get all the questions from the database, it's not a suspend function because the data is needed immediately

    fun getQuestionById(questionId: Int?) = questionDao.getQuestionById(questionId) //get a single question from the database based on its ID

    suspend fun updateQuestion(question: Question) { //used for updating a question after it's been edited
        questionDao.updateQuestion(question)

    }

    suspend fun deleteQuestion(question: Question) { //used for deleting a question from the database
        questionDao.deleteQuestion(question)
    }

    suspend fun deleteQuestionsByIds(questionIds: List<Int>) { //used for deleting multiple questions from the database, for the select delete feature
        questionDao.deleteQuestionsByIds(questionIds)
    }

    fun getAllRecentScores() = scoreDao.getAllRecentScores() //retrieving the recent 8 scores from the database

    suspend fun updateScore(score: Score) { //updating a score in the db, in this case it's only used to update the name for the moment
        scoreDao.updateScore(score)
    }

    suspend fun insertScore(score: Score) { //adding a new score to the db
        scoreDao.insertScore(score)
    }

    fun getMostRecentUsername() = scoreDao.getMostRecentUsername() //getting the most recent score's username to save the new score with the same username

}