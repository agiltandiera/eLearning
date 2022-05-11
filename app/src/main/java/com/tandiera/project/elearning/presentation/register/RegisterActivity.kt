package com.tandiera.project.elearning.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tandiera.project.elearning.R
import com.tandiera.project.elearning.databinding.ActivityRegisterBinding
import com.tandiera.project.elearning.model.User
import com.tandiera.project.elearning.utils.hideSoftKeyboard
import com.tandiera.project.elearning.utils.showDialogError
import com.tandiera.project.elearning.utils.showDialogLoading
import com.tandiera.project.elearning.utils.showDialogSuccess
import org.jetbrains.anko.toast

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var userDatabase: DatabaseReference
    private lateinit var dialogLoading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init
        firebaseAuth = FirebaseAuth.getInstance()
        userDatabase = FirebaseDatabase.getInstance().getReference()
        dialogLoading = showDialogLoading(this)
        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnCloseRegister.setOnClickListener{
                finish()
            }

            btnRegister.setOnClickListener {
                val name = etNameRegister.text.toString().trim()
                val email = etEmailRegister.text.toString().trim()
                val pass = etPasswordRegister.text.toString().trim()
                val confirmPass = etConfirmPasswordRegister.text.toString().trim()

                if(checkValidation(name, email, pass, confirmPass)) {
                    hideSoftKeyboard(this@RegisterActivity, binding.root)
                    registerToServer(name, email, pass)
                }
            }
        }
    }

    private fun registerToServer(name: String, email: String, pass: String) {
        dialogLoading.show()
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                val uid = it.user?.uid
                val imageUrl = "https://ui-avatars.com/api/?background=218B5E&color=fff&size=100&rounded=true&name=$name"
                val user = User(
                    uidUser = uid,
                    nameUser = name,
                    emailUser = email,
                    avatarUser = imageUrl
                )
                userDatabase
                    .child(uid.toString())
                    .setValue(user)
                    .addOnSuccessListener {
                        dialogLoading.dismiss()
                        val dialogSuccess = showDialogSuccess(this, getString(R.string.success))
                        dialogSuccess.show()

                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                dialogSuccess.dismiss()
                                finishAffinity()
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

    private fun checkValidation(
        name: String,
        email: String,
        pass: String,
        confirmPass: String
    ): Boolean {
        binding.apply {
            when {
                name.isEmpty() -> {
                    etNameRegister.error = getString(R.string.filled_required)
                    etNameRegister.requestFocus()
                }
                email.isEmpty() -> {
                    etEmailRegister.error = getString(R.string.filled_required)
                    etEmailRegister.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmailRegister.error = getString(R.string.filled_required)
                    etEmailRegister.requestFocus()
                }
                pass.isEmpty() -> {
                    etEmailRegister.error = getString(R.string.filled_required)
                    etEmailRegister.requestFocus()
                }
                pass.length < 8 -> {
                    etPasswordRegister.error = getString(R.string.password_required_more_than_8)
                    etPasswordRegister.requestFocus()
                }
                confirmPass.isEmpty() -> {
                    etConfirmPasswordRegister.error = getString(R.string.filled_required)
                    etConfirmPasswordRegister.requestFocus()
                }
                confirmPass.length < 8 -> {
                    etConfirmPasswordRegister.error = getString(R.string.password_required_more_than_8)
                    etConfirmPasswordRegister.requestFocus()
                }
                pass != confirmPass -> {
                    etPasswordRegister.error = getString(R.string.pass_not_match)
                    etPasswordRegister.requestFocus()
                    etConfirmPasswordRegister.error = getString(R.string.pass_not_match)
                    etConfirmPasswordRegister.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }
}