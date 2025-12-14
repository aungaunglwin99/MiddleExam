package com.example.middleexam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.loginsystem.DatabaseHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginSystem : AppCompatActivity() {
    lateinit var db: DatabaseHelper

    lateinit var btRegister: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_system)

        db = DatabaseHelper(this)

        val etUser = findViewById<TextInputEditText>(R.id.etUser)
        val etPass = findViewById<TextInputEditText>(R.id.etPass)
        val etConfirm = findViewById<TextInputEditText>(R.id.etConfirm)

        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)
        val btnGoLogin = findViewById<MaterialButton>(R.id.btnGoLogin) // ✅ ADD THIS

        // REGISTER
        btnRegister.setOnClickListener {

            val user = etUser.text.toString()
            val pass = etPass.text.toString()
            val confirm = etConfirm.text.toString()

            when {
                user.isEmpty() || pass.isEmpty() || confirm.isEmpty() ->
                    toast("All fields required")

                pass != confirm ->
                    toast("Password not match")

                else -> {
                    if (db.register(user, pass)) {
                        toast("Register successful")
                        startActivity(Intent(this, Login::class.java))
                        finish()
                    } else {
                        toast("Username already exists")
                    }
                }
            }
        }

        // ✅ GO TO LOGIN
        btnGoLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}