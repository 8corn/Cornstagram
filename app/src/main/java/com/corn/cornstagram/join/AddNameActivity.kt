package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityAddNameBinding

class AddNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addNameNextbtn.setOnClickListener {
            val intent = Intent(this, AddPwdActivity::class.java)
            startActivity(intent)
        }
    }
}