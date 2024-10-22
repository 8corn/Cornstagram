package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityAddAkaBinding

class AddAkaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAkaBinding
    private val existingAkaNames = listOf("김정권", "김철권")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAkaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addAkaName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val akaName = s.toString()

                if (existingAkaNames.contains(akaName)) {
                    binding.addAkaErrortxt.visibility = View.VISIBLE
                    binding.addAkaNextbtn.isEnabled = false
                } else {
                    binding.addAkaErrortxt.visibility = View.INVISIBLE
                    binding.addAkaNextbtn.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.addAkaNextbtn.setOnClickListener {
            val akaName = binding.addAkaName.text.toString()

            val intent = Intent(this, CheckAkaActivity::class.java)
            intent.putExtra("AKA_NAME", akaName)
            startActivity(intent)
        }
    }
}