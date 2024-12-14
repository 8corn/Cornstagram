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

        val email = intent.getStringExtra("email")
        val phonenum = intent.getStringExtra("phonenum")

        binding.addNameNextbtn.setOnClickListener {
            val name = binding.addNameText.text.toString().trim()

            if (name.isEmpty()) {
                binding.addNameText.error = "이름을 입력하세요."
                return@setOnClickListener
            }

            val intent = Intent(this, AddPwdActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("phonenum", phonenum)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}