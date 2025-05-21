package com.damiens.mjoba.ui.theme.Screens.Map

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.GeoPoint
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.ui.theme.SafaricomGreen
import com.damiens.mjoba.util.LocationSuggestion
import com.damiens.mjoba.util.SampleData
import com.damiens.mjoba.util.fetchLocationSuggestions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyProvidersScreen(
    navController: NavHostController,
    categoryId: String,
    viewModel: LocationViewModel = viewModel(
        factory = LocationViewModelFactory(LocalContext.current)
    )
) {
    val context = LocalContext.current
    val category = remember { SampleData.getCategory(categoryId) }
    val coroutineScope = rememberCoroutineScope()

    // Collect state from ViewModel
    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()
    val searchLocation by viewModel.searchLocation.collectAsStateWithLifecycle()
    val providers by viewModel.serviceProviders.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    // Load providers when screen is shown
    LaunchedEffect(categoryId) {
        viewModel.loadServiceProviders(categoryId)
    }

    // Map state
    val defaultLocation = LatLng(-1.286389, 36.817223) // Nairobi coordinates
    val currentLocation = searchLocation?.let { LatLng(it.latitude, it.longitude) }
        ?: userLocation?.let { LatLng(it.latitude, it.longitude) }
        ?: defaultLocation

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 13f)
    }

    // Update camera position when user or search location changes
    LaunchedEffect(userLocation, searchLocation) {
        val newLocation = searchLocation?.let { LatLng(it.latitude, it.longitude) }
            ?: userLocation?.let { LatLng(it.latitude, it.longitude) }
            ?: return@LaunchedEffect

        cameraPositionState.position = CameraPosition.fromLatLngZoom(newLocation, 14f)
    }

    // Location search states
    var searchQuery by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(false) }
    var locationSuggestions by remember { mutableStateOf<List<LocationSuggestion>>(emptyList()) }

    // Check location permission
    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Selected provider for bottom sheet
    var selectedProvider by remember { mutableStateOf<ServiceProviderWithDistance?>(null) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Nearby ${category.name} Providers") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }

                    IconButton(onClick = {
                        // Show filter options dialog
                        // This would filter providers by distance, rating, etc.
                    }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Google Map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = true
                ),
                onMapLoaded = {
                    // Map is fully loaded
                    if (userLocation == null && hasLocationPermission) {
                        viewModel.fetchUserLocation()
                    }
                }
            ) {
                // Add markers for each provider
                providers.forEach { providerWithDistance ->
                    val provider = providerWithDistance.provider

                    // Use the provider's location or default to a nearby position if null
                    val position = provider.location?.let {
                        LatLng(it.latitude, it.longitude)
                    } ?: getRandomNearbyLocation(currentLocation)

                    Marker(
                        state = MarkerState(position = position),
                        title = provider.businessName,
                        snippet = providerWithDistance.distanceText,
                        onClick = {
                            selectedProvider = providerWithDistance
                            showBottomSheet = true
                            true
                        },
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        )
                    )
                }

                // Show user location marker
                userLocation?.let {
                    val userPosition = LatLng(it.latitude, it.longitude)
                    Marker(
                        state = MarkerState(position = userPosition),
                        title = "Your Location",
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_BLUE
                        )
                    )
                }
            }

            // Enhanced search bar with location search
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Location search field
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { query ->
                                searchQuery = query
                                if (query.length > 2) {
                                    coroutineScope.launch {
                                        fetchLocationSuggestions(context, query) { suggestions ->
                                            locationSuggestions = suggestions
                                            showSuggestions = suggestions.isNotEmpty()
                                        }
                                    }
                                } else {
                                    showSuggestions = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Enter area ( Kilimani, Nairobi)") },
                            label = { Text("Search by location") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = SafaricomGreen
                                )
                            },
                            trailingIcon = {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = {
                                        viewModel.searchLocation(searchQuery)
                                        showSuggestions = false
                                    }) {
                                        Icon(Icons.Default.Search, "Search Location")
                                    }
                                }
                            },
                            singleLine = true
                        )

                        // Location suggestions
                        if (showSuggestions) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shadowElevation = 4.dp
                            ) {
                                Column {
                                    locationSuggestions.forEach { suggestion ->
                                        LocationSuggestionItem(
                                            suggestion = suggestion,
                                            onClick = {
                                                searchQuery = suggestion.name
                                                showSuggestions = false

                                                // Convert LatLng to GeoPoint and update in ViewModel
                                                val geoPoint = GeoPoint(
                                                    suggestion.latLng.latitude,
                                                    suggestion.latLng.longitude
                                                )
                                                viewModel.setSearchLocation(geoPoint)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Show error if any
                        if (error != null) {
                            Text(
                                text = error!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            TextButton(onClick = { viewModel.clearError() }) {
                                Text("Dismiss")
                            }
                        }
                    }
                }

                // Provider count
                if (providers.isNotEmpty()) {
                    Text(
                        text = "Found ${providers.size} providers near you",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                    )
                }
            }

            // List view toggle button
            FloatingActionButton(
                onClick = {
                    // Navigate to category details screen with list view
                    navController.navigate(Screen.CategoryDetails.createRoute(categoryId))
                },
                containerColor = SafaricomGreen,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ViewList,
                    contentDescription = "List View"
                )
            }

            // Location button - refresh user location
            FloatingActionButton(
                onClick = {
                    viewModel.fetchUserLocation()
                },
                containerColor = Color.White,
                contentColor = SafaricomGreen,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "My Location"
                )
            }
        }
    }

    // Provider details bottom sheet
    if (showBottomSheet && selectedProvider != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                selectedProvider = null
            },
            sheetState = bottomSheetState
        ) {
            ProviderDetailsCard(
                providerWithDistance = selectedProvider!!,
                onViewDetails = {
                    navController.navigate(
                        Screen.ProviderDetails.createRoute(selectedProvider!!.provider.userId)
                    )
                    showBottomSheet = false
                },
                onBookService = {
                    // Navigate to booking screen
                    // This would navigate to the service selection screen first
                    navController.navigate(
                        Screen.ProviderDetails.createRoute(selectedProvider!!.provider.userId)
                    )
                    showBottomSheet = false
                }
            )
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
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = SafaricomGreen,
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

@Composable
fun ProviderDetailsCard(
    providerWithDistance: ServiceProviderWithDistance,
    onViewDetails: () -> Unit,
    onBookService: () -> Unit
) {
    val provider = providerWithDistance.provider

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Provider profile image/icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(SafaricomGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = provider.businessName.first().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = SafaricomGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = provider.businessName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (provider.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = "Verified",
                            tint = SafaricomGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Rating display
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107), // Amber color for stars
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${provider.rating} (${provider.reviewCount} reviews)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Provider description
        Text(
            text = provider.description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Distance and availability indicator
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (provider.isAvailable) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = null,
                tint = if (provider.isAvailable) SafaricomGreen else Color.Red,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = if (provider.isAvailable) "Available now" else "Not available",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = providerWithDistance.distanceText,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = { /* TODO: Call provider */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text("Call")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onBookService,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SafaricomGreen
                )
            ) {
                Text("Book Now")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // View details button
        OutlinedButton(
            onClick = onViewDetails,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = SafaricomGreen
            )
        ) {
            Text("View Details")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Helper function to generate random nearby locations for demonstration
private fun getRandomNearbyLocation(center: LatLng): LatLng {
    val radius = 0.02 // Approximately 2 km
    val x0 = center.longitude
    val y0 = center.latitude

    // Random angle
    val randomAngle = Math.random() * 2 * Math.PI

    // Random radius (using sqrt for uniform distribution)
    val randomRadius = Math.sqrt(Math.random()) * radius

    // Calculate coordinates
    val x = x0 + randomRadius * Math.cos(randomAngle)
    val y = y0 + randomRadius * Math.sin(randomAngle)

    return LatLng(y, x)
}