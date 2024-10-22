package com.corn.cornstagram.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityStartBinding
import com.corn.cornstagram.join.NumEmailActivity

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startLoginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.startJoinBtn.setOnClickListener {
            val intent = Intent(this, NumEmailActivity::class.java)
            startActivity(intent)
        }
    }
}