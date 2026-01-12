package com.example.myappbudget.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myappbudget.R
import com.example.myappbudget.databinding.FragmentTransactionListBinding

class TransactionListFragment : Fragment(R.layout.fragment_transaction_list) {

    private var binding: FragmentTransactionListBinding? = null
    private val viewModel: BudgetViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTransactionListBinding.bind(view)

        // 1. Nastavení Adapteru
        val adapter = TransactionAdapter { transaction ->
            val bundle = Bundle().apply {
                putLong("transaction_id", transaction.id)
            }
            findNavController().navigate(R.id.action_list_to_addEdit, bundle)
        }

        binding?.recyclerView?.adapter = adapter
        // LayoutManager určuje, jak se položky skládají pod sebe
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)

        // Sledování MĚNY ---
        viewModel.currency.observe(viewLifecycleOwner) { symbol ->
            // Když se změní měna v nastavení, pošleme ji do adapteru
            adapter.currencySymbol = symbol
            adapter.notifyDataSetChanged() // Překreslit seznam
        }

        // 2. Sledování dat
        viewModel.allTransactions.observe(viewLifecycleOwner) { list ->
            // VÝPIS DO LOGU - uvidíme, kolik dat přišlo
            Log.d("BudgetApp", "Počet načtených transakcí: ${list.size}")

            if (list.isEmpty()) {
                // Pokud je seznam prázdný, zobrazíme hlášku (pokud máš v XML TextView pro empty state)
                Toast.makeText(context, "Žádné transakce v databázi", Toast.LENGTH_SHORT).show()
            }

            adapter.submitList(list)
        }

        binding?.fabAddList?.setOnClickListener {
            // Zde použijeme ID, které máš v nav_graph.xml
            findNavController().navigate(R.id.action_list_to_addEdit)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}