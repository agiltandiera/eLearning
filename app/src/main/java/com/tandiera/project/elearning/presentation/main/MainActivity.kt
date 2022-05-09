package com.tandiera.project.elearning.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tandiera.project.elearning.R
import com.tandiera.project.elearning.databinding.ActivityMainBinding
import com.tandiera.project.elearning.presentation.user.UserActivity
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onAction()
    }

    private fun onAction() {
        binding.apply {
            ivAvatarMain.setOnClickListener {
                startActivity<UserActivity>()
            }
        }
    }
}