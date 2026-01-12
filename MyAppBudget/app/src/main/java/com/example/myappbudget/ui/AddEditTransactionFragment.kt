package com.example.myappbudget.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels // Důležité: sdílený ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myappbudget.R
import com.example.myappbudget.data.database.TransactionEntity
import com.example.myappbudget.databinding.FragmentAddEditTransactionBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEditTransactionFragment : Fragment(R.layout.fragment_add_edit_transaction) {

    private var binding: FragmentAddEditTransactionBinding? = null
    // Používáme activityViewModels, aby to bylo propojené se zbytkem aplikace
    private val viewModel: BudgetViewModel by activityViewModels()
    private var currentTransaction: TransactionEntity? = null

    private var selectedDate: Long = System.currentTimeMillis()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEditTransactionBinding.bind(view)

        setupCategoryDropdown()
        updateDateInView()

        // Kliknutí na pole Datum -> Otevřít kalendář
        binding?.etDate?.setOnClickListener {
            showDatePicker()
        }

        // NOVINKA: Kliknutí na "Spravovat kategorie"
        binding?.tvManageCategories?.setOnClickListener {
            showDeleteCategoryDialog()
        }

        val transactionId = arguments?.getLong("transaction_id", -1L) ?: -1L
        if (transactionId != -1L) {
            loadTransactionData(transactionId)
        }

        binding?.btnSave?.setOnClickListener {
            saveTransaction()
        }

        binding?.btnDelete?.setOnClickListener {
            deleteTransaction()
        }

        binding?.btnCancel?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // --- NOVÉ FUNKCE PRO MAZÁNÍ KATEGORIÍ ---
    private fun showDeleteCategoryDialog() {
        val currentCategories = viewModel.categories.value ?: emptyList()
        val categoryNames = currentCategories.map { it.name }.toTypedArray()

        if (categoryNames.isEmpty()) {
            Toast.makeText(context, "Žádné kategorie ke smazání", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Vyber kategorii ke smazání")
            .setItems(categoryNames) { _, which ->
                val categoryToDelete = categoryNames[which]
                confirmDeleteCategory(categoryToDelete)
            }
            .setNegativeButton("Zrušit", null)
            .show()
    }

    private fun confirmDeleteCategory(name: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Smazat '$name'?")
            .setMessage("Opravdu chceš tuto kategorii odstranit z nabídky? Existující transakce zůstanou zachovány.")
            .setPositiveButton("Smazat") { _, _ ->
                viewModel.deleteCategory(name)
                Toast.makeText(context, "Kategorie '$name' smazána", Toast.LENGTH_SHORT).show()
                // Pokud uživatel zrovna měl tuto kategorii vybranou v poli, vymažeme ji
                if (binding?.etCategory?.text.toString() == name) {
                    binding?.etCategory?.setText("")
                }
            }
            .setNegativeButton("Ne", null)
            .show()
    }
    // ----------------------------------------

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDate

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val newCalendar = Calendar.getInstance()
            newCalendar.set(selectedYear, selectedMonth, selectedDay)
            selectedDate = newCalendar.timeInMillis
            updateDateInView()
        }, year, month, day)

        datePicker.show()
    }

    private fun updateDateInView() {
        val format = SimpleDateFormat("d. M. yyyy", Locale.getDefault())
        binding?.etDate?.setText(format.format(selectedDate))
    }

    private fun loadTransactionData(id: Long) {
        lifecycleScope.launch {
            currentTransaction = viewModel.getTransaction(id)
            currentTransaction?.let { t ->
                binding?.tvHeader?.text = "Upravit transakci"
                binding?.etTitle?.setText(t.title)
                binding?.etAmount?.setText(t.amount.toString())
                binding?.etCategory?.setText(t.category, false) // false = nefiltrovat

                selectedDate = t.date
                updateDateInView()

                if (t.type == "INCOME") {
                    binding?.rbIncome?.isChecked = true
                } else {
                    binding?.rbExpense?.isChecked = true
                }

                binding?.btnDelete?.visibility = View.VISIBLE
                binding?.btnSave?.text = "Aktualizovat"
            }
        }
    }

    private fun saveTransaction() {
        val title = binding?.etTitle?.text.toString()
        val amountStr = binding?.etAmount?.text.toString()
        val category = binding?.etCategory?.text.toString()

        if (title.isBlank() || amountStr.isBlank()) {
            Toast.makeText(context, "Vyplň název a částku", Toast.LENGTH_SHORT).show()
            return
        }
        if (category.isBlank() || category == "null") {
            Toast.makeText(context, "Vyber nebo napiš kategorii", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull() ?: 0.0
        val isIncome = binding?.rbIncome?.isChecked == true
        val type = if (isIncome) "INCOME" else "EXPENSE"

        if (currentTransaction == null) {
            viewModel.addTransaction(title, amount, type, category, selectedDate)
        } else {
            val updatedTransaction = currentTransaction!!.copy(
                title = title,
                amount = amount,
                type = type,
                category = category,
                date = selectedDate
            )
            viewModel.updateTransaction(updatedTransaction)
        }

        Toast.makeText(context, "Uloženo!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    private fun deleteTransaction() {
        AlertDialog.Builder(requireContext())
            .setTitle("Smazat transakci?")
            .setMessage("Opravdu chceš smazat tuto položku?")
            .setPositiveButton("Ano") { _, _ ->
                currentTransaction?.let {
                    viewModel.deleteTransaction(it)
                    Toast.makeText(context, "Smazáno", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
            .setNegativeButton("Ne", null)
            .show()
    }

    private fun setupCategoryDropdown() {
        viewModel.categories.observe(viewLifecycleOwner) { categoryList ->
            val categoryNames = categoryList.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryNames)
            binding?.etCategory?.setAdapter(adapter)
            binding?.etCategory?.setOnClickListener { binding?.etCategory?.showDropDown() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}