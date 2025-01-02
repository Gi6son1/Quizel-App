package com.cs31620.quizel.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cs31620.quizel.ui.components.Question
import com.cs31620.quizel.ui.components.Score

@Dao
interface ScoreDao {

        @Insert
        suspend fun insertScore(score: Score)

        @Update(onConflict = OnConflictStrategy.REPLACE)
        suspend fun updateScore(score: Score)

        @Query("SELECT * FROM scores ORDER BY id DESC LIMIT 7")
        fun getAllRecentScores(): LiveData<List<Score>>

        @Query("SELECT username FROM scores ORDER BY id DESC LIMIT 1")
        fun getMostRecentUsername(): LiveData<String>
}