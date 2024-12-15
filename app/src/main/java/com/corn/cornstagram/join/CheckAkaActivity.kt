package com.corn.cornstagram.join

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityCheckAkaBinding
import com.corn.cornstagram.start.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CheckAkaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckAkaBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckAkaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val phonenum = intent.getStringExtra("phonenum")
        val pwd = intent.getStringExtra("password")
        val birth = intent.getStringExtra("birth")
        val akaName = intent.getStringExtra("akaname")

        binding.checkAkaTxt.text = "$akaName 님으로\n가입하시겠어요?"

        binding.checkAkaLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.checkAkaNext.setOnClickListener {
            saveDataToFirebase(email, phonenum, pwd, name, birth, akaName)
        }
    }

    private fun saveDataToFirebase(email: String?, phonenum: String?, pwd: String?, name: String?, birth: String?, akaname: String?) {
        val uid = System.currentTimeMillis().toString()
        val userData = hashMapOf(
            "uid" to uid,
            "email" to email,
            "phonenum" to phonenum,
            "password" to pwd,
            "name" to name,
            "birth" to birth,
            "akaname" to akaname,
        )
        firestore.collection("users")
            .document(uid)
            .set(userData)
            .addOnSuccessListener {
                val intent = Intent(this, FindNumActivity::class.java)
                Log.d("TAG", "회원 정보 저장 완료")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "회원 정보 저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "회원 정보 저장 실패")
            }
    }
}