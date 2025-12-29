package com.example.myapp017christmasapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapp017christmasapp.data.AppRepository
import com.example.myapp017christmasapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializace ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nastavení Navigace
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Propojení Toolbaru
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)

        // Propojení spodního menu s navigací
        binding.bottomNav.setupWithNavController(navController)

        // Bonus: Změna barvy podle "Vánoční nálady"
        val repo = AppRepository(this)
        lifecycleScope.launch {
            repo.modFlow.collectLatest { jeVanocniMod ->
                if (jeVanocniMod) {
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.RED))
                    binding.toolbar.title = "Šťastné a Veselé!"
                } else {
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#6200EE")))
                    binding.toolbar.title = "Vánoční Pošta"
                }
            }
        }
    }

    // Aby fungovalo tlačítko Zpět v liště (pokud bys šel hlouběji v navigaci)
    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}