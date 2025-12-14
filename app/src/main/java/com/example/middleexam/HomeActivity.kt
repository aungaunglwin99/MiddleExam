package com.example.middleexam

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.loginsystem.DatabaseHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class HomeActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var toolbar: Toolbar
    private lateinit var tvStatus: TextView
    private lateinit var etStatus: EditText

    private var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"

        // Database
        db = DatabaseHelper(this)
        userId = intent.getIntExtra("USER_ID", -1)

        // Views
        etStatus = findViewById(R.id.etStatus)
        tvStatus = findViewById(R.id.tvStatus)

        val btnUpload = findViewById<MaterialButton>(R.id.btnUpload)
        val btnUpdate = findViewById<MaterialButton>(R.id.btnUpdate)
        val btnRemove = findViewById<MaterialButton>(R.id.btnRemove)

        // Load status
        val status = db.getStatus(userId)
        if (!status.isNullOrEmpty()) {
            tvStatus.text = status
        }

        // UPLOAD
        btnUpload.setOnClickListener {
            val text = etStatus.text.toString().trim()
            if (text.isEmpty()) {
                toast("Enter status")
            } else {
                db.saveStatus(userId, text)
                tvStatus.text = text
                etStatus.setText("")
                toast("Status uploaded")
            }
        }

        // UPDATE (Dialog)
        btnUpdate.setOnClickListener {
            if (!db.hasStatus(userId)) {
                toast("No status found. Upload first.")
            } else {
                showUpdateDialog(tvStatus.text.toString())
            }
        }

        // REMOVE
        btnRemove.setOnClickListener {
            if (!db.hasStatus(userId)) {
                toast("No status to remove")
            } else {
                showDeleteDialog()
            }
        }

    }

    // Toolbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                startActivity(Intent(this, LoginSystem::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Update dialog
    private fun showUpdateDialog(currentStatus: String) {
        val view = layoutInflater.inflate(R.layout.dialog_update_status, null)
        val etDialog = view.findViewById<TextInputEditText>(R.id.etDialogStatus)
        val btnDialogUpdate = view.findViewById<MaterialButton>(R.id.btnDialogUpdate)

        etDialog.setText(currentStatus)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        btnDialogUpdate.setOnClickListener {
            val newText = etDialog.text.toString().trim()
            if (newText.isEmpty()) {
                toast("Status cannot be empty")
            } else {
                db.saveStatus(userId, newText)
                tvStatus.text = newText
                toast("Status updated")
                dialog.dismiss()
            }
        }

        dialog.show()
    }
    private fun showDeleteDialog() {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Delete Status")
            .setMessage("Do you want to delete this status?")
            .setPositiveButton("YES") { _, _ ->
                db.deleteStatus(userId)
                tvStatus.text = "No status yet"
                etStatus.setText("")
                toast("Status removed")
            }
            .setNegativeButton("CANCEL", null)
            .create()

        dialog.show()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
