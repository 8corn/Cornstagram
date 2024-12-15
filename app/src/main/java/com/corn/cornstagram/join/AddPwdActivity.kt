package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityAddPwdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddPwdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPwdBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPwdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val email = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")

        binding.addPwdNextbtn.setOnClickListener {
            val pwd = binding.addPwdPwdtext.text.toString().trim()
            val confirmPassword = binding.addPwdPwdtext2.text.toString().trim()

            if (pwd.isEmpty() || pwd.length < 8) {
                binding.addPwdPwdtext.error = "영소문자, 특수문자, 숫자를 포함하여 8자 이상 입력하시오."
            } else if (pwd != confirmPassword) {
                binding.addPwdPwdtext2.error = "비밀번호가 일치하지 않습니다."
            } else {
                val intent = Intent(this, AddBirthActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("password", pwd)
                intent.putExtra("name", name)
                startActivity(intent)
            }
        }
    }
}