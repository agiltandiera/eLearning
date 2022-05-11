package com.tandiera.project.elearning.presentation.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.tandiera.project.elearning.R
import com.tandiera.project.elearning.databinding.ActivityForgotPassswordBinding
import com.tandiera.project.elearning.utils.hideSoftKeyboard
import com.tandiera.project.elearning.utils.showDialogError
import com.tandiera.project.elearning.utils.showDialogLoading
import com.tandiera.project.elearning.utils.showDialogSuccess
import org.jetbrains.anko.toast
import kotlin.coroutines.suspendCoroutine

class ForgotPassswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityForgotPassswordBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var dialogLoading : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init
        firebaseAuth = FirebaseAuth.getInstance()
//        dialogLoading = showDialogLoading(this)
        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnCloseForgotPassword.setOnClickListener {
                finish()
            }

            btnForgotPassword.setOnClickListener {
                val email = etEmailForgotPassword.text.toString().trim()

                if(checkValidation(email)) {
                    hideSoftKeyboard(this@ForgotPassswordActivity, binding.root)
                    forgotPasswordToServer(email)
                }
            }
        }
    }

    private fun forgotPasswordToServer(email: String) {
        dialogLoading.show()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                dialogLoading.dismiss()
                val dialogSuccess = showDialogSuccess(this, getString(R.string.success))
                dialogSuccess.show()

                Handler(Looper.getMainLooper())
                    .postDelayed( {
                        dialogSuccess.dismiss()
                        finishAffinity()
                    }, 1500)
            }
            .addOnFailureListener {
                dialogLoading.dismiss()
                showDialogError(this, it.message.toString())
            }
    }

    private fun checkValidation(email: String): Boolean {
        binding.apply {
            when {
                email.isEmpty() -> {
                    etEmailForgotPassword.error = getString(R.string.filled_required)
                    etEmailForgotPassword.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmailForgotPassword.error = getString(R.string.filled_required)
                    etEmailForgotPassword.requestFocus()
                }
                else -> return false
            }
        }
        return false
    }
}