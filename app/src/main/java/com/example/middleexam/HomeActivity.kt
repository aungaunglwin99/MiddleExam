package com.example.middleexam

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsystem.DatabaseHelper
import com.example.middleexam.databinding.ActivityHomeBinding
import com.example.middleexam.util.showToast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class HomeActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.toolbar.tbToolbar)
        supportActionBar?.title = "Home"

        // Database
        db = DatabaseHelper(this)
        userId = intent.getIntExtra("USER_ID", -1)
        showToast("Register successful")


        val btnUpload = findViewById<MaterialButton>(R.id.btUpload)
        val btnUpdate = findViewById<MaterialButton>(R.id.btUpdate)
        val btnRemove = findViewById<MaterialButton>(R.id.btRemove)

        // Load status
        val status = db.getStatus(userId)
        if (!status.isNullOrEmpty()) {
            binding.tvStatus.text = status
        }

        // UPLOAD
        btnUpload.setOnClickListener {
            uploadStatus()
        }

        // UPDATE (Dialog)
        btnUpdate.setOnClickListener {
            if (!db.hasStatus(userId)) {
                showToast("No status found. Upload first.")
            } else {
                showUpdateDialog(binding.tvStatus.text.toString())
            }
        }

        // REMOVE
        btnRemove.setOnClickListener {
            if (!db.hasStatus(userId)) {
                showToast("No status to remove")
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
            R.id.mitem_logout -> {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // Update dialog
    private fun showUpdateDialog(currentStatus: String) {
        val view = layoutInflater.inflate(R.layout.dialog_update_status, null)
        val etDialog = view.findViewById<TextInputEditText>(R.id.etStatus)
        val btnDialogUpdate = view.findViewById<MaterialButton>(R.id.btUpdate)

        etDialog.setText(currentStatus)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        btnDialogUpdate.setOnClickListener {
            val newText = etDialog.text.toString().trim()
            if (newText.isEmpty()) {
                showToast("Status cannot be empty")
            } else {
                db.saveStatus(userId, newText)
                binding.tvStatus.text = newText
                showToast("Status updated")
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
                binding.tvStatus.text = "No status yet"
                binding.etStatus.setText("")
                showToast("Status removed")
            }
            .setNegativeButton("CANCEL", null)
            .create()

        dialog.show()
    }

    fun uploadStatus() = with(binding) {
        val text = etStatus.text.toString().trim()
        if (text.isEmpty()) {
            showToast("Enter status")
        } else {
            db.saveStatus(userId, text)
            tvStatus.text = text
            etStatus.setText("")
            showToast("Status uploaded")
        }
    }
}
