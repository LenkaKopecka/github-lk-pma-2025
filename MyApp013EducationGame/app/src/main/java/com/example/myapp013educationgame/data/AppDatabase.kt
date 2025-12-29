package com.example.myapp013educationgame.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Question::class, GameResult::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "game_database"
                )
                    .addCallback(DatabaseCallback()) // Přidání callbacku pro naplnění dat
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Třída pro předvyplnění databáze otázkami při prvním vytvoření
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.gameDao())
                    }
                }
            }

            suspend fun populateDatabase(dao: GameDao) {
                // Ukázková data
                val questions = listOf(
                    Question(text = "Kolik bitů má jeden Byte?", answerA = "4", answerB = "8", answerC = "16", answerD = "32", correctIndex = 1),
                    Question(text = "Hlavní město Francie?", answerA = "Londýn", answerB = "Berlín", answerC = "Paříž", answerD = "Madrid", correctIndex = 2),
                    Question(text = "Co znamená zkratka CPU?", answerA = "Central Processing Unit", answerB = "Computer Personal Unit", answerC = "Central Power Unit", answerD = "Control Panel Utility", correctIndex = 0),
                    Question(text = "Jak se řekne anglicky 'Jablko'?", answerA = "Banana", answerB = "Pear", answerC = "Apple", answerD = "Orange", correctIndex = 2),
                    Question(text = "Kolik je 5 * 5?", answerA = "20", answerB = "25", answerC = "30", answerD = "10", correctIndex = 1),
                    Question(text = "Kdo napsal R.U.R.?", answerA = "Karel Čapek", answerB = "Božena Němcová", answerC = "Franz Kafka", answerD = "Jaroslav Hašek", correctIndex = 0)
                )
                dao.insertQuestions(questions)
            }
        }
    }
}