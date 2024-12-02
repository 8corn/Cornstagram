package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityAddNameBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNameBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        val email = intent.getStringExtra("email")
        val phonenum = intent.getStringExtra("phonenum")

        binding.addNameNextbtn.setOnClickListener {
            val name = binding.addNameText.text.toString().trim()

            if (name.isEmpty()) {
                binding.addNameText.error = "이름을 입력하세요."
                return@setOnClickListener
            }

            saveNameToFirestore(name)

            val intent = Intent(this, AddPwdActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("phonenum", phonenum)
            startActivity(intent)
        }
    }

    private fun saveNameToFirestore(name: String) {
        val uid = System.currentTimeMillis().toString()
        val userData = hashMapOf("uid" to uid, "name" to name)

        firestore.collection("users")
            .document(uid)
            .set(userData)
            .addOnFailureListener { e ->
                Toast.makeText(this, "이름을 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
    }
}