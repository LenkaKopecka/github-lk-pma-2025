package com.example.myappbudget.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val name: String // Název je zároveň unikátní klíč (nemůžou být dvě Jídla)
)