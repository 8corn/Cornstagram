package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.corn.cornstagram.databinding.ActivityAddPwdBinding
import com.google.android.play.integrity.internal.n
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
        val phonenum = intent.getStringExtra("phonenum")
        val name = intent.getStringExtra("name")

        binding.addPwdNextbtn.setOnClickListener {
            val password = binding.addPwdPwdtext.text.toString().trim()
            val confirmPassword = binding.addPwdPwdtext2.text.toString().trim()

            if (password.isEmpty() || password.length < 8) {
                binding.addPwdPwdtext.error = "영소문자, 특수문자, 숫자를 포함하여 8자 이상 입력하시오."
            } else if (password != confirmPassword) {
                binding.addPwdPwdtext2.error = "비밀번호가 일치하지 않습니다."
            } else {
                signup(email, phonenum, password, name)
            }
        }
    }

    private fun signup(email: String?, phonenum: String?, password: String, name: String?) {
        if (!email.isNullOrEmpty()) {
            val intent = Intent(this, AddBirthActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("phonenum", phonenum)
            intent.putExtra("password", password)
            intent.putExtra("name", name)
            startActivity(intent)
        } else if (!phonenum.isNullOrEmpty()) {
            val intent = Intent(this, AddBirthActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("phonenum", phonenum)
            intent.putExtra("password", password)
            intent.putExtra("name", name)
            startActivity(intent)
        } else {
            Toast.makeText(this, "이메일 또는 전화번호를 입력하시오", Toast.LENGTH_SHORT).show()
            val intent2 = Intent(this, NumEmailActivity::class.java)
            startActivity(intent2)
        }
    }
}