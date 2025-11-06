package com.example.mybudget

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_TRANSACTIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_AMOUNT REAL NOT NULL,
                $COLUMN_NOTE TEXT
            );
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        onCreate(db)
    }

    fun insertTransaction(amount: Double, note: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AMOUNT, amount)
            put(COLUMN_NOTE, note)
        }
        return db.insert(TABLE_TRANSACTIONS, null, values)
    }

    fun getAllTransactions(): List<TransactionData> {
        val list = mutableListOf<TransactionData>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_TRANSACTIONS,
            arrayOf(COLUMN_ID, COLUMN_AMOUNT, COLUMN_NOTE),
            null, null, null, null,
            "$COLUMN_ID DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val amount = it.getDouble(it.getColumnIndexOrThrow(COLUMN_AMOUNT))
                val note = it.getString(it.getColumnIndexOrThrow(COLUMN_NOTE)) ?: ""
                list.add(TransactionData(id, amount, note))
            }
        }
        return list
    }

    fun getTransactionById(id: Int): TransactionData? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_TRANSACTIONS,
            arrayOf(COLUMN_ID, COLUMN_AMOUNT, COLUMN_NOTE),
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        cursor.use {
            if (it.moveToFirst()) {
                val amount = it.getDouble(it.getColumnIndexOrThrow(COLUMN_AMOUNT))
                val note = it.getString(it.getColumnIndexOrThrow(COLUMN_NOTE)) ?: ""
                return TransactionData(id, amount, note)
            }
        }
        return null
    }

    fun updateTransaction(id: Int, amount: Double, note: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AMOUNT, amount)
            put(COLUMN_NOTE, note)
        }
        return db.update(
            TABLE_TRANSACTIONS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteTransaction(id: Int): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_TRANSACTIONS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    companion object {
        private const val DATABASE_NAME = "mybudget.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_TRANSACTIONS = "transactions"
        const val COLUMN_ID = "id"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_NOTE = "note"
    }
}
//MyDatabaseHelper je třída, která dědí z SQLiteOpenHelper a stará se o databázi. V onCreate vytvořím tabulku. Pak mám metody pro insert, select, update a delete – tedy celé CRUD nad tabulkou transactions.“