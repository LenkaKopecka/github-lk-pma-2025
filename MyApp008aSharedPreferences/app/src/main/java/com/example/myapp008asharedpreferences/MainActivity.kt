package com.example.myapp008asharedpreferences

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp008asharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Funkcionalita pro ukladání dat
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val ageString = binding.etAge.text.toString().trim()

            if(ageString.isBlank()) {
                Toast.makeText(this, "Musíš vyplnit věk...", Toast.LENGTH_LONG).show()
            } else {
                val age = ageString.toInt()
                val isAdult = binding.cbAdult.isChecked
                if ((age < 18 && isAdult) || (age >= 18 && !isAdult)) {
                    Toast.makeText(this, "Kecáš, takže nic ukládat nebudu...", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Ukládám data...", Toast.LENGTH_LONG).show()
                    editor.apply {
                        putString("name", name)
                        putInt("age", age)
                        putBoolean("isAdult", isAdult)
                        apply()
                    }
                }
            }

        }



        // Funkcionalita pro načtení dat
        binding.btnLoad.setOnClickListener {
            val name = sharedPref.getString("name", null) // pokud není nic uložené, vrátí prázdný řetězec
            val age = sharedPref.getInt("age", 0) // pokud není nic uložené, vrátí -1
            val isAdult = sharedPref.getBoolean("isAdult", false) // defaultně false

            binding.etName.setText(name)
            binding.etAge.setText(age.toString())
            binding.cbAdult.isChecked = isAdult
        }


        }
    }
