package com.tandiera.project.elearning.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.tandiera.project.elearning.databinding.LayoutDialogErrorBinding
import com.tandiera.project.elearning.databinding.LayoutDialogLoadingBinding
import com.tandiera.project.elearning.databinding.LayoutDialogSuccessBinding

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.enabled() {
    isEnabled = true
}

fun View.disabled() {
    isEnabled = false
}

fun showDialogLoading(context: Context): AlertDialog? {
    val binding = LayoutDialogLoadingBinding.inflate(LayoutInflater.from(context))
    return AlertDialog
        .Builder(context)
        .setView(binding.root)
        .setCancelable(false)
        .create()
}

@SuppressLint("ServiceCast")
fun hideSoftKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showDialogSuccess(context: Context, message: String): AlertDialog{
    val binding = LayoutDialogSuccessBinding.inflate(LayoutInflater.from(context))
    binding.tvMessageError.text = message

    return AlertDialog
        .Builder(context)
        .setView(binding.root)
        .setCancelable(true)
        .create()
}

fun showDialogError(context: Context, message: String) {
    val binding = LayoutDialogErrorBinding.inflate(LayoutInflater.from(context))
    binding.tvMessageError.text = message
    AlertDialog
        .Builder(context)
        .setView(binding.root)
        .setCancelable(false)
        .create()
        .show()
}
