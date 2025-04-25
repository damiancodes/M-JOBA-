package com.damiens.mjoba.ui.theme.Screens.Map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.damiens.mjoba.Data.LocationRepository
import com.damiens.mjoba.Model.GeoPoint
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.util.SampleData
import com.damiens.mjoba.util.geocodeAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling location and map-related functionality
 */
class LocationViewModel(
    private val context: Context
) : ViewModel() {

    private val locationRepository = LocationRepository(context)

    // User location
    private val _userLocation = MutableStateFlow<GeoPoint?>(null)
    val userLocation: StateFlow<GeoPoint?> = _userLocation.asStateFlow()

    // Search location (when user searches for a specific area)
    private val _searchLocation = MutableStateFlow<GeoPoint?>(null)
    val searchLocation: StateFlow<GeoPoint?> = _searchLocation.asStateFlow()

    // Service providers
    private val _serviceProviders = MutableStateFlow<List<ServiceProviderWithDistance>>(emptyList())
    val serviceProviders: StateFlow<List<ServiceProviderWithDistance>> = _serviceProviders.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchUserLocation()
    }

    /**
     * Fetch the user's current location
     */
    fun fetchUserLocation() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val location = locationRepository.getCurrentLocation()
                _userLocation.value = location

                // If a user location is available, update provider distances
                location?.let { updateProviderDistances(it) }
            } catch (e: Exception) {
                _error.value = "Could not get your location. Please check your settings."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Search for a location by address
     */
    fun searchLocation(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val geoPoint = geocodeAddress(context, query)
                _searchLocation.value = geoPoint

                // Update provider distances from the search location
                geoPoint?.let { updateProviderDistances(it) }
            } catch (e: Exception) {
                _error.value = "Could not find the location. Please try a different search."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Set search location directly using a GeoPoint
     * This is used when selecting from location suggestions
     */
    fun setSearchLocation(location: GeoPoint) {
        _searchLocation.value = location
        // Update provider distances based on the new location
        updateProviderDistances(location)
    }

    /**
     * Load service providers for a specific category
     */
    fun loadServiceProviders(categoryId: String) {
        // In a real app, you would fetch this from a repository connected to Firebase
        val providers = SampleData.getServiceProvidersByCategory(categoryId)

        // Get current reference location (either user location or search location)
        val referenceLocation = _searchLocation.value ?: _userLocation.value

        referenceLocation?.let {
            updateProviderDistances(it, providers)
        } ?: run {
            // If no location is available, just display the providers without distance
            _serviceProviders.value = providers.map { provider ->
                ServiceProviderWithDistance(provider, null)
            }
        }
    }

    /**
     * Update distances for all providers based on a reference location
     */
    private fun updateProviderDistances(
        referenceLocation: GeoPoint,
        providers: List<ServiceProvider> = _serviceProviders.value.map { it.provider }
    ) {
        val providersWithDistance = providers.map { provider ->
            val distance = provider.location?.let { location ->
                locationRepository.calculateDistance(referenceLocation, location)
            }

            val distanceText = distance?.let {
                locationRepository.getDistanceText(it)
            } ?: "Distance unknown"

            ServiceProviderWithDistance(
                provider = provider,
                distanceKm = distance,
                distanceText = distanceText
            )
        }

        // Sort by distance
        _serviceProviders.value = providersWithDistance.sortedBy { it.distanceKm ?: Double.MAX_VALUE }
    }

    /**
     * Filter providers by distance
     */
    fun filterByDistance(maxDistanceKm: Double) {
        val referenceLocation = _searchLocation.value ?: _userLocation.value ?: return

        val filteredProviders = _serviceProviders.value.filter { providerWithDistance ->
            val distance = providerWithDistance.distanceKm ?: return@filter false
            distance <= maxDistanceKm
        }

        _serviceProviders.value = filteredProviders
    }

    /**
     * Clear any error message
     */
    fun clearError() {
        _error.value = null
    }
}

/**
 * Data class to hold a service provider with its distance information
 */
data class ServiceProviderWithDistance(
    val provider: ServiceProvider,
    val distanceKm: Double? = null,
    val distanceText: String = "Distance unknown"
)

/**
 * Factory to create LocationViewModel with context
 */
class LocationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}