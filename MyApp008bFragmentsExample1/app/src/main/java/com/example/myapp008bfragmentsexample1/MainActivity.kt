package com.example.myapp008bfragmentsexample1

import com.example.myapp008bfragmentsexample1.CatFragment
import com.example.myapp008bfragmentsexample1.DogFragment
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Najdeme tlačítka pomocí findViewById
        val btnCatFragment = findViewById<Button>(R.id.btnCatFragment)
        val btnDogFragment = findViewById<Button>(R.id.btnDogFragment)

        btnCatFragment.setOnClickListener {
            replaceFragment(CatFragment())
        }

        btnDogFragment.setOnClickListener {
            replaceFragment(DogFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
