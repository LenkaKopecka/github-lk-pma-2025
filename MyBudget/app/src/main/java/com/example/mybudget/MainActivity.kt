package com.example.mybudget

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mybudget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: MyDatabaseHelper

    // Seznam transakcí v paměti
    private var transactionList: List<TransactionData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Top bar title
        supportActionBar?.title = "MyBudget"

        dbHelper = MyDatabaseHelper(this)

        // Tlačítko "Přidat transakci"
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, EditTransactionActivity::class.java)
            startActivity(intent)
        }

        // Klik na položku v seznamu = editace
        binding.lvTransactions.setOnItemClickListener { _, _, position, _ ->
            val clicked = transactionList[position]
            val intent = Intent(this, EditTransactionActivity::class.java)
            intent.putExtra("TRANSACTION_ID", clicked.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadTransactions()
    }

    private fun loadTransactions() {
        transactionList = dbHelper.getAllTransactions()
        val displayList = transactionList.map { t ->
            "${t.amount} Kč - ${t.note}"
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            displayList
        )
        binding.lvTransactions.adapter = adapter

        if (transactionList.isEmpty()) {
            Toast.makeText(this, "Zatím žádné transakce.", Toast.LENGTH_SHORT).show()
        }
    }
}

//„MainActivity je hlavní obrazovka. V onResume načítám transakce z databáze, zobrazím je v ListView přes ArrayAdapter a tady se dá kliknout na Přidat transakci nebo na konkrétní položku pro úpravu.“