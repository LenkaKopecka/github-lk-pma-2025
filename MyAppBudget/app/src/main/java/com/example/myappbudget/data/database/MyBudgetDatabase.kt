package com.example.myappbudget.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TransactionEntity::class, CategoryEntity::class], version = 2, exportSchema = false)
abstract class MyBudgetDatabase : RoomDatabase() {

    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: MyBudgetDatabase? = null

        fun getDatabase(context: Context): MyBudgetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyBudgetDatabase::class.java,
                    "budget_database"
                )
                    .fallbackToDestructiveMigration() // Toto dovolí smazat stará data při změně verze
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}