package com.example.myapp002myownconstraintlayout

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    // Cílové množství vody
    private val waterGoal = 2000
    private var currentWater = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etWater = findViewById<EditText>(R.id.etWater)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnClear = findViewById<Button>(R.id.btnClear)

        progressBar.max = waterGoal

        // Přidání vody
        btnAdd.setOnClickListener {
            val input = etWater.text.toString()
            if (input.isEmpty()) {
                Toast.makeText(this, "Zadejte množství vody!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val addedWater = input.toIntOrNull()
            if (addedWater == null || addedWater <= 0) {
                Toast.makeText(this, "Zadejte platné číslo!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            currentWater += addedWater
            if (currentWater > waterGoal) currentWater = waterGoal

            tvResult.text = "Už jste vypili $currentWater ml vody.\n" +
                    "Ještě doporučeno: ${waterGoal - currentWater} ml."

            progressBar.progress = currentWater

            etWater.text.clear()
        }

        // Vymazání všech hodnot
        btnClear.setOnClickListener {
            currentWater = 0
            tvResult.text = "Zde uvidíte svůj pokrok"
            progressBar.progress = 0
            etWater.text.clear()
        }
    }
}