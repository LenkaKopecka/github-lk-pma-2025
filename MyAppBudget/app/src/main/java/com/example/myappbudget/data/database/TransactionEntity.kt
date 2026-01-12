package com.example.myappbudget.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,         // Např. "Nákup Albert"
    val amount: Double,        // Např. 1200.50
    val type: String,          // "INCOME" nebo "EXPENSE"
    val category: String,      // Např. "Jídlo"
    val date: Long             // Timestamp
)