package com.example.myapp017christmasapp.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// Toto je ten řádek, který definuje "dataStore" pro celou aplikaci
val Context.dataStore by preferencesDataStore(name = "vanocni_nastaveni")