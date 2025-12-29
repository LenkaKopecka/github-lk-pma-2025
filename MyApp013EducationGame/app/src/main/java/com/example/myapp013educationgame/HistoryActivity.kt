package com.example.myapp013educationgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp013educationgame.data.AppDatabase
import com.example.myapp013educationgame.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val db by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadHistory()
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            val results = db.gameDao().getAllResults()

            if (results.isEmpty()) {
                binding.tvHistoryList.text = "Zatím žádné odehrané hry."
            } else {
                val sb = StringBuilder()
                val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

                results.forEach { result ->
                    val dateStr = dateFormat.format(Date(result.timestamp))
                    sb.append("Skóre: ${result.score} bodů\nDatum: $dateStr\n\n----------------\n\n")
                }
                binding.tvHistoryList.text = sb.toString()
            }
        }
    }
}