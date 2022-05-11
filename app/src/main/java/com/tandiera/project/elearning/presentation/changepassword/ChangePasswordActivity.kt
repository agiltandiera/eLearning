package com.tandiera.project.elearning.presentation.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tandiera.project.elearning.R
import com.tandiera.project.elearning.databinding.ActivityChangePasswordBinding
import com.tandiera.project.elearning.utils.hideSoftKeyboard
import com.tandiera.project.elearning.utils.showDialogError
import com.tandiera.project.elearning.utils.showDialogLoading
import com.tandiera.project.elearning.utils.showDialogSuccess
import org.jetbrains.anko.toast

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChangePasswordBinding
    private lateinit var dialogLoading : AlertDialog
    private var currentUser : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init
        currentUser = FirebaseAuth.getInstance().currentUser
        dialogLoading = showDialogLoading(this)
        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnChangePass.setOnClickListener {
                val oldPass = etOldPassChangePass.text.toString().trim()
                val newPass = etNewPassChangePass.text.toString().trim()
                val confirmPass = etConfirmPassChangePass.text.toString().trim()

                if(checkValidation(oldPass, newPass, confirmPass)) {
                    hideSoftKeyboard(this@ChangePasswordActivity, binding.root)
                    changePasswordToServer(oldPass, newPass)
                }
            }

            btnCloseChangePass.setOnClickListener {
                finish()
            }
        }
    }

    private fun changePasswordToServer(oldPass: String, newPass: String) {
        dialogLoading.show()
        currentUser?.let { mCurrentUser ->
            val credential = EmailAuthProvider.getCredential(mCurrentUser.email.toString(), oldPass)

            mCurrentUser.reauthenticate(credential)
                .addOnSuccessListener {
                    mCurrentUser.updatePassword(newPass)
                        .addOnSuccessListener {
                            dialogLoading.dismiss()
                            val dialogSuccess = showDialogSuccess(this, getString(R.string.success))
                            dialogSuccess.show()

                            Handler(Looper.getMainLooper())
                                .postDelayed({
                                    dialogSuccess.dismiss()
                                    finish()
                                }, 1500)
                        }
                        .addOnFailureListener {
                            dialogLoading.dismiss()
                            showDialogError(this, it.message.toString())
                        }
                }
                .addOnFailureListener {
                    dialogLoading.dismiss()
                    showDialogError(this, it.message.toString())
                }
        }
    }

    private fun checkValidation(oldPass: String, newPass: String, confirmPass: String): Boolean {
        binding.apply {
            when {
                oldPass.isEmpty() -> {
                    etOldPassChangePass.error = getString(R.string.filled_required)
                    etOldPassChangePass.requestFocus()
                }
                oldPass.length < 8 -> {
                    etOldPassChangePass.error = getString(R.string.password_required_more_than_8)
                    etOldPassChangePass.requestFocus()
                }
                newPass.isEmpty() -> {
                    etNewPassChangePass.error = getString(R.string.filled_required)
                    etNewPassChangePass.requestFocus()
                }
                newPass.length < 8 -> {
                    etNewPassChangePass.error = getString(R.string.password_required_more_than_8)
                    etNewPassChangePass.requestFocus()
                }
                confirmPass.isEmpty() -> {
                    etConfirmPassChangePass.error = getString(R.string.filled_required)
                    etConfirmPassChangePass.requestFocus()
                }
                confirmPass.length < 8 -> {
                    etConfirmPassChangePass.error = getString(R.string.password_required_more_than_8)
                    etConfirmPassChangePass.requestFocus()
                }
                newPass != confirmPass -> {
                    etNewPassChangePass.error = getString(R.string.pass_not_match)
                    etNewPassChangePass.requestFocus()
                    etConfirmPassChangePass.error = getString(R.string.pass_not_match)
                    etConfirmPassChangePass.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }
}