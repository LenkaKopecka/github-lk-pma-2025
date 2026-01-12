package com.example.myappbudget.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity

    // --- ZMĚNA: Filtrování podle data (od - do) ---

    // 1. Všechny transakce v daném rozmezí
    @Query("SELECT * FROM transactions WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getTransactionsByDate(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>

    // 2. Příjmy v daném rozmezí
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'INCOME' AND date >= :startDate AND date <= :endDate")
    fun getIncomeByDate(startDate: Long, endDate: Long): Flow<Double?>

    // 3. Výdaje v daném rozmezí
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'EXPENSE' AND date >= :startDate AND date <= :endDate")
    fun getExpenseByDate(startDate: Long, endDate: Long): Flow<Double?>

    // 4. Získání všech kategorií (abecedně seřazené)
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    // 5. Vložení nové kategorie (OnConflictStrategy.IGNORE znamená: pokud už tam je, nic nedělej)
    @Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity)

    // 6. Smazání kategorie podle názvu
    @Query("DELETE FROM categories WHERE name = :categoryName")
    suspend fun deleteCategoryByName(categoryName: String)

}