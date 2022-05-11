package com.tandiera.project.elearning.presentation.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tandiera.project.elearning.adapter.PagesAdapter
import com.tandiera.project.elearning.databinding.ActivityContentBinding
import com.tandiera.project.elearning.model.Content
import com.tandiera.project.elearning.model.Material
import com.tandiera.project.elearning.model.Page
import com.tandiera.project.elearning.presentation.content.ContentActivity.Companion.EXTRA_POSITION
import com.tandiera.project.elearning.presentation.main.MainActivity
import com.tandiera.project.elearning.repository.Repository
import com.tandiera.project.elearning.utils.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ContentActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_MATERIAL = "extra_material"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var binding : ActivityContentBinding
    private lateinit var pagesAdapter: PagesAdapter
    private lateinit var contentDatabase : DatabaseReference
    private var currentPosition = 0
    private var materialPosition = 0

    private var listenerContent = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            if(snapshot.value != null) {
                showData()
                val json = Gson().toJson(snapshot.value)
                val content = Gson().fromJson(json, Content::class.java)
                pagesAdapter.pages = content?.pages as MutableList<Page>
            } else {
                showEmptyData()
            }
        }

        private fun showEmptyData() {
            binding.apply {
                ivEmptyData.visible()
                vpContent.gone()
            }
        }

        private fun showData() {
            binding.apply {
                ivEmptyData.invisible()
                vpContent.visible()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            showDialogError(this@ContentActivity, error.message)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Init
        pagesAdapter = PagesAdapter(this)
        contentDatabase = FirebaseDatabase.getInstance().getReference("contents")

        getDataIntent()
        onAction()
        viewPagerCurrentPosition()
    }

    private fun viewPagerCurrentPosition() {
        binding.vpContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val totalIndex = pagesAdapter.count
                currentPosition = position
                val textIndex = "${currentPosition + 1} / $totalIndex"
                binding.tvIndexContent.text = textIndex

                if(currentPosition == 0) {
                    binding.btnPrevContent.invisible()
                    binding.btnPrevContent.disabled()
                } else {
                    binding.btnPrevContent.visible()
                    binding.btnPrevContent.enabled()
                }
            }

            override fun onPageSelected(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onPageScrollStateChanged(state: Int) {
                TODO("Not yet implemented")
            }
        })
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
//        val content = material.idMaterial?.let { Repository.getContents(this)?.get(it) }
        contentDatabase
            .child(material.idMaterial.toString())
            .addValueEventListener(listenerContent)

        binding.vpContent.adapter = pagesAdapter
        binding.vpContent.setPagingEnabled(false)

        //Init untuk tampilan awal  index
        val textIndex = "${currentPosition + 1} / ${pagesAdapter.count}"
        binding.tvIndexContent.text = textIndex
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
                if (currentPosition < pagesAdapter.count - 1){
                    binding.vpContent.currentItem += 1
                }else{
                    startActivity<MainActivity>(
                        MainActivity.EXTRA_POSITION to materialPosition + 1
                    )
                    finish()
                }
            }
            
            btnPrevContent.setOnClickListener {
                binding.vpContent.currentItem -= 1
            }

            swipeContent.setOnRefreshListener {
                getDataIntent()
            }
        }
    }
}