package com.cs31620.quizel.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cs31620.quizel.datasource.util.AnswerConverter
import com.cs31620.quizel.model.QuestionDao
import com.cs31620.quizel.ui.components.Answer
import com.cs31620.quizel.ui.components.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Question::class], version = 1)
@TypeConverters(AnswerConverter::class)
abstract class QuizelRoomDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {
        private var instance: QuizelRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): QuizelRoomDatabase? {
            if (instance == null) {
                instance =
                    Room.databaseBuilder<QuizelRoomDatabase>(
                        context.applicationContext,
                        QuizelRoomDatabase::class.java,
                        "quizel_database"
                    )
                        .allowMainThreadQueries()
                        .addCallback(roomDatabaseCallback(context))
                        //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()
            }
            return instance
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    coroutineScope.launch {
                        populateDatabase(context, getDatabase(context)!!)
                    }
                }
            }
        }

        private suspend fun populateDatabase(context: Context, instance: QuizelRoomDatabase) {
            val question1 = Question(
                title = "What is the capital of France?",
                description = "Select the correct capital of France.",
                answers = mutableListOf(
                    Answer("Paris", true),
                    Answer("London", false),
                    Answer("Berlin", false),
                    Answer("Madrid", false)
                )
            )
            val question2 = Question(
                title = "Which planet is known as the Red Planet?",
                description = "Select the correct planet.",
                answers = mutableListOf(
                    Answer("Mars", true),
                    Answer("Venus", false),
                )
            )
            val question3 = Question(
                description = "What is the largest mammal in the world?",
                answers = mutableListOf(
                    Answer("Elephant", false),
                    Answer("Blue Whale", true),
                    Answer("Giraffe", false),
                    Answer("Hippopotamus", false)
                )
            )

            val questionList = mutableListOf(
                question1,
                question2,
                question3
            )

            val dao = instance.questionDao()
            dao.insertMultipleQuestions(questionList)
        }
    }
}