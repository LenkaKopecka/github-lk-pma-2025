package com.example.myapp017christmasapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppRepository(private val context: Context) {
    private val dataStore = context.dataStore

    // --- DOPIS ---
    // Načtení (vrací Flow = proud dat)
    val dopisFlow: Flow<String> = dataStore.data.map { prefs ->
        prefs[AppKeys.DOPIS_TEXT] ?: "" // Pokud nic není, vrať prázdný text
    }
    // Uložení
    suspend fun ulozDopis(text: String) {
        dataStore.edit { prefs -> prefs[AppKeys.DOPIS_TEXT] = text }
    }

    // --- JMÉNO ---
    val jmenoFlow: Flow<String> = dataStore.data.map { prefs ->
        prefs[AppKeys.JMENO_UZIVATELE] ?: "Neznámý Elf"
    }
    suspend fun ulozJmeno(jmeno: String) {
        dataStore.edit { prefs -> prefs[AppKeys.JMENO_UZIVATELE] = jmeno }
    }

    // --- MÓD ---
    val modFlow: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[AppKeys.VANOCNI_MOD] ?: false
    }
    suspend fun prepniMod(zapnuto: Boolean) {
        dataStore.edit { prefs -> prefs[AppKeys.VANOCNI_MOD] = zapnuto }
    }
}