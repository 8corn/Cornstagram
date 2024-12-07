package com.corn.cornstagram.profilefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.corn.cornstagram.databinding.FragmentTagPeedBinding

class TagPeedFragment : Fragment() {
    private var _binding:FragmentTagPeedBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTagPeedBinding.inflate(inflater, container, false)
        return binding.root
    }
}