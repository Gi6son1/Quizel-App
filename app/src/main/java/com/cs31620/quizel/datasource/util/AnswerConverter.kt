package com.cs31620.quizel.datasource.util

import androidx.room.TypeConverter
import com.cs31620.quizel.ui.components.Answer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AnswerConverter {
    @TypeConverter
    @JvmStatic
    fun fromAnswerList(answers: MutableList<Answer>?): String? {
        return Gson().toJson(answers)
    }

    @TypeConverter
    @JvmStatic
    fun toAnswerList(answerString: String?): MutableList<Answer>? {
        val listType = object : TypeToken<MutableList<Answer>>() {}.type
        return Gson().fromJson(answerString, listType)
    }
}