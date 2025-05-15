package com.damiens.mjoba.Data

import android.content.Context
import com.damiens.mjoba.Model.GeoPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Singleton repository pattern for location data
object AppLocationRepository {
    // User's selected location
    private val _selectedLocation = MutableStateFlow<LocationData?>(null)
    val selectedLocation: StateFlow<LocationData?> = _selectedLocation.asStateFlow()

    // Update location
    fun updateLocation(location: LocationData) {
        _selectedLocation.value = location
    }

    // Clear location
    fun clearLocation() {
        _selectedLocation.value = null
    }
}

// Data class to hold complete location information
data class LocationData(
    val displayName: String,
    val address: String,
    val geoPoint: GeoPoint
)