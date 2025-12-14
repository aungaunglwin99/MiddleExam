package com.example.middleexam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsystem.DatabaseHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity() {
    lateinit var toolbar : Toolbar
    lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        db = DatabaseHelper(this)

        val etUser = findViewById<TextInputEditText>(R.id.etUser)
        val etPass = findViewById<TextInputEditText>(R.id.etPass)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoRegister = findViewById<Button>(R.id.btnGoRegister)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Login"

        btnLogin.setOnClickListener {
            val id = db.login(etUser.text.toString(), etPass.text.toString())
            if (id != -1) {
                val i = Intent(this, HomeActivity::class.java)
                i.putExtra("USER_ID", id)
                startActivity(i)
                finish()
            } else {
                toast("Invalid login")
            }
        }
        btnGoRegister.setOnClickListener {
            startActivity(Intent(this, LoginSystem::class.java))
            finish()
        }
    }


    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}