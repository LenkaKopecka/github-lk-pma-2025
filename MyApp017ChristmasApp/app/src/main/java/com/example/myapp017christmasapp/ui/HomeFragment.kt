package com.example.myapp017christmasapp.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp017christmasapp.R
import com.example.myapp017christmasapp.data.AppRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = AppRepository(requireContext())
        val textUvitani = view.findViewById<TextView>(R.id.textUvitani)

        // Sledujeme změnu jména v DataStore
        lifecycleScope.launch {
            repo.jmenoFlow.collectLatest { jmeno ->
                textUvitani.text = "Vítej, $jmeno!\n\nJe čas napsat Ježíškovi."
            }
        }
    }
}