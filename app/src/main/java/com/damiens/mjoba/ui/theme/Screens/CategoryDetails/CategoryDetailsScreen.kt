package com.damiens.mjoba.ui.theme.Screens.CategoryDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceCategory
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.ui.theme.SafaricomGreen
import com.damiens.mjoba.util.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailsScreen(
    navController: NavHostController,
    categoryId: String
) {
    // Get data from SampleData utility class
    val category = remember { SampleData.getCategory(categoryId) }
    val providers = remember { SampleData.getServiceProvidersByCategory(categoryId) }

    // Create a list of services from all providers in this category
    val services = remember {
        providers.flatMap { provider ->
            provider.services.filter { it.categoryId == categoryId }
        }.ifEmpty {
            // Fallback to sample services if no services found in providers
            getSampleServices(categoryId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = category.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    // Add map view button
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.NearbyProviders.createRoute(categoryId))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map View"
                        )
                    }

                    // Add filter button
                    IconButton(onClick = { /* TODO: Implement filtering */ }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            // Add toggle view FAB
            FloatingActionButton(
                onClick = { /* TODO: Toggle between list and grid view */ },
                containerColor = SafaricomGreen,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.ViewList, "Toggle View")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Category description
            Text(
                text = category.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Services in this category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Available Providers",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${services.size} providers",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(services) { service ->
                    ServiceProviderCard(
                        service = service,
                        onClick = {
                            navController.navigate(Screen.ProviderDetails.createRoute(service.providerId))
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceProviderCard(
    service: Service,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // This would be an actual image in a real app
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Placeholder for image
                    Text(
                        text = service.name.first().toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = SafaricomGreen,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            Text(
                text = service.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = "4.5",  // This would come from the service provider's rating
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = "2.3 km",  // This would be calculated distance
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Text(
                text = "Ksh ${service.price.toInt()}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = SafaricomGreen
            )
        }
    }
}

// Sample data function for fallback - in a real app, this would come from a repository
private fun getSampleServices(categoryId: String): List<Service> {
    return when (categoryId) {
        "101" -> listOf(
            Service(id = "1001", categoryId = "101", providerId = "2001", name = "Basic House Cleaning", description = "General cleaning of your home", price = 1500.0),
            Service(id = "1002", categoryId = "101", providerId = "2002", name = "Deep Cleaning", description = "Thorough cleaning of all surfaces", price = 3000.0),
            Service(id = "1003", categoryId = "101", providerId = "2003", name = "Move-in/Move-out Cleaning", description = "Prepare your home for moving", price = 4500.0),
            Service(id = "1004", categoryId = "101", providerId = "2004", name = "Office Cleaning", description = "Professional cleaning for offices", price = 2500.0)
        )
        "201" -> listOf(
            Service(id = "2001", categoryId = "201", providerId = "3001", name = "Hair Styling", description = "Professional styling for all occasions", price = 1000.0),
            Service(id = "2002", categoryId = "201", providerId = "3002", name = "Hair Coloring", description = "Full coloring and highlights", price = 2500.0),
            Service(id = "2003", categoryId = "201", providerId = "3003", name = "Hair Treatment", description = "Treatments for damaged hair", price = 1800.0),
            Service(id = "2004", categoryId = "201", providerId = "3004", name = "Hair Extensions", description = "Quality hair extensions", price = 3500.0)
        )
        // Add more categories as needed
        else -> listOf(
            Service(id = "9001", categoryId = categoryId, providerId = "9001", name = "Service 1", description = "Service description", price = 1000.0),
            Service(id = "9002", categoryId = categoryId, providerId = "9002", name = "Service 2", description = "Service description", price = 1500.0),
            Service(id = "9003", categoryId = categoryId, providerId = "9003", name = "Service 3", description = "Service description", price = 2000.0),
            Service(id = "9004", categoryId = categoryId, providerId = "9004", name = "Service 4", description = "Service description", price = 2500.0)
        )
    }
}