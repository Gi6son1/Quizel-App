package com.cs31620.quizel.ui.components

import android.os.Parcel
import android.os.Parcelable

data class Answer(
    val text: String,
    var isCorrect: Boolean
) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeByte(if (isCorrect) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Answer> {
        override fun createFromParcel(parcel: Parcel): Answer {
            return Answer(
                parcel.readString()!!,
                parcel.readByte() != 0.toByte()
            )
        }

        override fun newArray(size: Int): Array<Answer?> {
            return arrayOfNulls(size)
        }
    }
}
