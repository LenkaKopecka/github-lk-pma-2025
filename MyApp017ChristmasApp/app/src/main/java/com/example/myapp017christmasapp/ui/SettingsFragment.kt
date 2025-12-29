package com.example.myapp017christmasapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp017christmasapp.R
import com.example.myapp017christmasapp.data.AppRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = AppRepository(requireContext())

        val inputJmeno = view.findViewById<EditText>(R.id.inputJmeno)
        val btnUlozit = view.findViewById<Button>(R.id.btnUlozit)
        val switchMod = view.findViewById<Switch>(R.id.switchMod)

        // Načtení dat do formuláře
        lifecycleScope.launch {
            repo.jmenoFlow.collectLatest { jmeno ->
                if (inputJmeno.text.isEmpty()) inputJmeno.setText(jmeno)
            }
        }
        lifecycleScope.launch {
            repo.modFlow.collectLatest { zapnuto -> switchMod.isChecked = zapnuto }
        }

        // Uložení jména
        btnUlozit.setOnClickListener {
            lifecycleScope.launch { repo.ulozJmeno(inputJmeno.text.toString()) }
        }

        // Přepnutí módu
        switchMod.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch { repo.prepniMod(isChecked) }
        }
    }
}