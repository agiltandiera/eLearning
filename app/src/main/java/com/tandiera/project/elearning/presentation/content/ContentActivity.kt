package com.tandiera.project.elearning.presentation.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.tandiera.project.elearning.adapter.PagesAdapter
import com.tandiera.project.elearning.databinding.ActivityContentBinding
import com.tandiera.project.elearning.model.Material
import com.tandiera.project.elearning.model.Page
import com.tandiera.project.elearning.repository.Repository
import org.jetbrains.anko.toast

class ContentActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MATERIAL = "extra_material"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var binding : ActivityContentBinding
    private lateinit var pagesAdapter: PagesAdapter
    private var currentPosition = 0
    private var materialPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Init
        pagesAdapter = PagesAdapter(this)
        getDataIntent()
        onAction()
    }

    private fun getDataIntent() {
        if(intent != null) {
            materialPosition = intent.getIntExtra(EXTRA_POSITION, 0)
            val material = intent.getParcelableExtra<Material>(EXTRA_MATERIAL)
            binding.tvTitleContent.text = material?.titleMaterial
            material?.let { getDataContent(material) }
        }
    }

    private fun getDataContent(material: Material) {
        showLoading()
        val content = material.idMaterial?.let { Repository.getContents(this)?.get(it) }

        Handler(Looper.getMainLooper())
            .postDelayed( {
                hideLoading()
                pagesAdapter.pages = content?.pages as MutableList<Page>
                binding.vpContent.adapter = pagesAdapter
                binding.vpContent.setPagingEnabled(false)
            }, 1200)
    }

    private fun showLoading() {
        binding.swipeContent.isRefreshing = true
    }

    private fun hideLoading() {
        binding.swipeContent.isRefreshing = false
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

            swipeContent.setOnRefreshListener {
                getDataIntent()
            }
        }
    }
}