package com.corn.cornstagram.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.corn.cornstagram.databinding.FragmentNumberBinding
import com.corn.cornstagram.join.AddNameActivity
import com.google.firebase.auth.FirebaseAuth

class NumberFragment : Fragment() {
    private var _binding: FragmentNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

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

        val intent = Intent(requireContext(), AddNameActivity::class.java)
        intent.putExtra("phonenum", phonenum)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
