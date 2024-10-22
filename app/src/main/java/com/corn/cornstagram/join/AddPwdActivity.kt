package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.SecurityUtils
import com.corn.cornstagram.databinding.ActivityAddPwdBinding

class AddPwdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPwdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPwdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addPwdCheckbtn.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                binding.addPwdNextbtn.isEnabled = true
            } else {
                binding.addPwdNextbtn.isEnabled = false
            }
        }

        val password = "password"

        val salt = SecurityUtils.generateSalt()

        val hashedPassword = SecurityUtils.hashPassword(password, salt)

        println("해싱된 비밀번호 : $hashedPassword")

        binding.addPwdChecktext.setOnClickListener {
            val isChecked =! binding.addPwdCheckbtn.isChecked
            binding.addPwdCheckbtn.isChecked = isChecked
            binding.addPwdNextbtn.isEnabled = isChecked
        }

        binding.addPwdNextbtn.setOnClickListener {
            val intent = Intent(this, AddBirthActivity::class.java)
            startActivity(intent)
        }
    }
}