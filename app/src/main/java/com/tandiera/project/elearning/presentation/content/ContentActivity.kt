package com.tandiera.project.elearning.presentation.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tandiera.project.elearning.databinding.ActivityContentBinding
import org.jetbrains.anko.toast

class ContentActivity : AppCompatActivity() {

    private lateinit var binding : ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnCloseContent.setOnClickListener {
                finish()
            }

            btnNextContent.setOnClickListener {
                toast("Next")
            }
            
            btnPrevContent.setOnClickListener {
                toast("Prev")
            }
        }
    }
}