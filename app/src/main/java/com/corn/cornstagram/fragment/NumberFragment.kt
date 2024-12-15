package com.corn.cornstagram.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.corn.cornstagram.MainActivity
import com.corn.cornstagram.databinding.FragmentNumberBinding
import com.corn.cornstagram.start.VerificationActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class NumberFragment : Fragment() {
    private var _binding: FragmentNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.frgNumNextbtn.setOnClickListener {
            val phonenum = binding.fragmentNumberText.text.toString().trim()
            signup(phonenum)
        }
    }

    private fun signup(phonenum: String) {
        if (phonenum.isEmpty()) {
            binding.fragmentNumberText.error = "전화번호를 입력하세요."
            return
        }

        if (!phonenum.matches(Regex("^\\d{11}\$"))) {
            binding.fragmentNumberText.error = "유효한 11자리 전화번호를 입력하세요."
            return
        }

        signInPhone(phonenum)
    }

    private fun signInPhone(phonenum: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phonenum)
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                moveMainPage(auth.currentUser)
                            } else {
                                task.exception?.let { exception ->
                                    Log.e("TAG", "전화번호 인증 실패", exception)
                                    Toast.makeText(requireContext(), "로그인 실패 : ${exception.localizedMessage}" ,
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("TAG", "전화번호 인증 실패", e)
                    Toast.makeText(requireContext(), "전화번호 인증 실패: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                    Log.d("TAG", "인증 코드 전송됨: $verificationId")

                    firestore.collection("users")
                        .whereEqualTo("phonenum", phonenum)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                val intent = Intent(requireContext(), VerificationActivity::class.java)
                                intent.putExtra("verificationId", verificationId)
                                intent.putExtra("phonenum", phonenum)
                                startActivity(intent)
                            } else {
                                moveMainPage(auth.currentUser)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("TAG", "전화번호 확인 실패", exception)
                            Toast.makeText(requireContext(), "전화번호 확인 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun moveMainPage (user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
