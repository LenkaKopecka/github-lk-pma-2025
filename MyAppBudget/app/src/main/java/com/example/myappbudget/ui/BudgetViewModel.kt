package com.example.myappbudget.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.myappbudget.data.UserPreferencesRepository
import com.example.myappbudget.data.database.CategoryEntity // <-- Důležitý import pro nové kategorie
import com.example.myappbudget.data.database.MyBudgetDatabase
import com.example.myappbudget.data.database.TransactionEntity
import kotlinx.coroutines.launch
import java.util.Calendar

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val database = MyBudgetDatabase.getDatabase(application)
    private val dao = database.budgetDao()
    private val prefs = UserPreferencesRepository(application)

    // --- 1. NASTAVENÍ A KATEGORIE ---

    // Limit a Měna z nastavení
    val monthlyLimit = prefs.monthlyLimitFlow.asLiveData()
    val currency = prefs.currencyFlow.asLiveData()

    // NOVINKA: Seznam kategorií pro naše rozbalovací menu (načítá se z DB)
    val categories = dao.getAllCategories().asLiveData()

    // --- 2. LOGIKA KALENDÁŘE ---

    // Držíme si aktuální měsíc
    private val _currentMonth = MutableLiveData<Calendar>()

    // Text pro hlavičku (např. "Leden 2026")
    val currentMonthText: LiveData<String> = _currentMonth.switchMap { calendar ->
        val monthName = getMonthName(calendar.get(Calendar.MONTH))
        val year = calendar.get(Calendar.YEAR)
        MutableLiveData("$monthName $year")
    }

    init {
        // Nastavení aktuálního měsíce při startu
        val today = Calendar.getInstance()
        today.set(Calendar.DAY_OF_MONTH, 1)
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        _currentMonth.value = today

        // Vložení základních kategorií (pokud je databáze prázdná)
        viewModelScope.launch {
            val defaultCategories = listOf("Jídlo", "Bydlení", "Doprava", "Zábava", "Nákupy", "Výplata", "Ostatní")
            for (cat in defaultCategories) {
                // insertCategory má nastaveno IGNORE, takže pokud už tam jsou, nic se nestane
                dao.insertCategory(CategoryEntity(cat))
            }
        }
    }

    // Posun měsíce (+1 nebo -1)
    fun changeMonth(amount: Int) {
        val current = _currentMonth.value ?: Calendar.getInstance()
        current.add(Calendar.MONTH, amount)
        _currentMonth.value = current
    }

    // --- 3. AUTOMATICKÉ NAČÍTÁNÍ DAT (podle měsíce) ---

    val allTransactions: LiveData<List<TransactionEntity>> = _currentMonth.switchMap { cal ->
        val (start, end) = getMonthRange(cal)
        dao.getTransactionsByDate(start, end).asLiveData()
    }

    val totalIncome: LiveData<Double?> = _currentMonth.switchMap { cal ->
        val (start, end) = getMonthRange(cal)
        dao.getIncomeByDate(start, end).asLiveData()
    }

    val totalExpense: LiveData<Double?> = _currentMonth.switchMap { cal ->
        val (start, end) = getMonthRange(cal)
        dao.getExpenseByDate(start, end).asLiveData()
    }

    // Pomocná funkce pro výpočet začátku a konce měsíce
    private fun getMonthRange(calendar: Calendar): Pair<Long, Long> {
        val start = calendar.timeInMillis
        val endCal = calendar.clone() as Calendar
        endCal.add(Calendar.MONTH, 1)
        endCal.add(Calendar.MILLISECOND, -1)
        val end = endCal.timeInMillis
        return Pair(start, end)
    }

    // --- 4. OPERACE S TRANSAKCEMI ---

    // Upravená funkce pro přidání (řeší i kategorii)
    fun addTransaction(title: String, amount: Double, type: String, category: String, date: Long) {
        viewModelScope.launch {
            // A) Nejprve zkusíme uložit kategorii (pro případ, že uživatel napsal novou)
            dao.insertCategory(CategoryEntity(category))

            // B) Poté uložíme samotnou transakci
            val newTransaction = TransactionEntity(
                title = title,
                amount = amount,
                type = type,
                category = category,
                date = date
            )
            dao.insertTransaction(newTransaction)
        }
    }

    // Upravená funkce pro aktualizaci (řeší i kategorii)
    fun updateTransaction(t: TransactionEntity) {
        viewModelScope.launch {
            // I při editaci uložíme kategorii, kdyby ji uživatel přepsal
            dao.insertCategory(CategoryEntity(t.category))
            dao.updateTransaction(t)
        }
    }
    fun deleteCategory(categoryName: String) {
        viewModelScope.launch {
            dao.deleteCategoryByName(categoryName)
        }
    }
    suspend fun getTransaction(id: Long) = dao.getTransactionById(id)

    fun deleteTransaction(t: TransactionEntity) = viewModelScope.launch { dao.deleteTransaction(t) }

    private fun getMonthName(month: Int): String {
        return arrayOf("Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec")[month]
    }
}

