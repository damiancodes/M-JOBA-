package com.damiens.mjoba.ViewModel

import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceProvider

/**
 * UI state for services data
 */
sealed class ServicesUiState {
    /**
     * Initial state before any data is loaded
     */
    object Initial : ServicesUiState() // ✅ Fixed typo: was "SservicesUiState()"

    /**
     * Loading state while data is being fetched
     */
    object Loading : ServicesUiState()

    /**
     * Success state when a list of services is loaded
     */
    data class Success(val services: List<Service>) : ServicesUiState()

    /**
     * State for when a single service with its provider details is loaded
     */
    data class ServiceDetails(val service: Service, val provider: ServiceProvider) : ServicesUiState()

    /**
     * Error state with error message
     */
    data class Error(val message: String) : ServicesUiState()
}