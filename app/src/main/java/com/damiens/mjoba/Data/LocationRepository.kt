
package com.damiens.mjoba.Data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.damiens.mjoba.Model.GeoPoint
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.util.geocodeAddress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import com.google.android.gms.location.LocationRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


class LocationRepository(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Current user location
    private val _userLocation = MutableStateFlow<GeoPoint?>(null)
    val userLocation: StateFlow<GeoPoint?> = _userLocation

    // Default Nairobi location if user location is not available
    private val defaultLocation = GeoPoint(-1.286389, 36.817223)

    /**
     * Get the current location of the user
     * @return GeoPoint representing the user's location or null if unavailable
     */
    suspend fun getCurrentLocation(): GeoPoint? = withContext(Dispatchers.IO) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            return@withContext null
        }

        try {
            suspendCancellableCoroutine { continuation ->
                val cancellationToken = CancellationTokenSource()

                val locationTask = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationToken.token
                )

                locationTask.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val geoPoint = GeoPoint(it.latitude, it.longitude)
                        _userLocation.value = geoPoint
                        continuation.resume(geoPoint)
                    } ?: continuation.resume(null)
                }

                locationTask.addOnFailureListener { exception ->
                    Log.e("LocationRepository", "Failed to get location: ${exception.message}")
                    continuation.resumeWithException(exception)
                }

                continuation.invokeOnCancellation {
                    cancellationToken.cancel()
                }
            }
        } catch (e: Exception) {
            Log.e("LocationRepository", "Error getting location: ${e.message}")
            null
        }
    }

    fun calculateDistance(point1: GeoPoint, point2: GeoPoint): Double {
        val earthRadius = 6371.0 // Earth's radius in kilometers

        val lat1 = Math.toRadians(point1.latitude)
        val lon1 = Math.toRadians(point1.longitude)
        val lat2 = Math.toRadians(point2.latitude)
        val lon2 = Math.toRadians(point2.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2).pow(2) +
                cos(lat1) * cos(lat2) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    /**
     * Get user-friendly distance string
     */
    fun getDistanceText(distance: Double): String {
        return when {
            distance < 1.0 -> "${(distance * 1000).toInt()} m away"
            else -> String.format("%.1f km away", distance)
        }
    }

    /**
     * Sort service providers by distance from a location
     */
    fun sortProvidersByDistance(providers: List<ServiceProvider>, fromLocation: GeoPoint): List<ServiceProvider> {
        return providers.sortedBy { provider ->
            provider.location?.let { location ->
                calculateDistance(fromLocation, location)
            } ?: Double.MAX_VALUE // Providers without location are sorted last
        }
    }

    /**
     * Find nearby providers within specified radius
     */
    fun findNearbyProviders(
        providers: List<ServiceProvider>,
        fromLocation: GeoPoint,
        maxDistanceKm: Double = 10.0
    ): List<ServiceProvider> {
        return providers.filter { provider ->
            provider.location?.let { location ->
                calculateDistance(fromLocation, location) <= maxDistanceKm
            } ?: false
        }
    }

    /**
     * Get address from location coordinates
     */
    suspend fun getAddressFromLocation(geoPoint: GeoPoint): String {
        // This functionality is already implemented in your geocodeAddress function,
        // but we might want to reverse geocode (coordinates to address)
        // For now, return a placeholder
        return "Nairobi, Kenya"
    }

    /**
     * Check if location permissions are granted
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

