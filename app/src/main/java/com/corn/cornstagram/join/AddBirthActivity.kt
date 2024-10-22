package com.corn.cornstagram.join

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityAddBirthBinding
import java.time.LocalDate

class AddBirthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBirthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBirthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addBirthText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.performClick()
            }
            v.clearFocus()
            true
        }

        val now = LocalDate.now()
        Log.d("시스템시간:", "$now")

        binding.addBirthText.setOnClickListener {
            DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_DARK,
                object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    binding.addBirthText.setText("${year}/${month + 1}/${dayOfMonth}")
                }
            }, now.year, now.monthValue - 1, now.dayOfMonth).show()
        }

        binding.addBirthNextbtn.setOnClickListener {
            val intent = Intent(this, AgreeActivity::class.java)
            startActivity(intent)
        }
    }
}