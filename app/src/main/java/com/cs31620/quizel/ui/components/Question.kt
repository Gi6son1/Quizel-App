package com.cs31620.quizel.ui.components

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {

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