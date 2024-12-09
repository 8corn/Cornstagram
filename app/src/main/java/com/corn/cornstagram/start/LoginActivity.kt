package com.corn.cornstagram.start

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.GET_SIGNATURES
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corn.cornstagram.MainActivity
import com.corn.cornstagram.R
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
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = this.javaClass.simpleName
    private var GOOGLE_LOGIN_CODE = 9001
    private var googleSignInClient: GoogleSignInClient? = null

    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginLoginBtn.setOnClickListener {
            signinAndSignup()
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
            val info: PackageInfo = packageManager.getPackageInfo(packageName, GET_SIGNATURES)

            val signatures = info.signingInfo?.apkContentsSigners
            if (signatures != null) {
                for (signature in signatures) {
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val hashKey = String(Base64.encode(md.digest(), Base64.DEFAULT))
                    Log.i("Tag", "printHashKey() Hash Key: $hashKey")
                }
            } else {
                Log.e("Tag", "No signatures found!")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Tag", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("Tag", "printHashKey()", e)
        }
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient?.signInIntent
        if (signInIntent != null) {
            startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
        } else {
            Log.e(TAG, "GoogleSignInClient is null")
        }
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
                    Log.d(TAG, "Facebook 로그인 취소")
                }

                override fun onError(error: FacebookException) {
                    Log.e(TAG, "Facebook 로그인 에러: ${error.message}")
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_LOGIN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e(TAG, "Google 로그인 실패", e)
            }
        }
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

    private fun signinAndSignup() {
        if (binding.emailLogin.text.toString().isEmpty() || binding.pwdLogin.text.toString().isEmpty()) {
            Toast.makeText(this, "아이디와 비밀번호를 입력해주시오.", Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(binding.emailLogin.text.toString(), binding.pwdLogin.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(task.result?.user)
                } else {
                    task.exception?.let { exception ->
                        Log.e(TAG,"Authentication error", exception)
                        Toast.makeText(this, "로그인 오류: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun moveMainPage (user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}