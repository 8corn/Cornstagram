package com.corn.cornstagram.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.corn.cornstagram.databinding.ActivityEmailFragmentBinding
import com.corn.cornstagram.join.AddNameActivity

class EmailFragment : Fragment() {
    private var _binding: ActivityEmailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityEmailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.frgEmailNextbtn.setOnClickListener {
            val intent = Intent(requireContext(), AddNameActivity::class.java)
            startActivity(intent)
        }
    }
}