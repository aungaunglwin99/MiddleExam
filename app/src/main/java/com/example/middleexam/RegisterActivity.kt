package com.example.middleexam

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.loginsystem.DatabaseHelper
import com.example.middleexam.databinding.ActivityRegisterBinding
import com.example.middleexam.util.showToast

class RegisterActivity : AppCompatActivity() {
    lateinit var db: DatabaseHelper

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.tbToolbar)
        supportActionBar?.title = "Register"

        db = DatabaseHelper(this)

        clearErrorOnTyping()

        binding.btRegister.setOnClickListener {
            register()
        }

        binding.btGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateFields(): Boolean = with(binding) {
        var isValid = true

        if (etUsername.text.isNullOrBlank()) {
            tiUsername.error = "Username is Required"
            isValid = false
        }

        if (etPassword.text.isNullOrBlank()) {
            tiPassword.error = "Password is Required"
            isValid = false
        }

        if (etCPassword.text.isNullOrBlank()) {
            tiCPassword.error = "Confirm Password is Required"
            isValid = false
        }

        return isValid
    }

    private fun clearErrorOnTyping() = with(binding) {

        etUsername.addTextChangedListener {
            tiUsername.error = null
        }

        etPassword.addTextChangedListener {
            tiPassword.error = null
        }

        etCPassword.addTextChangedListener {
            tiCPassword.error = null
        }
    }

    private fun register() = with(binding) setOnClickListener@{
        if (!validateFields()) return@setOnClickListener

        val user = etUsername.text.toString()
        val pass = etPassword.text.toString()
        val confirm = etCPassword.text.toString()

        when {
            pass != confirm -> {
                tiCPassword.error = "Password not match"
            }

            else -> {
                if (db.register(user, pass)) {
                    showToast("Register successful")
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    tiUsername.error = "Username already exists"
                }
            }
        }
    }
}