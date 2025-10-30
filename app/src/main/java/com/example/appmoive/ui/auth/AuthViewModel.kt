// file: ui/auth/AuthViewModel.kt
package com.example.appmoive.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData để thông báo kết quả (thành công/thất bại)
    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> = _authResult

    // LiveData để gửi thông báo lỗi
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // LiveData để báo hiệu đang xử lý
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, pass: String) {
        _isLoading.postValue(true)
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.postValue(true)
                } else {
                    _errorMessage.postValue(task.exception?.message ?: "Login Failed. Please try again.")
                }
                _isLoading.postValue(false)
            }
    }

    fun register(email: String, pass: String) {
        _isLoading.postValue(true)
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.postValue(true)
                } else {
                    _errorMessage.postValue(task.exception?.message ?: "Registration Failed. Please try again.")
                }
                _isLoading.postValue(false)
            }
    }
    // Thêm hàm gửi email reset password
    fun sendPasswordResetEmail(email: String) {
        _isLoading.postValue(true)
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Không cần thông báo thành công ở đây, để Activity tự xử lý
                } else {
                    _errorMessage.postValue(task.exception?.message ?: "Failed to send reset email.")
                }
                _isLoading.postValue(false)
            }
    }
}