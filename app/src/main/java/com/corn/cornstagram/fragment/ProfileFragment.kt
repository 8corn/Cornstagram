package com.corn.cornstagram.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.corn.cornstagram.R
import com.corn.cornstagram.databinding.FragmentProfileBinding
import com.corn.cornstagram.others.ProfileEditActivity
import com.corn.cornstagram.profilefragment.PeedFragment
import com.corn.cornstagram.profilefragment.TagPeedFragment

class ProfileFragment : Fragment() {
    private var _binding:FragmentProfileBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileProfileEditBtn.setOnClickListener {
            val intent = Intent(requireContext(), ProfileEditActivity::class.java)
            startActivity(intent)
        }

        replaceFragment(PeedFragment())

        binding.profilePeedBtn.setOnClickListener {
            replaceFragment(PeedFragment())
        }

        binding.profilePeedTagbtn.setOnClickListener {
            replaceFragment(TagPeedFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.profile_fragment, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}