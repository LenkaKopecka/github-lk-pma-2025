package com.example.myappbudget.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myappbudget.R
import com.example.myappbudget.data.database.TransactionEntity
import com.example.myappbudget.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var binding: FragmentDashboardBinding? = null
    private val viewModel: BudgetViewModel by activityViewModels()
    private var currentIncome = 0.0
    private var currentExpense = 0.0
    private var currentLimit = 0
    private var currentCurrency = "Kč"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)

        setupChart()

        // --- 1. OVLÁDÁNÍ DATA (TOTO JE NOVÉ) ---
        // Sledování textu měsíce (Leden 2026)
        viewModel.currentMonthText.observe(viewLifecycleOwner) { text ->
            binding?.tvCurrentMonth?.text = text
        }

        // Tlačítko Předchozí měsíc
        binding?.btnPrevMonth?.setOnClickListener {
            viewModel.changeMonth(-1)
        }

        // Tlačítko Další měsíc
        binding?.btnNextMonth?.setOnClickListener {
            viewModel.changeMonth(1)
        }
        // ---------------------------------------

        viewModel.currency.observe(viewLifecycleOwner) { symbol ->
            currentCurrency = symbol
            updateUI()
            updateLimitUI()
        }

        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            currentIncome = income ?: 0.0
            updateUI()
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
            currentExpense = expense ?: 0.0
            updateUI()
            updateLimitUI()
        }

        viewModel.monthlyLimit.observe(viewLifecycleOwner) { limit ->
            currentLimit = limit ?: 0
            updateLimitUI()
        }

        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            calculateTopCategory(transactions)
        }

        binding?.fabAdd?.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_addEdit)
        }
    }

    private fun updateLimitUI() {
        if (currentLimit == 0) {
            binding?.pbLimit?.progress = 0
            binding?.tvLimitText?.text = "Limit nenastaven"
            // Pokud není limit, dáme text světle šedý, aby byl na tmavém pozadí vidět
            binding?.tvLimitText?.setTextColor(Color.parseColor("#B0BEC5"))
            binding?.pbLimit?.progressTintList = ColorStateList.valueOf(Color.parseColor("#E0E0E0"))
            return
        }

        val expense = currentExpense.toInt()
        val percentage = if (currentLimit > 0) (expense * 100) / currentLimit else 0

        binding?.pbLimit?.progress = percentage.coerceAtMost(100)
        binding?.tvLimitText?.text = "$expense / $currentLimit $currentCurrency"

        if (expense > currentLimit) {
            // PŘEKROČENO: Červená (zvolil jsem světlejší červenou, aby byla na tmavé kartě čitelná)
            binding?.pbLimit?.progressTintList = ColorStateList.valueOf(Color.parseColor("#FF5252"))
            binding?.tvLimitText?.setTextColor(Color.parseColor("#FF5252"))
        } else {
            // V POŘÁDKU: Bílá (Tady byla chyba, bylo tu Color.BLACK)
            binding?.pbLimit?.progressTintList = ColorStateList.valueOf(Color.parseColor("#2196F3")) // Modrý progress
            binding?.tvLimitText?.setTextColor(Color.WHITE) // <--- TOTO JE TA OPRAVA
        }
    }

    private fun calculateTopCategory(transactions: List<TransactionEntity>) {
        val expenses = transactions.filter { it.type == "EXPENSE" }

        if (expenses.isEmpty()) {
            binding?.tvTopCategoryName?.text = "Zatím žádná data"
            binding?.tvTopCategoryAmount?.text = ""
            return
        }

        val categorySums = expenses.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        val topCategory = categorySums.maxByOrNull { it.value }

        if (topCategory != null) {
            binding?.tvTopCategoryName?.text = topCategory.key
            binding?.tvTopCategoryAmount?.text = "${topCategory.value} $currentCurrency"
        }
    }

    private fun updateUI() {
        val balance = currentIncome - currentExpense
        binding?.tvBalance?.text = "$balance $currentCurrency"
        binding?.tvIncome?.text = "$currentIncome $currentCurrency"
        binding?.tvExpense?.text = "$currentExpense $currentCurrency"
        updateChartData()
    }

    private fun setupChart() {
        binding?.pieChart?.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setHoleColor(Color.TRANSPARENT)
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(12f)
            centerText = "Bilance"
            setCenterTextSize(14f)

            // NOVINKA: Odstraní vnitřní okraje grafu, aby byl větší
            setExtraOffsets(0f, 0f, 0f, 0f)
        }
    }

    private fun updateChartData() {
        if (currentIncome == 0.0 && currentExpense == 0.0) {
            binding?.pieChart?.clear()
            return
        }

        val entries = ArrayList<PieEntry>()
        if (currentIncome > 0) entries.add(PieEntry(currentIncome.toFloat(), "Příjmy"))
        if (currentExpense > 0) entries.add(PieEntry(currentExpense.toFloat(), "Výdaje"))

        val colors = ArrayList<Int>()
        if (currentIncome > 0) colors.add(Color.parseColor("#4CAF50"))
        if (currentExpense > 0) colors.add(Color.parseColor("#F44336"))

        val dataSet = PieDataSet(entries, "Budget")
        dataSet.colors = colors
        dataSet.valueTextSize = 16f
        dataSet.valueTextColor = Color.WHITE

        val data = PieData(dataSet)
        binding?.pieChart?.data = data
        binding?.pieChart?.animateY(1000)
        binding?.pieChart?.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}