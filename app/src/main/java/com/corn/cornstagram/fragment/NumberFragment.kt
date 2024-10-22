package com.corn.cornstagram.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.corn.cornstagram.R
import com.corn.cornstagram.databinding.ActivityNumberFragmentBinding
import com.corn.cornstagram.join.AddNameActivity

class NumberFragment : Fragment() {
    private var _binding: ActivityNumberFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityNumberFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countryCodeNumber.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.fragmentNumberText.setText("${binding.countryCodeNumber.selectedItem} ")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.fragmentNumberText.inputType = android.text.InputType.TYPE_CLASS_PHONE

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.country_codes,
            R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.countryCodeNumber.adapter = adapter

        binding.frgNumNextbtn.setOnClickListener {
            val intent = Intent(requireContext(), AddNameActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}