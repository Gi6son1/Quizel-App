package com.cs31620.quizel.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cs31620.quizel.ui.components.Question

@Dao
interface QuestionDao {
    @Insert
    suspend fun insertSingleQuestion(question: Question)

    @Insert
    suspend fun insertMultipleQuestions(questions: List<Question>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("DELETE FROM questions WHERE id IN (:questionIds)")
    suspend fun deleteQuestionsByIds(questionIds: List<Int>)

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<Question>
}