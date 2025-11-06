package com.example.myapp005toastsnackbar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // Nastavení akce pro tlačítko Toast
        binding.btnShowToast.setOnClickerListener {
            val toast = Toast.makeText(this, "Nazdar - MÁM HLAD", Toast.LENGTH_LONG)
            toast.show()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nastavení akce pro tlačítko SnackBar
        binding.btnShowSnackBar.setOnClickerListener {
            Snackbar.make(binding.root, text = "Jsem Snackbar = jsem víc než TOAST", )

                .setBackgroundTint(Color.parseColor(colorString = "#FFCC50"))
                .setTextColor(Color.BLACK)
                .setDuration(7000)
                .setActionTextColor(Color.WHITE)
                .setAction(text = "Zavřít") {
                    Toast.makeText(this, "Zavírám SNACKBAR", Toast.LENGTH_SHORT).show()

                }
                .show() 
    }
}