package com.example.middleexam

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.loginsystem.DatabaseHelper
import com.example.middleexam.databinding.ActivityLoginBinding
import com.example.middleexam.util.showToast

class LoginActivity : AppCompatActivity() {
    lateinit var db: DatabaseHelper
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        setSupportActionBar(binding.toolbar.tbToolbar)
        supportActionBar?.title = "Login"

        clearErrorOnTyping()
        binding.btLogin.setOnClickListener {
            login()
        }
        binding.btGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun login() = with(binding) setOnClickListener@{
        if (!validateFields()) return@setOnClickListener

        val id = db.login(etUsername.text.toString(), etPassword.text.toString())
        if (id != -1) {
            val i = Intent(this@LoginActivity, HomeActivity::class.java)
            i.putExtra("USER_ID", id)
            startActivity(i)
            finish()
        } else {
            tiPassword.error = "Invalid username or password"
            showToast("Invalid username or password")
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

        return isValid
    }

    private fun clearErrorOnTyping() = with(binding) {

        etUsername.addTextChangedListener {
            tiUsername.error = null
        }

        etPassword.addTextChangedListener {
            tiPassword.error = null
        }
    }
}