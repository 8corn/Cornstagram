package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityCheckAkaBinding
import com.corn.cornstagram.start.LoginActivity

class CheckAkaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckAkaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckAkaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val akaName = intent.getStringExtra("akaname")

        binding.checkAkaTxt.text = "$akaName 님으로\n가입하시겠어요?"

        binding.checkAkaLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.checkAkaNext.setOnClickListener {
            val intent = Intent(this, FindNumActivity::class.java)
            startActivity(intent)
        }
    }
}