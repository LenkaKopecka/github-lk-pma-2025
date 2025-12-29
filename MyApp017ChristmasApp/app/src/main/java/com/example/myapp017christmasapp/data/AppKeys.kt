package com.example.myapp017christmasapp.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object AppKeys {
    // Klíč pro text dopisu
    val DOPIS_TEXT = stringPreferencesKey("dopis_text")
    // Klíč pro jméno uživatele
    val JMENO_UZIVATELE = stringPreferencesKey("jmeno_uzivatele")
    // Klíč pro vánoční mód (zapnuto/vypnuto)
    val VANOCNI_MOD = booleanPreferencesKey("vanocni_mod")
}