package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.R
import com.corn.cornstagram.databinding.ActivityAgreeBinding
import java.time.LocalDate

class AgreeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgreeBinding
    private var isAllChecked = false
    private var isDataChecked = false
    private var isGpsChecked = false
    private var isUseChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAgreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val phonenum = intent.getStringExtra("phonenum")
        val pwd = intent.getStringExtra("password")
        val birth = intent.getStringExtra("birth")

        fun handleCheckboxClick(imageView: ImageView, isChecked: Boolean): Boolean {
            imageView.setImageResource(if (isChecked) R.drawable.checkbtn_circle_icon else R.drawable.checkbtn_circle)
            return !isChecked
        }

        binding.agreeDataCheck.setOnClickListener {
            isDataChecked = handleCheckboxClick(binding.agreeDataCheck, isDataChecked)
        }

        binding.agreeGpsCheck.setOnClickListener {
            isGpsChecked = handleCheckboxClick(binding.agreeGpsCheck, isGpsChecked)
        }

        binding.agreeUseCheck.setOnClickListener {
            isUseChecked = handleCheckboxClick(binding.agreeUseCheck, isUseChecked)
        }

        binding.agreeAllCheck.setOnClickListener {
            isAllChecked = !isAllChecked

            isDataChecked = isAllChecked
            isUseChecked = isAllChecked
            isGpsChecked = isAllChecked

            handleCheckboxClick(binding.agreeAllCheck, !isAllChecked)
            handleCheckboxClick(binding.agreeGpsCheck, !isAllChecked)
            handleCheckboxClick(binding.agreeDataCheck, !isAllChecked)
            handleCheckboxClick(binding.agreeUseCheck, !isAllChecked)
        }

        binding.agreeNextBtn.setOnClickListener {
            if (!isGpsChecked && !isUseChecked && !isDataChecked) {
                val intent = Intent(this, AddAkaActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("phonenum", phonenum)
                intent.putExtra("password", pwd)
                intent.putExtra("name", name)
                intent.putExtra("birth", birth)
                startActivity(intent)
            } else if (!isAllChecked){
                val intent = Intent(this, AddAkaActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("phonenum", phonenum)
                intent.putExtra("password", pwd)
                intent.putExtra("name", name)
                intent.putExtra("birth", birth)
                startActivity(intent)
            } else {
                Toast.makeText(this, "모든 약관에 동의해야합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}