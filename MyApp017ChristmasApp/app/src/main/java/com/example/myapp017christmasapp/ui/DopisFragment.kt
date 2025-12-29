package com.example.myapp017christmasapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp017christmasapp.R
import com.example.myapp017christmasapp.data.AppRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DopisFragment : Fragment(R.layout.fragment_dopis) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = AppRepository(requireContext())
        val inputDopis = view.findViewById<EditText>(R.id.inputDopis)

        // 1. Načíst uložený text
        lifecycleScope.launch {
            repo.dopisFlow.collectLatest { text ->
                if (inputDopis.text.toString() != text) {
                    inputDopis.setText(text)
                }
            }
        }

        // 2. Automaticky ukládat při psaní
        inputDopis.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch { repo.ulozDopis(s.toString()) }
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        })
    }
}