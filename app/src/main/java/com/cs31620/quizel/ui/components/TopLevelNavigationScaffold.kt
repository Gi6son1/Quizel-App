package com.cs31620.quizel.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs31620.quizel.R
import com.cs31620.quizel.ui.navigation.Language

@Composable
fun TopLevelNavigationScaffold(
    navController: NavHostController,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .clip(RoundedCornerShape(15))
            )
            {
                MainPageNavigationBar(navController)
            }
        }
    )
    { innerPadding ->
        var expandedLanguageButton by remember { mutableStateOf(false) }

        TopLevelBackgroundScaffold(showTitle = !expandedLanguageButton) {
            pageContent(innerPadding)
        }
        LanguageButton(
            expanded = { expanded ->
                expandedLanguageButton = expanded
            }
        )
    }
}

@Preview
@Composable
private fun TopLevelNavigationScaffoldPreview() {
    TopLevelNavigationScaffold(navController = rememberNavController())
}

@Composable
fun LanguageButton(
    expanded: (Boolean) -> Unit
) {
    val context = LocalContext.current

    var isOpen by remember { mutableStateOf(false) }

    var languages = remember {
        mutableStateListOf<Language>(
            Language.English,
            Language.French,
            Language.Spanish
        )
    }
    var currentLanguage: Language by remember { mutableStateOf(languages.first()) }

    fun updateCurrentLanguage(language: Language) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.languageTag))
        currentLanguage = language
    }
    Log.d("QuestionBankScreen", stringResource(R.string.new_question))

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Image(
            painter = painterResource(id = currentLanguage.flagResource),
            contentDescription = currentLanguage.languageTag,
            modifier = Modifier
                .size(100.dp)
                .clickable {
                    isOpen = !isOpen
                    expanded(isOpen)
                }
        )

        AnimatedVisibility(visible = isOpen) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                languages.forEach { language ->
                    if (language != currentLanguage) {
                            Image(
                                painter = painterResource(id = language.flagResource),
                                contentDescription = language.languageTag,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable {
                                        isOpen = false
                                        expanded(false)
                                        updateCurrentLanguage(language)
                                        Log.d(
                                            "LanguageButton",
                                            "Language changed to ${language.languageTag}"
                                        )
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
