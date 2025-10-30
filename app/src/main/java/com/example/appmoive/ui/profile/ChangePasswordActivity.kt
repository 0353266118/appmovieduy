// file: ui/profile/ChangePasswordActivity.kt
package com.example.appmoive.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appmoive.databinding.ActivityChangePasswordBinding
import com.example.appmoive.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnChangeNow.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPassword.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changePassword(newPassword)
        }
    }

    private fun changePassword(newPassword: String) {
        val user = firebaseAuth.currentUser


        binding.btnChangeNow.isEnabled = false

        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->

                binding.btnChangeNow.isEnabled = true

                if (task.isSuccessful) {
                    showSuccessDialog()
                } else {
                    val errorMessage = task.exception?.message ?: "Failed to update password. Please try again."
                    Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Password Updated")
            .setMessage("Your password has been changed successfully. Please log in again to continue.")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                logoutAndGoToLogin()
                dialog.dismiss()
            }
            .create()
            .show()
    }


    private fun logoutAndGoToLogin() {
        firebaseAuth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        // Xóa tất cả các activity cũ khỏi stack
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}