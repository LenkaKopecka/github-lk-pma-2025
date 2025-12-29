package com.example.myapp013educationgame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp013educationgame.data.AppDatabase
import com.example.myapp013educationgame.data.User
import com.example.myapp013educationgame.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db by lazy { AppDatabase.getDatabase(this) }
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUser()

        binding.btnSaveUser.setOnClickListener {
            val name = binding.etName.text.toString()
            if (name.isNotEmpty()) {
                createUser(name)
            } else {
                Toast.makeText(this, "Zadej jméno", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStartGame.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Znovu načíst data, když se vrátím ze hry (aktualizace XP)
        loadUser()
    }

    private fun loadUser() {
        lifecycleScope.launch {
            currentUser = db.gameDao().getUser()
            if (currentUser == null) {
                // Žádný uživatel -> Registrace
                binding.registrationLayout.visibility = View.VISIBLE
                binding.menuLayout.visibility = View.GONE
            } else {
                // Uživatel existuje -> Menu
                binding.registrationLayout.visibility = View.GONE
                binding.menuLayout.visibility = View.VISIBLE
                updateUI()
            }
        }
    }

    private fun createUser(name: String) {
        lifecycleScope.launch {
            val newUser = User(name = name)
            db.gameDao().insertUser(newUser)
            loadUser() // Znovu načíst
        }
    }

    private fun updateUI() {
        currentUser?.let {
            binding.tvWelcome.text = "Ahoj, ${it.name}!"
            binding.tvXp.text = "Tvé zkušenosti (XP): ${it.totalXp}"
        }
    }
}