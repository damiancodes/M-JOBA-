package com.damiens.mjoba.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damiens.mjoba.Data.remote.ServiceProviderRepository
import com.damiens.mjoba.Model.GeoPoint
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServiceProviderViewModel : ViewModel() {
    private val serviceProviderRepository = ServiceProviderRepository()

    // UI state for providers list
    private val _providersState = MutableStateFlow<ProvidersUiState>(ProvidersUiState.Initial)
    val providersState: StateFlow<ProvidersUiState> = _providersState.asStateFlow()

    // UI state for provider details
    private val _providerDetailsState = MutableStateFlow<ProviderDetailsUiState>(ProviderDetailsUiState.Initial)
    val providerDetailsState: StateFlow<ProviderDetailsUiState> = _providerDetailsState.asStateFlow()

    // Get all service providers
    fun getAllProviders() {
        _providersState.value = ProvidersUiState.Loading

        viewModelScope.launch {
            val result = serviceProviderRepository.getAllServiceProviders()

            result.fold(
                onSuccess = { providers ->
                    _providersState.value = ProvidersUiState.Success(providers)
                },
                onFailure = { exception ->
                    _providersState.value = ProvidersUiState.Error(exception.message ?: "Failed to load providers")
                }
            )
        }
    }

    // Get providers by category
    fun getProvidersByCategory(categoryId: String) {
        _providersState.value = ProvidersUiState.Loading

        viewModelScope.launch {
            val result = serviceProviderRepository.getServiceProvidersByCategory(categoryId)

            result.fold(
                onSuccess = { providers ->
                    _providersState.value = ProvidersUiState.Success(providers)
                },
                onFailure = { exception ->
                    _providersState.value = ProvidersUiState.Error(exception.message ?: "Failed to load providers")
                }
            )
        }
    }

    // Get provider details
    fun getProviderDetails(providerId: String) {
        _providerDetailsState.value = ProviderDetailsUiState.Loading

        viewModelScope.launch {
            val providerResult = serviceProviderRepository.getServiceProviderByUserId(providerId)

            providerResult.fold(
                onSuccess = { provider ->
                    // Load provider's services as well
                    val servicesResult = serviceProviderRepository.getProviderServices(providerId)

                    servicesResult.fold(
                        onSuccess = { services ->
                            _providerDetailsState.value = ProviderDetailsUiState.Success(provider, services)
                        },
                        onFailure = { exception ->
                            // We have the provider but failed to load services
                            _providerDetailsState.value = ProviderDetailsUiState.Success(provider, emptyList())
                        }
                    )
                },
                onFailure = { exception ->
                    _providerDetailsState.value = ProviderDetailsUiState.Error(exception.message ?: "Failed to load provider details")
                }
            )
        }
    }

    fun updateVerificationStatus(providerId: String, isVerified: Boolean) {
        viewModelScope.launch {
            try {
                val result = serviceProviderRepository.updateVerificationStatus(providerId, isVerified)

                result.fold(
                    onSuccess = { success ->
                        if (success) {
                            // Refresh the providers list to show the updated status
                            getAllProviders()
                        } else {
                            // Handle the case where the update was not successful
                            _providersState.value = ProvidersUiState.Error("Failed to update verification status")
                        }
                    },
                    onFailure = { exception ->
                        // Handle failure
                        _providersState.value = ProvidersUiState.Error(
                            exception.message ?: "Failed to update verification status"
                        )
                    }
                )
            } catch (e: Exception) {
                // Handle unexpected errors
                _providersState.value = ProvidersUiState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    // Search providers by name
    fun searchProviders(query: String) {
        _providersState.value = ProvidersUiState.Loading

        viewModelScope.launch {
            val result = serviceProviderRepository.searchProvidersByName(query)

            result.fold(
                onSuccess = { providers ->
                    _providersState.value = ProvidersUiState.Success(providers)
                },
                onFailure = { exception ->
                    _providersState.value = ProvidersUiState.Error(exception.message ?: "Search failed")
                }
            )
        }
    }
}

// Sealed classes to represent different UI states
sealed class ProvidersUiState {
    object Initial : ProvidersUiState()
    object Loading : ProvidersUiState()
    data class Success(val providers: List<ServiceProvider>) : ProvidersUiState()
    data class Error(val message: String) : ProvidersUiState()
}

sealed class ProviderDetailsUiState {
    object Initial : ProviderDetailsUiState()
    object Loading : ProviderDetailsUiState()
    data class Success(val provider: ServiceProvider, val services: List<Service>) : ProviderDetailsUiState()
    data class Error(val message: String) : ProviderDetailsUiState()
}

