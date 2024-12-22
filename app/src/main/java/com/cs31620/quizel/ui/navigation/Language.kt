package com.cs31620.quizel.ui.navigation

import com.cs31620.quizel.R

sealed class Language(
    val languageTag: String,
    val flagResource: Int
) {
    data object English : Language("en", R.drawable.british_flag_icon)
    data object French : Language("fr", R.drawable.french_flag_icon)
    data object Spanish : Language("es", R.drawable.spanish_flag_icon)
}


val languages = listOf(
    Language.English,
    Language.French,
    Language.Spanish
)