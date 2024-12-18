package com.cs31620.quizel.ui.components

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cs31620.quizel.datasource.util.AnswerConverter

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var description: String = "",

    @TypeConverters(AnswerConverter::class)
    var answers: MutableList<Answer> = mutableListOf()
)