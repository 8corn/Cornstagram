package com.corn.cornstagram

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.adapter.ContentDTO
import com.corn.cornstagram.databinding.ActivityPlusPhotoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date

class PlusPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlusPhotoBinding
    private var PICK_IMAGE_FROM_ALBUM = 0
    private var storage: FirebaseStorage? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlusPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        binding.plusPhotoBtnUpload.setOnClickListener {
            contentUpload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM)
            if (resultCode == Activity.RESULT_OK) {
                photoUri = data?.data
                binding.plusPhotoAddImage.setImageURI(photoUri)
            } else {
                return
            }
    }

    private fun contentUpload() {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(photoUri!!)
            ?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception("파일을 업로드하는 동안 오류가 없었습니다.")
                }
                storageRef.downloadUrl
            }
            ?.addOnSuccessListener { uri ->
                val contentDTO = ContentDTO(
                    imageUrl = uri.toString(),
                    uid = auth.currentUser?.uid,
                    userId = auth.currentUser?.email,
                    explain = binding.plusPhotoEdit.text.toString(),
                    timestamp = System.currentTimeMillis()
                )

                firestore.collection("images").document().set(contentDTO)

                setResult(Activity.RESULT_OK)
                finish()
            }
            ?.addOnFailureListener { e ->
                Toast.makeText(this, "업로드 실패 : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}