package com.corn.cornstagram

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityVerificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        verificationId = intent.getStringExtra("verificationId")

        binding.verificationBtn.setOnClickListener {
            val code = binding.verificationCode.text.toString().trim()

            if (code.isEmpty()) {
                Toast.makeText(this, "인증 코드를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.verificationBar.visibility = View.VISIBLE
            verifivationCode(code)
        }
    }

    private fun verifivationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                binding.verificationBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "인증 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}