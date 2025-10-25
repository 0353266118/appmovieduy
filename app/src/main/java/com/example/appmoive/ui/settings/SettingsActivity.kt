// file: ui/settings/SettingsActivity.kt
package com.example.appmoive.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appmoive.databinding.ActivitySettingsBinding
import com.example.appmoive.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Nút Back
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Nút Logout
        binding.layoutLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    // MỚI: Hàm hiển thị hộp thoại xác nhận đăng xuất
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, _ ->
                // Người dùng nhấn Yes -> Thực hiện đăng xuất
                logoutUser()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                // Người dùng nhấn No -> Đóng hộp thoại
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // MỚI: Hàm thực hiện đăng xuất và chuyển màn hình
    private fun logoutUser() {
        // Đăng xuất khỏi Firebase
        firebaseAuth.signOut()

        // Chuyển người dùng về màn hình Login
        val intent = Intent(this, LoginActivity::class.java)
        // Xóa tất cả các activity cũ khỏi stack, đảm bảo người dùng không thể back lại
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}