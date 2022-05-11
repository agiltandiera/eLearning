package com.tandiera.project.elearning.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tandiera.project.elearning.R
import com.tandiera.project.elearning.adapter.MaterialsAdapter
import com.tandiera.project.elearning.databinding.ActivityMainBinding
import com.tandiera.project.elearning.model.Material
import com.tandiera.project.elearning.model.User
import com.tandiera.project.elearning.presentation.content.ContentActivity
import com.tandiera.project.elearning.presentation.user.UserActivity
import com.tandiera.project.elearning.repository.Repository
import com.tandiera.project.elearning.utils.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var binding : ActivityMainBinding
    private lateinit var materialsAdapter: MaterialsAdapter
    private lateinit var userDatasbase : DatabaseReference
    private lateinit var materialDatabase : DatabaseReference
    private var currentUser : FirebaseUser? = null

    private var listenerUser = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            val user = snapshot.getValue(User::class.java)
            user.let {
                binding.apply {
                    tvNameUserMain.text = it?.nameUser

                    Glide.with(this@MainActivity)
                        .load(it?.avatarUser)
                        .placeholder(android.R.color.darker_gray)
                        .into(ivAvatarMain)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("MainActivity", "[onCancelled] - ${error.message}")
            showDialogError(this@MainActivity, error.message)
        }
    }

    private var listenerMaterials = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            if(snapshot.value != null) {
                showData()
                val json = Gson().toJson(snapshot.value)
                val type = object : TypeToken<MutableList<Material>>() {}.type
                val materials = Gson().fromJson<MutableList<Material>>(json, type)

                materials?.let { materialsAdapter.materials = it }
            } else {
                showEmptyData()
            }
        }

        private fun showEmptyData() {
            binding.apply {
                ivEmptyData.visible()
                etSearchMain.disabled()
                rvMaterialsMain.gone()
            }
        }

        private fun showData() {
            binding.apply {
                ivEmptyData.invisible()
                etSearchMain.enabled()
                rvMaterialsMain.visible()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("MainActivity", "[onCancelled] - ${error.message}")
            showDialogError(this@MainActivity, error.message)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Init
        materialsAdapter = MaterialsAdapter()
        userDatasbase = FirebaseDatabase.getInstance().getReference("users")
        materialDatabase = FirebaseDatabase.getInstance().getReference("materials")
        currentUser = FirebaseAuth.getInstance().currentUser

        getDataFirebase()
//        getDataMaterial()
        onAction()
    }

    private fun getDataFirebase() {
        showLoading()
        userDatasbase
            .child(currentUser?.uid.toString())
            .addValueEventListener(listenerUser)

        materialDatabase
            .addValueEventListener(listenerMaterials)

        binding.rvMaterialsMain.adapter = materialsAdapter
    }

    override fun onResume() {
        super.onResume()
        if(intent != null) {
            val position = intent.getIntExtra(EXTRA_POSITION, 0)
            binding.rvMaterialsMain.smoothScrollToPosition(position)
        }
    }

//    private fun getDataMaterial() {
//        showLoading()
//        val materials = Repository.getMaterials(this)
//
//        Handler(Looper.getMainLooper())
//            .postDelayed({
//                hideLoading()
//                materials?.let {
//                    materialsAdapter.materials = it
//                }
//            }, 1200)
//
//        binding.rvMaterialsMain.adapter = materialsAdapter
//    }

    private fun showLoading() {
        binding.swipeMain.isRefreshing = true
    }

    private fun hideLoading() {
        binding.swipeMain.isRefreshing = false
    }

    private fun onAction() {
        binding.apply {
            ivAvatarMain.setOnClickListener {
                startActivity<UserActivity>()
            }

            etSearchMain.addTextChangedListener {
                materialsAdapter.filter.filter(it.toString())
            }

            etSearchMain.setOnEditorActionListener { _ , actionId, _ ->
                   if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                       val dataSearch = etSearchMain.text.toString().trim()
                       materialsAdapter.filter.filter(dataSearch)
                       return@setOnEditorActionListener true
                   }
                    return@setOnEditorActionListener false
            }

            swipeMain.setOnRefreshListener {
                getDataFirebase()
            }
        }

        materialsAdapter.onClick { material , position ->
            startActivity<ContentActivity>()
            ContentActivity.EXTRA_MATERIAL to material
            ContentActivity.EXTRA_POSITION to position
        }
    }
}