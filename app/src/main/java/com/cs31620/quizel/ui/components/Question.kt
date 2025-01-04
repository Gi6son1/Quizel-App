package com.cs31620.quizel.ui.components

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cs31620.quizel.datasource.util.AnswerConverter

/**
 * Data class to hold the question objects in the quiz
 * It also belongs to the room database in questions table
 * @param id the id of the question, autogenerated
 * @param title the title of the question
 * @param description the description of the question
 * @param answers the list of answers for the question
 */
@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var description: String = "",

    @TypeConverters(AnswerConverter::class) //used when converting a list of answers into a string
    var answers: MutableList<Answer> = mutableListOf()
) : Parcelable {

    /**
     * These are required so that the answer can be passed as a mutable state object in the test questions screen
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeTypedList(answers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(
                parcel.readInt(),
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.createTypedArrayList(Answer.CREATOR)!!
            )
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}