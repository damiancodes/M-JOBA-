package com.damiens.mjoba.ui.theme.Screens.Maps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Data.AppLocationRepository
import com.damiens.mjoba.Data.LocationData
import com.damiens.mjoba.Model.GeoPoint
import com.damiens.mjoba.ui.theme.SafaricomGreen
import com.damiens.mjoba.ui.theme.Screens.Map.LocationViewModel
import com.damiens.mjoba.ui.theme.Screens.Map.LocationViewModelFactory
import com.damiens.mjoba.util.LocationSuggestion
import com.damiens.mjoba.util.fetchLocationSuggestions
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectionScreen(
    navController: NavHostController,
    viewModel: LocationViewModel = viewModel(
        factory = LocationViewModelFactory(LocalContext.current)
    )
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // State variables
    var searchQuery by remember { mutableStateOf("") }
    var locationSuggestions by remember { mutableStateOf<List<LocationSuggestion>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }

    // Get the current saved location from AppLocationRepository
    val currentSavedLocation by AppLocationRepository.selectedLocation.collectAsState()

    // Get saved locations - in a real app, this would come from your user profile
    val savedLocations = remember {
        listOf(
            LocationSuggestion("Home", "Kilimani, Nairobi", LatLng(-1.2921, 36.7815)),
            LocationSuggestion("Work", "Westlands, Nairobi", LatLng(-1.2673, 36.8035)),
            LocationSuggestion("Gym", "Lavington, Nairobi", LatLng(-1.2723, 36.7755))
        )
    }

    // Collect state
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()

    // Fetch user location when screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchUserLocation()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Set Your Location") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Show current location if available
            currentSavedLocation?.let { location ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = SafaricomGreen
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Current Selected Location",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = location.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = location.address,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    if (query.length > 2) {
                        isSearching = true
                        coroutineScope.launch {
                            fetchLocationSuggestions(context, query) { suggestions ->
                                locationSuggestions = suggestions
                                isSearching = false
                            }
                        }
                    } else {
                        locationSuggestions = emptyList()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search for a location (e.g., Kilimani)") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Current location button
            FilledTonalButton(
                onClick = {
                    viewModel.fetchUserLocation()
                    // Use the current location
                    coroutineScope.launch {
                        userLocation?.let { location ->
                            // Create location data and update repository
                            val locationData = LocationData(
                                displayName = "Current Location",
                                address = "Your current location", // This would be resolved with reverse geocoding
                                geoPoint = location
                            )

                            // Update the shared repository
                            AppLocationRepository.updateLocation(locationData)

                            // Update in LocationViewModel as well
                            viewModel.setSearchLocation(location)

                            // Navigate back
                            navController.navigateUp()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Current Location"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Use Current Location")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // If there are search results, show them
            if (locationSuggestions.isNotEmpty()) {
                Text(
                    text = "Search Results",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(locationSuggestions) { suggestion ->
                        LocationItem(
                            title = suggestion.name,
                            subtitle = suggestion.description,
                            onClick = {
                                // Create location data and update repository
                                val locationData = LocationData(
                                    displayName = suggestion.name,
                                    address = suggestion.description,
                                    geoPoint = GeoPoint(
                                        suggestion.latLng.latitude,
                                        suggestion.latLng.longitude
                                    )
                                )

                                // Update the shared repository
                                AppLocationRepository.updateLocation(locationData)

                                // Update in LocationViewModel as well
                                viewModel.setSearchLocation(GeoPoint(
                                    suggestion.latLng.latitude,
                                    suggestion.latLng.longitude
                                ))

                                // Navigate back
                                navController.navigateUp()
                            }
                        )
                    }
                }
            } else if (searchQuery.length > 2 && !isSearching) {
                // No results found
                Text(
                    text = "No locations found for '$searchQuery'",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            } else if (locationSuggestions.isEmpty() && searchQuery.length <= 2 && !isSearching) {
                // Show saved locations
                Text(
                    text = "Saved Locations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(savedLocations) { location ->
                        LocationItem(
                            title = location.name,
                            subtitle = location.description,
                            icon = Icons.Default.Bookmark,
                            onClick = {
                                // Create location data and update repository
                                val locationData = LocationData(
                                    displayName = location.name,
                                    address = location.description,
                                    geoPoint = GeoPoint(
                                        location.latLng.latitude,
                                        location.latLng.longitude
                                    )
                                )

                                // Update the shared repository
                                AppLocationRepository.updateLocation(locationData)

                                // Update in LocationViewModel as well
                                viewModel.setSearchLocation(GeoPoint(
                                    location.latLng.latitude,
                                    location.latLng.longitude
                                ))

                                // Navigate back
                                navController.navigateUp()
                            }
                        )
                    }
                }
            }

            // Error message if any
            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun LocationItem(
    title: String,
    subtitle: String,
    icon: ImageVector = Icons.Default.LocationOn,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SafaricomGreen
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}