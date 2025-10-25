// file: ui/auth/LoginActivity.kt
package com.example.appmoive.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.appmoive.databinding.ActivityLoginBinding
import com.example.appmoive.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        // Sự kiện click nút "Continue"
        binding.btnContinue.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.login(email, pass)
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // Sự kiện click vào "Sign Up" để chuyển màn hình
        binding.tvGoToSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        // Lắng nghe kết quả đăng nhập/đăng ký
        viewModel.authResult.observe(this) { success ->
            if (success) {
                // Đăng nhập thành công, thông báo và chuyển đến HomeActivity
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                goToHomeScreen()
            }
        }

        // Lắng nghe thông báo lỗi
        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        // Lắng nghe trạng thái loading để hiện/ẩn ProgressBar (nếu có)
        // Hoặc vô hiệu hóa nút để tránh spam click
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnContinue.isEnabled = !isLoading
        }
    }

    private fun goToHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        // Xóa tất cả các activity cũ khỏi stack, để người dùng không thể back lại màn hình login
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}