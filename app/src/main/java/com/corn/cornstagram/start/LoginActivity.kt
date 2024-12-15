package com.corn.cornstagram.start

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.MainActivity
import com.corn.cornstagram.R
import com.corn.cornstagram.VerificationActivity
import com.corn.cornstagram.databinding.ActivityLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var callbackManager: CallbackManager

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e("TAG", "Google Login 실패", e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginLoginBtn.setOnClickListener {
            val emailId = binding.emailLogin.text.toString().trim()

            if (emailId.isEmpty()) {
                phoneSignin()
            } else if (emailId.isNotEmpty()) {
                emailSignin()
            } else {
                Toast.makeText(this, "로그인 방법을 선택하여 주십시오.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginGoogleIcon.setOnClickListener {
            googleLogin()
        }

        binding.loginFacebookIcon.setOnClickListener {
            facebookLogin()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)

        printHashKey()

        callbackManager = CallbackManager.Factory.create()
    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth.currentUser)
    }

    private fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)

            val signingInfo = info.signingInfo
            val signatures = signingInfo?.apkContentsSigners ?: arrayOf()
            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.i("TAG", "Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Tag", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("Tag", "printHashKey()", e)
        }
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun handleFacebookAccessToken(token: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(task.result?.user)
                } else {
                    Toast.makeText(this, "Facebook 로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun facebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("public_profile", "email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    Log.d("TAG", "Facebook 로그인 취소")
                }

                override fun onError(error: FacebookException) {
                    Log.e("TAG", "Facebook 로그인 에러: ${error.message}")
                }
            })
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(task.result?.user)
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun emailSignin() {
        val emailId = binding.emailLogin.text.toString().trim()
        val password = binding.pwdLogin.text.toString().trim()

        if (emailId.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (isEmail(emailId)) {
            signInEmail(emailId, password)
        } else {
            Toast.makeText(this, "유효하지 않은 이메일입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun phoneSignin() {
        val phonenum = binding.phonenumLogin.text.toString().trim()

        if (phonenum.isEmpty()) {
            Toast.makeText(this, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (isPhoneNum(phonenum)) {
            signInPhone(phonenum)
        } else {
            Toast.makeText(this, "유효하지 않은 전화번호입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isEmail(emailId: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return emailId.matches(emailPattern.toRegex())
    }

    private fun isPhoneNum(phonenum: String): Boolean {
        val phonePattern = "^[+]?[0-9]{10,15}\$"
        return phonenum.matches(phonePattern.toRegex())
    }

    private fun signInEmail(loginId: String, password: String) {
        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(loginId, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(task.result?.user)
                } else {
                    task.exception?.let { exception ->
                        Log.e("TAG", "이메일 로그인 오류", exception)
                        Toast.makeText(this, "로그인 실패 : ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun signInPhone(phonenum: String) {
        if (phonenum.isEmpty()) {
            Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phonenum)
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                moveMainPage(task.result?.user)
                            } else {
                                task.exception?.let { exception ->
                                    Log.e("TAG", "전화번호 인증 실패", exception)
                                    Toast.makeText(this@LoginActivity, "로그인 실패 : ${exception.localizedMessage}" ,Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("TAG", "전화번호 인증 실패", e)
                    Toast.makeText(this@LoginActivity, "전화번호 인증 실패: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    Log.d("TAG", "인증 코드 전송됨: $verificationId")
                    val intent = Intent(this@LoginActivity, VerificationActivity::class.java)
                    intent.putExtra("verificationId", verificationId)
                    startActivity(intent)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun moveMainPage (user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}