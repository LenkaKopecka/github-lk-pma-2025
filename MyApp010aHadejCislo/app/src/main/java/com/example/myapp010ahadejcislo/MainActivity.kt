package com.example.guessnumber

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var editGuess: EditText
    private lateinit var btnCheck: Button

    private var target: Int = 0

    companion object {
        private const val KEY_TARGET = "key_target"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        editGuess = findViewById(R.id.editGuess)
        btnCheck = findViewById(R.id.btnCheck)

        target = savedInstanceState?.getInt(KEY_TARGET) ?: newTarget()

        btnCheck.setOnClickListener { checkGuess() }

        // Odeslání z klávesnice (Enter/Done)
        editGuess.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkGuess()
                true
            } else false
        }
    }

    private fun checkGuess() {
        val text = editGuess.text?.toString()?.trim()
        val guess = text?.toIntOrNull()

        if (guess == null) {
            Toast.makeText(this, getString(R.string.msg_empty), Toast.LENGTH_SHORT).show()
            return
        }
        if (guess !in 1..10) {
            Toast.makeText(this, getString(R.string.msg_range), Toast.LENGTH_SHORT).show()
            return
        }

        if (guess == target) {
            Toast.makeText(this, getString(R.string.msg_correct), Toast.LENGTH_SHORT).show()
            target = newTarget()
            editGuess.text?.clear()
        } else {
            Toast.makeText(this, getString(R.string.msg_wrong), Toast.LENGTH_SHORT).show()
        }
    }

    private fun newTarget(): Int = Random.nextInt(1, 11)

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_TARGET, target)
        super.onSaveInstanceState(outState)
    }
}
