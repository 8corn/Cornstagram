package com.corn.cornstagram.join

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
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
                val intent = Intent(this, AgreeActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("phonenum", phonenum)
                intent.putExtra("password", pwd)
                intent.putExtra("name", name)
                intent.putExtra("birth", birth)
                startActivity(intent)
            } else {
                binding.addBirthText.error = "생년월일을 선택하세요"
            }
        }

        binding.addBirthSkip.setOnClickListener {
            val intent = Intent(this, AgreeActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("phonenum", phonenum)
            intent.putExtra("password", pwd)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}