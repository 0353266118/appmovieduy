// file: ui/auth/RegisterActivity.kt
package com.example.appmoive.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.appmoive.databinding.ActivityRegisterBinding
import com.example.appmoive.ui.home.HomeActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        // Nút Back trên header
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Nút "Agree and continue"
        binding.btnAgreeContinue.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString().trim()
            val pass = binding.etRegisterPassword.text.toString().trim()
            val confirmPass = binding.etConfirmPassword.text.toString().trim()

            // Kiểm tra đầu vào
            if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass != confirmPass) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Firebase yêu cầu mật khẩu ít nhất 6 ký tự
            if (pass.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Gọi ViewModel để đăng ký
            viewModel.register(email, pass)
        }
    }

    private fun observeViewModel() {
        viewModel.authResult.observe(this) { success ->
            if (success) {
                // Đăng ký thành công, tự động đăng nhập và chuyển màn hình
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                goToHomeScreen()
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnAgreeContinue.isEnabled = !isLoading
        }
    }

    private fun goToHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        // Xóa tất cả các activity cũ khỏi stack
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}