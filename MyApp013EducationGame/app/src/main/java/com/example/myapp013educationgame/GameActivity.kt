package com.example.myapp013educationgame

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp013educationgame.data.AppDatabase
import com.example.myapp013educationgame.data.GameResult
import com.example.myapp013educationgame.data.Question
import com.example.myapp013educationgame.databinding.ActivityGameBinding
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private val db by lazy { AppDatabase.getDatabase(this) }

    private var questionsList = listOf<Question>()
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startGame()
    }

    private fun startGame() {
        lifecycleScope.launch {
            // Načtení 5 náhodných otázek z DB
            questionsList = db.gameDao().getRandomQuestions()
            if (questionsList.isNotEmpty()) {
                showQuestion()
            } else {
                Toast.makeText(this@GameActivity, "Žádné otázky v DB!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showQuestion() {
        val q = questionsList[currentQuestionIndex]

        binding.tvQuestionCount.text = "Otázka ${currentQuestionIndex + 1} / ${questionsList.size}"
        binding.tvQuestionText.text = q.text

        binding.btnA.text = q.answerA
        binding.btnB.text = q.answerB
        binding.btnC.text = q.answerC
        binding.btnD.text = q.answerD

        // Nastavení listenerů
        binding.btnA.setOnClickListener { checkAnswer(0) }
        binding.btnB.setOnClickListener { checkAnswer(1) }
        binding.btnC.setOnClickListener { checkAnswer(2) }
        binding.btnD.setOnClickListener { checkAnswer(3) }
    }

    private fun checkAnswer(selectedIndex: Int) {
        val correctIndex = questionsList[currentQuestionIndex].correctIndex

        if (selectedIndex == correctIndex) {
            score += 10 // 10 bodů za správnou odpověď
            Toast.makeText(this, "Správně!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Chyba!", Toast.LENGTH_SHORT).show()
        }

        currentQuestionIndex++
        if (currentQuestionIndex < questionsList.size) {
            showQuestion()
        } else {
            finishGame()
        }
    }

    private fun finishGame() {
        lifecycleScope.launch {
            // 1. Uložit výsledek
            val result = GameResult(score = score)
            db.gameDao().insertResult(result)

            // 2. Aktualizovat XP uživatele
            val user = db.gameDao().getUser()
            user?.let {
                val updatedUser = it.copy(totalXp = it.totalXp + score)
                db.gameDao().updateUser(updatedUser)
            }

            Toast.makeText(this@GameActivity, "Hra končí! Skóre: $score", Toast.LENGTH_LONG).show()
            finish() // Návrat do menu
        }
    }
}