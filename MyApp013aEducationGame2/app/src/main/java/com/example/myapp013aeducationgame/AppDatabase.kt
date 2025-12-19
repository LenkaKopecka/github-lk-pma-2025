package com.example.myapp013aeducationgame

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Player::class, Question::class, GameResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun questionDao(): QuestionDao
    abstract fun gameResultDao(): GameResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "education_game_database"
                )
                .addCallback(AppDatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateDatabase(it.questionDao())
                }
            }
        }

        suspend fun prepopulateDatabase(questionDao: QuestionDao) {
            val questions = listOf(
                Question(questionText = "What is the capital of France?", correctAnswer = "Paris", wrongAnswer1 = "London", wrongAnswer2 = "Berlin"),
                Question(questionText = "What is 2 + 2?", correctAnswer = "4", wrongAnswer1 = "3", wrongAnswer2 = "5"),
                Question(questionText = "What is the color of the sky?", correctAnswer = "Blue", wrongAnswer1 = "Green", wrongAnswer2 = "Red"),
                Question(questionText = "What is the highest mountain in the world?", correctAnswer = "Mount Everest", wrongAnswer1 = "K2", wrongAnswer2 = "Kangchenjunga"),
                Question(questionText = "Who wrote 'Hamlet'?", correctAnswer = "William Shakespeare", wrongAnswer1 = "Charles Dickens", wrongAnswer2 = "Leo Tolstoy")
            )
            questionDao.insertAll(questions)
        }
    }
}
