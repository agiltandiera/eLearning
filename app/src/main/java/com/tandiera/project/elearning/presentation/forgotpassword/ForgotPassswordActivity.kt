package com.tandiera.project.elearning.presentation.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tandiera.project.elearning.databinding.ActivityForgotPassswordBinding

class ForgotPassswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityForgotPassswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}