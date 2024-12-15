package com.corn.cornstagram.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.corn.cornstagram.MainActivity
import com.corn.cornstagram.PlusPhotoActivity
import com.corn.cornstagram.R
import com.corn.cornstagram.adapter.ContentDTO
import com.corn.cornstagram.databinding.FragmentProfileBinding
import com.corn.cornstagram.others.ProfileEditActivity
import com.corn.cornstagram.profilefragment.PeedFragment
import com.corn.cornstagram.profilefragment.TagPeedFragment
import com.corn.cornstagram.start.LoginActivity
import com.corn.cornstagram.start.StartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private var _binding:FragmentProfileBinding ?= null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var uid: String? = null
    private var currentUserUid: String? = null
    private var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
    companion object {
        var PICK_PROFILE_FROM_ALBUM = 10
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        currentUserUid = auth.currentUser?.uid

        uid = arguments?.getString("destinationUid")

        if (uid == currentUserUid) {
            binding.profileProfileFollowBtn.text = getString(R.string.signout)
            binding.profileProfileFollowBtn.setOnClickListener {
                auth.signOut()
                activity?.finish()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        } else {
            binding.profileProfileFollowBtn.text = getString(R.string.follow)
        }

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

        binding.profileSetting.setOnClickListener { showPopupMenu(it) }

        binding.profilePlusBtn.setOnClickListener {
            val intent = Intent(requireContext(), PlusPhotoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.profile_fragment, fragment)
            .commit()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.profile_setting)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.setting_item -> {
                    val intent = Intent(requireContext(), FindNumInfoActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.logout_item -> {
                    auth.signOut()
                    val intent = Intent(requireContext(), StartActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                } else -> false
            }
        }
        popupMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}