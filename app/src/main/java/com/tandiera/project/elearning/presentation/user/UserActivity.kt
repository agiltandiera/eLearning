package com.tandiera.project.elearning.presentation.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import com.google.firebase.auth.FirebaseAuth
import com.tandiera.project.elearning.databinding.ActivityUserBinding
import com.tandiera.project.elearning.presentation.changepassword.ChangePasswordActivity
import com.tandiera.project.elearning.presentation.login.LoginActivity
import org.jetbrains.anko.startActivity

class UserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserBinding
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init
        firebaseAuth = FirebaseAuth.getInstance()
        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnCloseUser.setOnClickListener {
                finish()
            }

            btnChangeLanguageUser.setOnClickListener {
                startActivity(Intent(ACTION_LOCALE_SETTINGS))
            }

            btnChangePassUser.setOnClickListener {
                startActivity<ChangePasswordActivity>()
            }

            btnLogoutUser.setOnClickListener {
                firebaseAuth.signOut()
                startActivity<LoginActivity>()
                finishAffinity()
            }
        }
    }
}