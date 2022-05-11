package com.tandiera.project.elearning.presentation.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.tandiera.project.elearning.databinding.ActivityUserBinding
import com.tandiera.project.elearning.model.User
import com.tandiera.project.elearning.presentation.changepassword.ChangePasswordActivity
import com.tandiera.project.elearning.presentation.login.LoginActivity
import com.tandiera.project.elearning.utils.showDialogError
import org.jetbrains.anko.startActivity

class UserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var userDatasbase : DatabaseReference
    private var currentUser : FirebaseUser? = null

    private var listenerUser = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            val user = snapshot.getValue(User::class.java)
            user?.let {
                binding.tvNameUser.text = it.nameUser
                binding.tvEmailUser.text = it.emailUser

                Glide
                    .with(this@UserActivity)
                    .load(it.avatarUser)
                    .placeholder(android.R.color.darker_gray)
                    .into(binding.ivAvatarUser)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            showDialogError(this@UserActivity, error.message)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init
        firebaseAuth = FirebaseAuth.getInstance()
        userDatasbase = FirebaseDatabase.getInstance().getReference("users")
        currentUser = firebaseAuth.currentUser

        getDataFirebase()
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

            swipeUser.setOnRefreshListener {
                getDataFirebase()
            }
        }
    }

    private fun getDataFirebase() {
        showLoading()
        userDatasbase
            .child(currentUser?.uid.toString())
            .addValueEventListener(listenerUser)
    }

    private fun showLoading() {
        binding.swipeUser.isRefreshing = true
    }

    private fun hideLoading() {
        binding.swipeUser.isRefreshing = false
    }
}