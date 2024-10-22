package com.corn.cornstagram.fragment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.corn.cornstagram.R
import com.corn.cornstagram.databinding.ActivityFindNumInfoBinding

class FindNumInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFindNumInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFindNumInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}