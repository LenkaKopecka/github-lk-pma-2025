package com.example.myapp007afragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapp007afragments.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Otevřít Fragment1 (galerii) při startu s indexem 0 a názvem "Moje galerie"
        replaceFragment(Fragment1.newInstance("0", "Moje galerie"))

        binding.btnFragment1.setOnClickListener {
            replaceFragment(Fragment1.newInstance("0", "Moje galerie"))
        }

        binding.btnFragment2.setOnClickListener {
            // otevře Fragment2 (detail) pro první obrázek jako příklad
            // předáváme resource id jako String (to je design podle šablony)
            val imgResString = R.drawable.img1.toString()
            replaceFragment(Fragment2.newInstance(imgResString, "Ukázkový obrázek 1"))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}
