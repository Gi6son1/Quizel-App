package com.cs31620.quizel.datasource.util

import androidx.room.TypeConverter
import com.cs31620.quizel.ui.components.Answer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * This is used to convert a mutableList of answer objects into JSON string and back again when required using the GSON library
 * This is so that they can be stored in the Room database
 */
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