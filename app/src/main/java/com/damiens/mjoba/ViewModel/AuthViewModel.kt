package com.damiens.mjoba.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damiens.mjoba.Model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    // Authentication state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    // Current user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    // Login function
    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading

        // In a real app, this would call Firebase Auth
        // For now, simulate authentication
        viewModelScope.launch {
            try {
                // Simulate network delay
                kotlinx.coroutines.delay(1500)

                // Check credentials (in a real app, this would be Firebase Auth)
                if (email == "test@example.com" && password == "password") {
                    // Success - create a mock user
                    val user = User(
                        id = "u123",
                        name = "Test User",
                        email = email,
                        phone = "+254 712 345 678",
                        profileImageUrl = "",
                        address = "Nairobi, Kenya",
                        isServiceProvider = false,
                        dateCreated = System.currentTimeMillis()
                    )
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    // Failure
                    _authState.value = AuthState.Error("Invalid email or password")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An error occurred")
            }
        }
    }

    // Register function
    fun register(name: String, email: String, phone: String, password: String, isServiceProvider: Boolean) {
        _authState.value = AuthState.Loading

        // In a real app, this would call Firebase Auth
        // For now, simulate registration
        viewModelScope.launch {
            try {
                // Simulate network delay
                kotlinx.coroutines.delay(1500)

                // Create a user (in a real app, this would be Firebase Auth + Firestore)
                val user = User(
                    id = "u${System.currentTimeMillis()}",
                    name = name,
                    email = email,
                    phone = phone,
                    profileImageUrl = "",
                    address = "",
                    isServiceProvider = isServiceProvider,
                    dateCreated = System.currentTimeMillis()
                )
                _currentUser.value = user
                _authState.value = AuthState.Authenticated(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An error occurred")
            }
        }
    }

    // Password reset function
    fun resetPassword(email: String) {
        _authState.value = AuthState.Loading

        // In a real app, this would call Firebase Auth
        // For now, simulate password reset
        viewModelScope.launch {
            try {
                // Simulate network delay
                kotlinx.coroutines.delay(1500)

                // Simulate success
                _authState.value = AuthState.PasswordResetSent
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An error occurred")
            }
        }
    }

    // Logout function
    fun logout() {
        _authState.value = AuthState.Loading

        // In a real app, this would call Firebase Auth signOut
        // For now, simulate logout
        viewModelScope.launch {
            _currentUser.value = null
            _authState.value = AuthState.NotAuthenticated
        }
    }

    // Check if user is logged in
    fun checkAuthStatus() {
        _authState.value = AuthState.Loading

        // In a real app, this would check Firebase Auth current user
        // For now, simulate checking auth status
        viewModelScope.launch {
            // Simulate a delay
            kotlinx.coroutines.delay(1000)

            val user = _currentUser.value
            if (user != null) {
                _authState.value = AuthState.Authenticated(user)
            } else {
                _authState.value = AuthState.NotAuthenticated
            }
        }
    }
}

// Authentication states
sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object NotAuthenticated : AuthState()
    object PasswordResetSent : AuthState()
    data class Error(val message: String) : AuthState()
}
