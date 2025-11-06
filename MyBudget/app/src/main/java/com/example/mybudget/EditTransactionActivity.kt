package com.example.mybudget

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mybudget.databinding.ActivityEditTransactionBinding

class EditTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTransactionBinding
    private lateinit var dbHelper: MyDatabaseHelper

    private var currentId: Int? = null  // null = nová transakce

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Transakce"

        dbHelper = MyDatabaseHelper(this)

        // Zjistíme, jestli přišlo ID
        val idFromIntent = intent.getIntExtra("TRANSACTION_ID", -1)
        if (idFromIntent != -1) {
            currentId = idFromIntent
            loadTransaction(idFromIntent)
        } else {
            // Nová transakce -> nemá smysl mazat
            binding.btnDelete.isEnabled = false
        }

        binding.btnSave.setOnClickListener {
            saveTransaction()
        }

        binding.btnDelete.setOnClickListener {
            deleteTransaction()
        }
    }

    private fun loadTransaction(id: Int) {
        val transaction = dbHelper.getTransactionById(id)
        if (transaction != null) {
            binding.etAmount.setText(transaction.amount.toString())
            binding.etNote.setText(transaction.note)
        }
    }

    private fun saveTransaction() {
        val amountText = binding.etAmount.text.toString().trim()
        val noteText = binding.etNote.text.toString().trim()

        if (amountText.isEmpty()) {
            Toast.makeText(this, "Zadejte částku", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null) {
            Toast.makeText(this, "Neplatná částka", Toast.LENGTH_SHORT).show()
            return
        }

        val note = if (noteText.isEmpty()) "-" else noteText

        if (currentId == null) {
            // CREATE
            dbHelper.insertTransaction(amount, note)
            Toast.makeText(this, "Transakce uložena", Toast.LENGTH_SHORT).show()
        } else {
            // UPDATE
            dbHelper.updateTransaction(currentId!!, amount, note)
            Toast.makeText(this, "Transakce upravena", Toast.LENGTH_SHORT).show()
        }

        finish() // vrátí se na MainActivity
    }

    private fun deleteTransaction() {
        val id = currentId ?: return
        dbHelper.deleteTransaction(id)
        Toast.makeText(this, "Transakce smazána", Toast.LENGTH_SHORT).show()
        finish()
    }
}
//vytvořit novou transakci (když nepřijde žádné ID)
//upravit existující (když přijde ID)
//smazat existující