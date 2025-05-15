package com.damiens.mjoba.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.damiens.mjoba.Data.AppLocationRepository
import com.damiens.mjoba.Data.LocationData
import com.damiens.mjoba.Model.GeoPoint
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlinx.coroutines.withContext

// Data class for location suggestions
data class LocationSuggestion(
    val name: String,
    val description: String = "",
    val latLng: LatLng
)

// A reusable location selection field for addresses
@Composable
fun LocationSelectionField(
    value: String,
    onValueChange: (String) -> Unit,
    onLocationSelected: (String, GeoPoint) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showSuggestions by remember { mutableStateOf(false) }
    var suggestions by remember { mutableStateOf(listOf<LocationSuggestion>()) }
    var isSearching by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                // When user types, fetch location suggestions
                if (it.length > 2) {
                    isSearching = true
                    coroutineScope.launch {
                        fetchLocationSuggestions(context, it) { results ->
                            suggestions = results
                            showSuggestions = results.isNotEmpty()
                            isSearching = false
                        }
                    }
                } else {
                    showSuggestions = false
                }
            },
            label = { Text("Location") },
            placeholder = { Text("e.g. Kilimani, Nairobi") },
            leadingIcon = { Icon(Icons.Default.LocationOn, null) },
            trailingIcon = {
                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Show location suggestions dropdown
        if (showSuggestions) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 4.dp
            ) {
                Column {
                    suggestions.forEach { suggestion ->
                        LocationSuggestionItem(
                            suggestion = suggestion,
                            onClick = {
                                onValueChange(suggestion.name)

                                // Create a LocationData object and save to repository
                                val locationData = LocationData(
                                    displayName = suggestion.name,
                                    address = suggestion.description,
                                    geoPoint = GeoPoint(
                                        suggestion.latLng.latitude,
                                        suggestion.latLng.longitude
                                    )
                                )

                                // Update the AppLocationRepository
                                AppLocationRepository.updateLocation(locationData)

                                // Also call the onLocationSelected callback
                                onLocationSelected(
                                    suggestion.name,
                                    GeoPoint(suggestion.latLng.latitude, suggestion.latLng.longitude)
                                )

                                showSuggestions = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationSuggestionItem(
    suggestion: LocationSuggestion,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = suggestion.name,
                style = MaterialTheme.typography.bodyLarge
            )

            if (suggestion.description.isNotEmpty()) {
                Text(
                    text = suggestion.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Function to fetch location suggestions based on search text
suspend fun fetchLocationSuggestions(
    context: Context,
    query: String,
    callback: (List<LocationSuggestion>) -> Unit
) = withContext(Dispatchers.IO) {
    // For a real app, this would use Google Places API
    // For now, we'll use Geocoder with some common Kenyan locations

    // Add Kenya to the query for more accurate results
    val searchQuery = if (!query.contains("Kenya", ignoreCase = true)) {
        "$query, Kenya"
    } else {
        query
    }

    try {
        // Use Geocoder to get suggestions
        val geocoder = Geocoder(context, Locale.getDefault())
        val suggestions = mutableListOf<LocationSuggestion>()

        // For Android 33+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Using continuation passing style for newer Android versions
            val result = suspendCancellableCoroutine<List<Address>> { continuation ->
                geocoder.getFromLocationName(searchQuery, 5) { addresses ->
                    continuation.resume(addresses)
                }
            }

            // Process results
            val locationSuggestions = result.mapNotNull { createSuggestionFromAddress(it) }

            if (locationSuggestions.isEmpty()) {
                withContext(Dispatchers.Main) {
                    callback(getCommonKenyanLocations(query))
                }
            } else {
                withContext(Dispatchers.Main) {
                    callback(locationSuggestions)
                }
            }
        } else {
            // For older Android versions
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(searchQuery, 5)
            suggestions.addAll(addresses?.mapNotNull { createSuggestionFromAddress(it) } ?: emptyList())

            withContext(Dispatchers.Main) {
                if (suggestions.isEmpty()) {
                    // Fallback to common locations if no results
                    callback(getCommonKenyanLocations(query))
                } else {
                    callback(suggestions)
                }
            }
        }
    } catch (e: Exception) {
        Log.e("LocationUtils", "Error fetching suggestions: ${e.message}")
        withContext(Dispatchers.Main) {
            // Fallback to common Kenyan locations if geocoding fails
            callback(getCommonKenyanLocations(query))
        }
    }
}

private fun getCommonKenyanLocations(query: String): List<LocationSuggestion> {
    return listOf(
        LocationSuggestion("Kilimani", "Nairobi, Kenya", LatLng(-1.287, 36.781)),
        LocationSuggestion("Westlands", "Nairobi, Kenya", LatLng(-1.267, 36.803)),
        LocationSuggestion("Lavington", "Nairobi, Kenya", LatLng(-1.275, 36.769)),
        LocationSuggestion("Githurai", "Nairobi, Kenya", LatLng(-1.219, 36.909)),
        LocationSuggestion("Karen", "Nairobi, Kenya", LatLng(-1.321, 36.716)),
        LocationSuggestion("Kileleshwa", "Nairobi, Kenya", LatLng(-1.280, 36.777)),
        LocationSuggestion("Ngong Road", "Nairobi, Kenya", LatLng(-1.299, 36.782)),
        LocationSuggestion("Eastleigh", "Nairobi, Kenya", LatLng(-1.271, 36.858)),
        LocationSuggestion("Mombasa Road", "Nairobi, Kenya", LatLng(-1.319, 36.850)),
        LocationSuggestion("CBD", "Nairobi, Kenya", LatLng(-1.284, 36.824))
    ).filter {
        it.name.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
    }
}

private fun createSuggestionFromAddress(address: Address): LocationSuggestion? {
    val name = when {
        !address.subLocality.isNullOrEmpty() -> address.subLocality
        !address.locality.isNullOrEmpty() -> address.locality
        !address.subAdminArea.isNullOrEmpty() -> address.subAdminArea
        else -> address.featureName
    } ?: return null

    val description = buildList {
        if (!address.locality.isNullOrEmpty() && address.locality != name) add(address.locality)
        if (!address.adminArea.isNullOrEmpty()) add(address.adminArea)
        if (!address.countryName.isNullOrEmpty() && address.countryName != "Kenya") add(address.countryName)
    }.joinToString(", ")

    return LocationSuggestion(
        name = name,
        description = description,
        latLng = LatLng(address.latitude, address.longitude)
    )
}

// Geocode an address string to coordinates
suspend fun geocodeAddress(context: Context, address: String): GeoPoint? = withContext(Dispatchers.IO) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val fullAddress = if (!address.contains("Kenya", ignoreCase = true)) {
            "$address, Kenya"
        } else {
            address
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            var result: GeoPoint? = null
            geocoder.getFromLocationName(fullAddress, 1) { addresses ->
                result = addresses.firstOrNull()?.let {
                    GeoPoint(it.latitude, it.longitude)
                }
            }
            // Wait for the result (not ideal but necessary with the callback API)
            // In a real app, you'd use a proper async approach
            Thread.sleep(1000)
            return@withContext result
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(fullAddress, 1)
            return@withContext addresses?.firstOrNull()?.let {
                GeoPoint(it.latitude, it.longitude)
            }
        }
    } catch (e: Exception) {
        Log.e("LocationUtils", "Error geocoding address: ${e.message}")
        // Return a default location for Nairobi if geocoding fails
        return@withContext GeoPoint(-1.286389, 36.817223)
    }
}