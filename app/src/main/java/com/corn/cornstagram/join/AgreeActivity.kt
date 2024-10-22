package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.corn.cornstagram.R
import com.corn.cornstagram.databinding.ActivityAgreeBinding
import com.google.protobuf.Internal.BooleanList

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
            val intent = Intent(this, AddAkaActivity::class.java)
            startActivity(intent)
        }
    }
}