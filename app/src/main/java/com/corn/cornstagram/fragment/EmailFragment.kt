package com.corn.cornstagram.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.corn.cornstagram.databinding.FragmentEmailBinding
import com.corn.cornstagram.join.AddNameActivity
import com.google.firebase.auth.FirebaseAuth

class EmailFragment : Fragment() {
    private var _binding: FragmentEmailBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.frgEmailNextbtn.setOnClickListener {
            val email = binding.fragmentEmailText.text.toString().trim()
            signup(email)
        }
    }

    private fun signup(email: String) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.fragmentEmailText.error = "유효한 이메일 주소를 입력하세요."
            return
        }

        val intent = Intent(requireContext(), AddNameActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
