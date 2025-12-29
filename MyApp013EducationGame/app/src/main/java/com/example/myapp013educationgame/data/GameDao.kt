package com.example.myapp013educationgame.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GameDao {

    // --- UŽIVATEL ---
    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getUser(): User?

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    // --- OTÁZKY ---
    @Insert
    suspend fun insertQuestions(questions: List<Question>)

    @Query("SELECT * FROM question_table ORDER BY RANDOM() LIMIT 5")
    suspend fun getRandomQuestions(): List<Question>

    // --- VÝSLEDKY ---
    @Insert
    suspend fun insertResult(result: GameResult)

    @Query("SELECT * FROM result_table ORDER BY timestamp DESC")
    suspend fun getAllResults(): List<GameResult>
}