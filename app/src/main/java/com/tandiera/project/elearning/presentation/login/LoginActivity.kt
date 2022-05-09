package com.tandiera.project.elearning.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tandiera.project.elearning.R
import com.tandiera.project.elearning.databinding.ActivityLoginBinding
import com.tandiera.project.elearning.presentation.forgotpassword.ForgotPassswordActivity
import com.tandiera.project.elearning.presentation.main.MainActivity
import com.tandiera.project.elearning.presentation.register.RegisterActivity
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnLogin.setOnClickListener{
                startActivity<MainActivity>()
            }

            btnRegister.setOnClickListener {
                startActivity<RegisterActivity>()
            }

            btnForgotPassLogin.setOnClickListener{
                startActivity<ForgotPassswordActivity>()
            }
        }
    }
}