package com.example.myappbudget.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {

    // 1) Měna (např. "Kč", "€", "$")
    val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")

    // 2) Měsíční limit útraty (např. 20000)
    val MONTHLY_LIMIT = intPreferencesKey("monthly_limit")
    
}