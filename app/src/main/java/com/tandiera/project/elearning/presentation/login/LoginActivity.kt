package com.tandiera.project.elearning.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tandiera.project.elearning.R
import com.tandiera.project.elearning.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}