

package com.damiens.mjoba.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damiens.mjoba.Data.remote.UserRepository
import com.damiens.mjoba.Model.User
import com.damiens.mjoba.Model.GeoPoint // Add this import
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    // Create instance of UserRepository
    private val userRepository = UserRepository()

    // Authentication state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    // Current user - now uses the repository's current user
    val currentUser = userRepository.currentUser

    init {
        // Observe changes to the currentUser from the repository
        viewModelScope.launch {
            userRepository.currentUser.collect { user ->
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                } else if (_authState.value !is AuthState.Loading) {
                    _authState.value = AuthState.NotAuthenticated
                }
            }
        }

        // Check initial auth status
        checkAuthStatus()
    }

    // Login function - now uses Firebase Auth
    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = userRepository.loginUser(email, password)

            result.fold(
                onSuccess = { user ->
                    // Success - user will be updated via the flow collection in init
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Invalid email or password")
                }
            )
        }
    }

    // Register function - now uses Firebase Auth
    fun register(name: String, email: String, phone: String, password: String, isServiceProvider: Boolean) {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = userRepository.registerUser(email, password, name, phone)

            result.fold(
                onSuccess = { user ->
                    // If the user wants to be a service provider, we would handle that separately
                    if (isServiceProvider) {
                        // We would typically call a ServiceProviderRepository method here
                        // For now, just update the user record
                        userRepository.updateUserProfile(isServiceProvider = true)
                    }
                    // User will be updated via the flow collection in init
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Registration failed")
                }
            )
        }
    }

    // Add this method to your AuthViewModel class
    fun resetPassword(email: String) {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                // Call the repository method
                val result = userRepository.sendPasswordResetEmail(email)

                result.fold(
                    onSuccess = {
                        _authState.value = AuthState.PasswordResetSent
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Failed to send password reset email")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    // Also make sure you have a method to reset the state
    fun resetState() {
        _authState.value = AuthState.Initial
    }

    // Logout function - now uses Firebase Auth
    fun logout() {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            userRepository.logout()
            // The currentUser flow will update to null, triggering the state change in the collector
        }
    }

    // Check if user is logged in - now uses Firebase Auth
    fun checkAuthStatus() {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            if (userRepository.isUserLoggedIn()) {
                // The user is already logged in, the currentUser flow will have the data
                // If not, force a refresh
                val userId = userRepository.getCurrentUserId()
                if (userId != null) {
                    userRepository.getUserById(userId)
                }
            } else {
                _authState.value = AuthState.NotAuthenticated
            }
        }
    }

    // Add the new updateUserProfile method here
    fun updateUserProfile(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        address: String? = null,
        profileImageUrl: String? = null,
        isServiceProvider: Boolean? = null,
        location: GeoPoint? = null // Optional parameter
    ) {
        // Show loading state
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val result = userRepository.updateUserProfile(
                    name = name,
                    email = email,
                    phone = phone,
                    address = address,
                    profileImageUrl = profileImageUrl,
                    isServiceProvider = isServiceProvider,
                    location = location
                )

                result.fold(
                    onSuccess = { user ->
                        // User is updated via Flow from userRepository, no need to update manually
                        // Success notification can be emitted if needed
                        _authState.value = AuthState.Authenticated(user)
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Failed to update profile")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An error occurred while updating profile")
            }
        }
    }
}

// Keep your existing AuthState sealed class
sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object NotAuthenticated : AuthState()
    object PasswordResetSent : AuthState()
    data class Error(val message: String) : AuthState()
}