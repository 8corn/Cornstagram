package com.corn.cornstagram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.corn.cornstagram.databinding.ActivityMainBinding
import com.corn.cornstagram.fragment.LikeFragment
import com.corn.cornstagram.fragment.MainFragment
import com.corn.cornstagram.fragment.PlusFragment
import com.corn.cornstagram.fragment.ProfileFragment
import com.corn.cornstagram.fragment.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(MainFragment())

        binding.mainBottomNav.itemIconTintList = null
        binding.mainBottomNav.itemIconSize = resources.getDimensionPixelSize(android.R.dimen.thumbnail_width)

        binding.mainBottomNav.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when(item.itemId) {
                R.id.nav_home -> selectedFragment = MainFragment()
                R.id.nav_search -> selectedFragment = SearchFragment()
                R.id.nav_plus -> selectedFragment = PlusFragment()
                R.id.nav_like -> selectedFragment = LikeFragment()
                R.id.nav_profile -> selectedFragment = ProfileFragment()
            }
            loadFragment(selectedFragment!!)
            true
        }


    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_control_nav, fragment)
            .commit()
    }
}