package com.example.myappbudget.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels // Změna: Zde stačí viewModels(), nastavení nepotřebuje sdílený model
import com.example.myappbudget.R
import com.example.myappbudget.data.UserPreferencesRepository
import com.example.myappbudget.databinding.FragmentSettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var binding: FragmentSettingsBinding? = null
    private lateinit var userPreferences: UserPreferencesRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        userPreferences = UserPreferencesRepository(requireContext())

        // Načíst aktuální data a vyplnit políčka
        loadCurrentSettings()

        binding?.btnSaveSettings?.setOnClickListener {
            saveSettings()
        }
    }

    private fun loadCurrentSettings() {
        // Spustíme asynchronní načítání
        CoroutineScope(Dispatchers.IO).launch {
            val limit = userPreferences.monthlyLimitFlow.first()
            val currency = userPreferences.currencyFlow.first()

            withContext(Dispatchers.Main) {
                binding?.etMonthlyLimit?.setText(limit.toString())
                binding?.etCurrency?.setText(currency)
            }
        }
    }

    private fun saveSettings() {
        val limitStr = binding?.etMonthlyLimit?.text.toString()
        val currency = binding?.etCurrency?.text.toString()

        if (limitStr.isBlank() || currency.isBlank()) {
            Toast.makeText(context, "Vyplňte prosím obě pole", Toast.LENGTH_SHORT).show()
            return
        }

        val limit = limitStr.toIntOrNull() ?: 0

        CoroutineScope(Dispatchers.IO).launch {
            userPreferences.updateMonthlyLimit(limit)
            userPreferences.updateCurrency(currency)

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Nastavení uloženo ✅", Toast.LENGTH_SHORT).show()
                // Volitelné: Vrátit se zpět na Dashboard
                // findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}