package com.corn.cornstagram.join

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.corn.cornstagram.R
import com.corn.cornstagram.databinding.ActivityNumEmailBinding
import com.corn.cornstagram.fragment.EmailFragment
import com.corn.cornstagram.fragment.NumberFragment

class NumEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNumEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNumEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(NumberFragment())

        binding.numemailNumBtn.setOnClickListener {
            replaceFragment(NumberFragment())
        }

        binding.numemailEmailBtn.setOnClickListener {
            replaceFragment(EmailFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}