package com.example.myappbudget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment // Důležitý import
import androidx.navigation.ui.setupWithNavController // Důležitý import
import com.example.myappbudget.databinding.ActivityMainBinding // Důležitý import bindingu

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializace ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Najdeme NavHostFragment
        // POZOR: R.id.nav_host_fragment musí existovat v activity_main.xml
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        // 2. Propojíme BottomNavigationView s NavControllerem
        // POZOR: binding.bottomNav funguje jen pokud máš v XML id="@+id/bottom_nav"
        binding.bottomNav.setupWithNavController(navController)
    }
}