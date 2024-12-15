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
        if (email.isNullOrEmpty() || pwd.isNullOrEmpty()) {
            Log.d("TAG", "이메일과 비밀번호가 잘못됨")
            Toast.makeText(this, "이메일과 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUid = task.result?.user?.uid ?: return@addOnCompleteListener

                    val userData = hashMapOf(
                        "uid" to userUid,
                        "email" to email,
                        "phonenum" to phonenum,
                        "password" to pwd,
                        "name" to name,
                        "birth" to birth,
                        "akaname" to akaname,
                    )

                    firestore.collection("users").document(userUid)
                            .set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, FindNumActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                Log.e("TAG", "Firestore에 데이터 저장 실패", e)
                                Toast.makeText(this, "회원가입 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                    task.exception?.let { exception ->
                        Log.e("TAG", "회원가입 실패", exception)
                        Toast.makeText(this, "회원가입 실패: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}