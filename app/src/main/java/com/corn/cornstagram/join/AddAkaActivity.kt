package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityAddAkaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddAkaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAkaBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAkaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val phonenum = intent.getStringExtra("phonenum")
        val pwd = intent.getStringExtra("password")
        val birth = intent.getStringExtra("birth")

        binding.addAkaName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val akaName = s.toString()

                val allowedPattern = Regex("^[a-zA-Z0-9!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?~]+$")
                if (!akaName.matches(allowedPattern)) {
                    binding.addAkaErrortxt.text = "영어, 숫자, 특수문자만 입력 가능합니다."
                    binding.addAkaErrortxt.visibility = View.VISIBLE
                    binding.addAkaErrortxt.isEnabled = false
                } else {
                    binding.addAkaErrortxt.visibility = View.INVISIBLE

                    checkAkaInFirebase(akaName)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.addAkaNextbtn.setOnClickListener {
            val akaName = binding.addAkaName.text.toString().trim()

            val intent = Intent(this, CheckAkaActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("phonenum", phonenum)
            intent.putExtra("password", pwd)
            intent.putExtra("name", name)
            intent.putExtra("birth", birth)
            intent.putExtra("akaname", akaName)
            startActivity(intent)
        }
    }

    private fun checkAkaInFirebase(akaname: String) {
        firestore.collection("users")
            .whereEqualTo("akaname", akaname)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    binding.addAkaErrortxt.visibility = View.VISIBLE
                    binding.addAkaErrortxt.isEnabled = false
                } else {
                    binding.addAkaErrortxt.visibility = View.INVISIBLE
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "에러 : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}