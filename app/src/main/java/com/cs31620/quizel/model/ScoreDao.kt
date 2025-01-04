package com.cs31620.quizel.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cs31620.quizel.ui.components.Score

/**
 * Data access object for the scores table in the database
 * Holds the SQL queries behind the methods
 */
@Dao
interface ScoreDao {

        @Insert
        suspend fun insertScore(score: Score)

        @Update(onConflict = OnConflictStrategy.REPLACE)
        suspend fun updateScore(score: Score)

        @Query("SELECT * FROM scores ORDER BY id DESC LIMIT 8")
        fun getAllRecentScores(): LiveData<List<Score>>

        @Query("SELECT username FROM scores ORDER BY id DESC LIMIT 1")
        fun getMostRecentUsername(): LiveData<String>
}