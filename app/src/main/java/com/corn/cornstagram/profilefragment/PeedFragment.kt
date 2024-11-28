package com.corn.cornstagram.profilefragment

import ProfilesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.corn.cornstagram.R
import com.corn.cornstagram.adapter.Profiles
import com.corn.cornstagram.databinding.FragmentPeedBinding

class PeedFragment : Fragment() {
    private var _binding: FragmentPeedBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileList = arrayListOf(
            Profiles(R.drawable.gilmon, "gilmon", "Gilmon", 2022),
            Profiles(R.drawable.megalizamon, "megalizamon", "megalizamon", 2024),
            Profiles(R.drawable.impmon, "impmon", "impmon", 2023),
            Profiles(R.drawable.agumon, "agumon", "agumon", 2024),
        )

        binding.peedRvProfile.layoutManager = GridLayoutManager(context, 3)
        binding.peedRvProfile.adapter = ProfilesAdapter(profileList)
    }

}