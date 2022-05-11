package com.tandiera.project.elearning.presentation.login

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.tandiera.project.elearning.R
import com.tandiera.project.elearning.databinding.ActivityLoginBinding
import com.tandiera.project.elearning.presentation.forgotpassword.ForgotPassswordActivity
import com.tandiera.project.elearning.presentation.main.MainActivity
import com.tandiera.project.elearning.presentation.register.RegisterActivity
import com.tandiera.project.elearning.utils.hideSoftKeyboard
import com.tandiera.project.elearning.utils.showDialogError
import com.tandiera.project.elearning.utils.showDialogLoading
import org.jetbrains.anko.startActivity
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var dialogloading : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init
        firebaseAuth = FirebaseAuth.getInstance()
//        dialogloading = showDialogLoading(this)
        onAction()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            startActivity<MainActivity>()
            finishAffinity()
        }
    }

    private fun onAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmailLogin.text.toString().trim()
                val pass = etPassLogin.text.toString().trim()

                if(checkValidation(email, pass)) {
                    hideSoftKeyboard(this@LoginActivity, binding.root)
                    loginToServer(email, pass)
                }
            }

            btnRegister.setOnClickListener {
                startActivity<RegisterActivity>()
            }

            btnForgotPassLogin.setOnClickListener{
                startActivity<ForgotPassswordActivity>()
            }
        }
    }

    private fun loginToServer(email: String, pass: String) {
        dialogloading.show()
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                dialogloading.dismiss()
                startActivity<MainActivity>()
                finishAffinity()
            }
            .addOnFailureListener {
                dialogloading.dismiss()
                showDialogError(this, it.message.toString())
            }
    }

    private fun checkValidation(email: String, pass: String): Boolean {
        binding.apply {
            when {
                email.isEmpty() -> {
                    etEmailLogin.error = getString(R.string.filled_required)
                    etEmailLogin.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmailLogin.error = getString(R.string.email_is_not_valid)
                    etEmailLogin.requestFocus()
                }
                pass.isEmpty() -> {
                    etPassLogin.error = getString(R.string.filled_required)
                    etPassLogin.requestFocus()
                }
                pass.length < 8 -> {
                    etPassLogin.error = getString(R.string.password_required_more_than_8)
                }
                else -> return true
            }
        }
        return false
    }
}