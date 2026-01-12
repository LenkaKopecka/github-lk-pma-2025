package com.example.myappbudget.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Toto vytvoří datastore (paměť) s názvem "settings"
private val Context.dataStore by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {

    // Definice klíčů (názvů šuplíků), kam budeme ukládat
    companion object {
        val MONTHLY_LIMIT = intPreferencesKey("monthly_limit")
        val CURRENCY = stringPreferencesKey("currency")
    }

    // --- ČTENÍ DAT (Flow) ---
    val monthlyLimitFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[MONTHLY_LIMIT] ?: 0 // Pokud nic není uloženo, vrať 0
        }

    val currencyFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[CURRENCY] ?: "Kč" // Pokud nic není uloženo, vrať "Kč"
        }

    // Funkce pro uložení limitu
    suspend fun updateMonthlyLimit(limit: Int) {
        context.dataStore.edit { preferences ->
            preferences[MONTHLY_LIMIT] = limit
        }
    }

    // Funkce pro uložení měny
    suspend fun updateCurrency(symbol: String) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY] = symbol
        }
    }
}