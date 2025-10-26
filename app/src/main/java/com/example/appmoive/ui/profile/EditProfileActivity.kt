// file: ui/profile/EditProfileActivity.kt
package com.example.appmoive.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.appmoive.R // Import R để có thể dùng drawable
import com.example.appmoive.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    // Bỏ hết các biến liên quan đến chọn ảnh
    // private var imageUri: Uri? = null
    // private val selectImageLauncher = ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        loadUserInfo()
        setupClickListeners()
    }

    // Tải và hiển thị thông tin người dùng hiện tại
    private fun loadUserInfo() {
        val user = firebaseAuth.currentUser
        user?.let {
            binding.etUsername.setText(it.displayName)
            binding.etEmail.setText(it.email)

            // Vẫn hiển thị ảnh đại diện cũ (nếu có)
            if (it.photoUrl != null) {
                Glide.with(this).load(it.photoUrl).into(binding.ivProfileAvatar)
            } else {
                // Nếu không có ảnh, hiển thị một ảnh mặc định
                binding.ivProfileAvatar.setImageResource(R.drawable.placeholder_avatar)
            }
        }
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Vô hiệu hóa chức năng click vào ảnh
        binding.ivProfileAvatar.isClickable = false

        binding.btnSaveChanges.setOnClickListener {
            val newUsername = binding.etUsername.text.toString().trim()
            if (newUsername.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Chỉ gọi hàm cập nhật tên
            updateUsername(newUsername)
        }
    }

    // Hàm chỉ dùng để cập nhật tên người dùng
    private fun updateUsername(username: String) {
        val user = firebaseAuth.currentUser ?: return

        // Vô hiệu hóa nút để tránh spam
        binding.btnSaveChanges.isEnabled = false
        Toast.makeText(this, "Updating username...", Toast.LENGTH_SHORT).show()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Username updated successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Đóng màn hình sau khi cập nhật thành công
                } else {
                    Toast.makeText(this, "Failed to update username: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
                // Bật lại nút dù thành công hay thất bại
                binding.btnSaveChanges.isEnabled = true
            }
    }
}