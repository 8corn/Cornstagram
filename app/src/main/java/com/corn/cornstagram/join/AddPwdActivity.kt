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
        val phonenum = intent.getStringExtra("phonenum")

        binding.addPwdNextbtn.setOnClickListener {
            val password = binding.addPwdPwdtext.text.toString().trim()
            val confirmPassword = binding.addPwdPwdtext2.text.toString().trim()
            if (password.isEmpty() || password.length < 6) {
                binding.addPwdPwdtext.error = "6자리이상 입력하시오."
            } else if (password != confirmPassword) {
                binding.addPwdPwdtext2.error = "비밀번호가 일치하지 않습니다."
            } else {
                signup(email, phonenum, password)
            }
        }
    }

    private fun signup(email: String?, phonenum: String?, password: String) {
        if (!email.isNullOrEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser?.uid ?: System.currentTimeMillis().toString()
                        saveUserToFirestore(user, email, null, password)
                    } else {
                        Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else if (!phonenum.isNullOrEmpty()) {
            val uid = System.currentTimeMillis().toString()
            saveUserToFirestore(uid, null, phonenum, password)
        } else {
            Toast.makeText(this, "이메일 또는 전화번호를 입력하시오", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveUserToFirestore(
        uid: String,
        email: String?,
        phonenum: String?,
        password: String
    ) {
        val userData = hashMapOf(
            "uid" to uid,
            "email" to email,
            "phonenum" to phonenum,
            "password" to password
        )
        firestore.collection("users")
            .document(uid)
            .set(userData)
            .addOnSuccessListener {
                val intent = Intent(this, AddBirthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "회원 정보 저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}