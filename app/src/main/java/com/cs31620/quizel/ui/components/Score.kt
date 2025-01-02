package com.cs31620.quizel.ui.components

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var username: String = "User",
    var score: Int = 0,
    var numQuestions: Int = 0,
)