package com.corn.cornstagram.join

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.databinding.ActivityFindNumBinding
import com.corn.cornstagram.fragment.FindNumInfoActivity

class FindNumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFindNumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFindNumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fulltext = "회원님의 연락처 정보가 주기적으로 동기화되어 서버에 저장됩니다. 연락처 정보를 삭제하려면 설정으로 이동하여 연결을 해제하세요. 더 알아보기"
        val spannableString = SpannableString(fulltext)

        val boldStart = fulltext.indexOf("더 알아보기")
        val boldEnd = boldStart + "더 알아보기".length
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, boldStart, boldEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@FindNumActivity, FindNumInfoActivity::class.java)
                startActivity(intent)
            }
        }
        spannableString.setSpan(clickableSpan, boldStart, boldEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.findNumMoreinfo.text = spannableString
        binding.findNumMoreinfo.movementMethod = android.text.method.LinkMovementMethod.getInstance()

        binding.findNumNextbtn.setOnClickListener {

        }

        binding.findNumSkipbtn.setOnClickListener {
            val intent = Intent(this, AddPhotoActivity::class.java)
            startActivity(intent)
        }
    }
}