package com.damiens.mjoba.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Data.AppLocationRepository
import com.damiens.mjoba.Data.LocationRepository
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.Navigation.Screen
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.VerifiedUser
import com.damiens.mjoba.ui.theme.SafaricomGreen
import com.damiens.mjoba.util.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceProviderDetailsScreen(
    navController: NavHostController,
    providerId: String
) {
    // This would come from a ViewModel in a real app
    val provider = getSampleProvider(providerId)
    val services = getSampleProviderServices(providerId)

    // Get location data from AppLocationRepository
    val selectedLocation by AppLocationRepository.selectedLocation.collectAsState()

    // Context for location repository
    val context = LocalContext.current
    val locationRepository = remember { LocationRepository(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = provider.businessName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Add to favorites */ }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Add to favorites"
                        )
                    }
                    IconButton(onClick = { /* TODO: Share provider */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Provider Header with Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    // This would be an actual image in a real app
                    Text(
                        text = "Provider Cover Image",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            // Provider Info
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Verification Badge
                    if (provider.isVerified) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VerifiedUser,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Verified Provider",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }

                    // Business Name
                    Text(
                        text = provider.businessName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < provider.rating.toInt())
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "${provider.rating} (${provider.reviewCount} reviews)",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Location Card - Updated with AppLocationRepository
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = SafaricomGreen
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "Your Location",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                TextButton(
                                    onClick = { navController.navigate(Screen.LocationSelection.createRoute()) }
                                ) {
                                    Text("Change", color = SafaricomGreen)
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Display current location or prompt to set one
                            selectedLocation?.let { location ->
                                Text(
                                    text = location.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = location.address,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                // Calculate and show distance if provider location is available
                                provider.location?.let { providerLocation ->
                                    val distance = locationRepository.calculateDistance(
                                        location.geoPoint,
                                        providerLocation
                                    )
                                    val distanceText = locationRepository.getDistanceText(distance)

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.DirectionsCar,
                                            contentDescription = null,
                                            tint = SafaricomGreen,
                                            modifier = Modifier.size(16.dp)
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = distanceText,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium,
                                            color = SafaricomGreen
                                        )
                                    }
                                }
                            } ?: run {
                                Text(
                                    text = "Location not set",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = "Set your location to see distance",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    // Description
                    Text(
                        text = provider.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                }
            }

            // Services Offered
            item {
                Text(
                    text = "Services Offered",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    services.forEach { service ->
                        ServiceCard(
                            service = service,
                            onClick = {
                                navController.navigate(Screen.BookService.createRoute(service.id))
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // Gallery
            item {
                if (provider.galleryImages.isNotEmpty()) {
                    Text(
                        text = "Gallery",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(provider.galleryImages) { imageUrl ->
                            // This would be an actual image in a real app
                            Surface(
                                modifier = Modifier
                                    .size(120.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "Gallery Image",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Reviews
            item {
                Text(
                    text = "Customer Reviews",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // Sample reviews
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Great service! Very professional and on time.",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Text(
                            text = "John Doe - 2 days ago",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Book Service Button
            item {
                Button(
                    onClick = {
                        // Navigate to booking screen for the first service
                        if (services.isNotEmpty()) {
                            navController.navigate(Screen.BookService.createRoute(services.first().id))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Book Service")
                }

                // Contact Provider Button
                OutlinedButton(
                    onClick = { /* TODO: Implement contact provider */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Contact Provider")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCard(
    service: Service,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = service.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Duration: ~${service.estimatedDuration} mins",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Ksh ${service.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(text = "Book")
                }
            }
        }
    }
}

// Sample data functions - in a real app, this would come from a repository
private fun getSampleProvider(providerId: String): ServiceProvider {
    val sampleGalleryImages = listOf("img1", "img2", "img3", "img4", "img5")

    return when (providerId) {
        "2001" -> ServiceProvider(
            userId = "u2001",
            businessName = "CleanHome Services",
            serviceCategories = listOf("101"),
            description = "We provide professional cleaning services for homes, offices and commercial spaces. Our trained staff ensures quality work with attention to detail.",
            rating = 4.8f,
            reviewCount = 124,
            isVerified = true,
            isAvailable = true,
            galleryImages = sampleGalleryImages
        )
        "3001" -> ServiceProvider(
            userId = "u3001",
            businessName = "Glamour Hair Salon",
            serviceCategories = listOf("201"),
            description = "Professional hair styling for all occasions. We offer cutting, coloring, styling, and treatments using quality products.",
            rating = 4.6f,
            reviewCount = 78,
            isVerified = true,
            isAvailable = true,
            galleryImages = sampleGalleryImages
        )
        else -> ServiceProvider(
            userId = "u${providerId.substring(1)}",
            businessName = "Service Provider $providerId",
            serviceCategories = listOf("101", "201", "301"),
            description = "Professional service provider with years of experience.",
            rating = 4.0f,
            reviewCount = 50,
            isVerified = true,
            isAvailable = true,
            galleryImages = sampleGalleryImages
        )
    }
}

private fun getSampleProviderServices(providerId: String): List<Service> {
    return when (providerId) {
        "2001" -> listOf(
            Service(id = "1001", categoryId = "101", providerId = "2001", name = "Basic House Cleaning", description = "General cleaning of your home", price = 1500.0, estimatedDuration = 120),
            Service(id = "1002", categoryId = "101", providerId = "2001", name = "Deep Cleaning", description = "Thorough cleaning of all surfaces", price = 3000.0, estimatedDuration = 240),
            Service(id = "1003", categoryId = "101", providerId = "2001", name = "Move-in/Move-out Cleaning", description = "Prepare your home for moving", price = 4500.0, estimatedDuration = 300),
            Service(id = "1004", categoryId = "101", providerId = "2001", name = "Office Cleaning", description = "Professional cleaning for offices", price = 2500.0, estimatedDuration = 180)
        )
        "3001" -> listOf(
            Service(id = "2001", categoryId = "201", providerId = "3001", name = "Hair Styling", description = "Professional styling for all occasions", price = 1000.0, estimatedDuration = 60),
            Service(id = "2002", categoryId = "201", providerId = "3001", name = "Hair Coloring", description = "Full coloring and highlights", price = 2500.0, estimatedDuration = 120),
            Service(id = "2003", categoryId = "201", providerId = "3001", name = "Hair Treatment", description = "Treatments for damaged hair", price = 1800.0, estimatedDuration = 90),
            Service(id = "2004", categoryId = "201", providerId = "3001", name = "Hair Extensions", description = "Quality hair extensions", price = 3500.0, estimatedDuration = 150)
        )
        else -> listOf(
            Service(id = "9001", categoryId = "101", providerId = providerId, name = "Service 1", description = "Service description", price = 1000.0, estimatedDuration = 60),
            Service(id = "9002", categoryId = "101", providerId = providerId, name = "Service 2", description = "Service description", price = 1500.0, estimatedDuration = 90),
            Service(id = "9003", categoryId = "101", providerId = providerId, name = "Service 3", description = "Service description", price = 2000.0, estimatedDuration = 120)
        )
    }
}