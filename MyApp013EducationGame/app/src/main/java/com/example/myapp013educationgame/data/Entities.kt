package com.example.myapp013educationgame.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// 1. Tabulka pro uživatele (Identita)
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val totalXp: Int = 0
)

// 2. Tabulka pro otázky (Obsah hry)
@Entity(tableName = "question_table")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val answerA: String,
    val answerB: String,
    val answerC: String,
    val answerD: String,
    val correctIndex: Int // 0=A, 1=B, 2=C, 3=D
)

// 3. Tabulka pro výsledky (Historie)
@Entity(tableName = "result_table")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
)