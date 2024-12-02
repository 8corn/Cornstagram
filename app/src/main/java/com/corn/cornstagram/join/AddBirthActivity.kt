package com.corn.cornstagram.join

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityAddBirthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate

class AddBirthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBirthBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBirthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.addBirthText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.performClick()
                v.clearFocus()
                return@setOnTouchListener true
            }
            false
        }

        val now = LocalDate.now()

        binding.addBirthText.setOnClickListener {
            DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_DARK,
                object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    binding.addBirthText.setText("${year}/${month + 1}/${dayOfMonth}")
                }
            }, now.year, now.monthValue - 1, now.dayOfMonth).show()
        }

        binding.addBirthNextbtn.setOnClickListener {
            val selectedDate = binding.addBirthText.text.toString().trim()
            if(selectedDate.isNotEmpty()) {
                saveDateToFirebase(selectedDate)
                val intent = Intent(this, AgreeActivity::class.java)
                startActivity(intent)
            } else {
                binding.addBirthText.error = "생년월일을 선택하세요"
            }
        }
    }
    private fun saveDateToFirebase(selectedDate: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("users").child(userId)

            userRef.child("birthDate").setValue(selectedDate)
                .addOnFailureListener {
                    Log.e("Firebase", "생년월일 저장 실패", it)
                }
        } else {
            Log.e("Firebase", "사용자가 로그인되어 있지 않음")
        }
    }
}