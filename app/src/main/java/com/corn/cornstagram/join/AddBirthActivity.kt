package com.corn.cornstagram.join

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityAddBirthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class AddBirthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBirthBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBirthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val now = LocalDate.now()
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val phonenum = intent.getStringExtra("phonenum")
        val pwd = intent.getStringExtra("password")

        binding.addBirthText.setOnClickListener {
            DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_DARK,
                object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    binding.addBirthText.setText("${year}/${month + 1}/${dayOfMonth}")
                }
            }, now.year, now.monthValue - 1, now.dayOfMonth).show()
        }

        binding.addBirthNextbtn.setOnClickListener {
            val birth = binding.addBirthText.text.toString().trim()

            if(birth.isNotEmpty()) {
                saveToFirebase(email, phonenum, pwd, name, birth)
            } else {
                binding.addBirthText.error = "생년월일을 선택하세요"
            }
        }

        binding.addBirthSkip.setOnClickListener {
            saveToFirebase(email, phonenum, pwd, name, null)
        }
    }
    private fun saveToFirebase(email: String?, phonenum: String?, pwd: String?, name: String?, birth: String?) {
        val uid = System.currentTimeMillis().toString()
        val userData = hashMapOf(
            "uid" to uid,
            "email" to email,
            "phonenum" to phonenum,
            "password" to pwd,
            "name" to name,
            "birth" to (birth ?: "null"),
        )
        firestore.collection("users")
            .document(uid)
            .set(userData)
            .addOnSuccessListener {
                val intent = Intent(this, AgreeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "회원 정보 저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}