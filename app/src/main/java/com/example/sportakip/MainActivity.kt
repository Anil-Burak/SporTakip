package com.example.sportakip

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("antrenman_prefs", Context.MODE_PRIVATE)
        val ilkAcilisYapildi = prefs.getBoolean("ilkAcilisYapildi", false)

        if (!ilkAcilisYapildi) {
            loadFragment(ProfilFragment())
            prefs.edit().putBoolean("ilkAcilisYapildi", true).apply()
        } else {
            loadFragment(GecmisFragment())
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_gecmis -> loadFragment(GecmisFragment())
                R.id.nav_antrenman -> loadFragment(AntrenmanFragment())
                R.id.nav_profil -> loadFragment(ProfilFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}