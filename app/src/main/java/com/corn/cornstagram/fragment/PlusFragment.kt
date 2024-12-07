package com.corn.cornstagram.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.corn.cornstagram.databinding.FragmentPlusBinding
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date

class PlusFragment : Fragment() {
    private var _binding: FragmentPlusBinding? = null
    private val binding get() = _binding!!
    private var PICK_IMAGE_FROM_ALBUM = 0
    private var storage: FirebaseStorage? = null
    private var photoUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storage = FirebaseStorage.getInstance()

        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        binding.frgPlusBtnUpload.setOnClickListener {
            contentUpload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM)
            if (resultCode == Activity.RESULT_OK) {
                photoUri = data?.data
                binding.frgPlusAddImage.setImageURI(photoUri)
            } else {
                return
            }
    }
    fun contentUpload() {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            Toast.makeText(requireContext(), getString(io.getstream.chat.android.ui.R.string.stream_ui_message_list_attachment_uploading), Toast.LENGTH_SHORT).show()
        }
    }
}