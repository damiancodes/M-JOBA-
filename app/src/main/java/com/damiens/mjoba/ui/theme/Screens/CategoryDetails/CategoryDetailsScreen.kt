//package com.damiens.mjoba.ui.theme.Screens.CategoryDetails
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import com.damiens.mjoba.Model.Service
//import com.damiens.mjoba.Model.ServiceCategory
//import com.damiens.mjoba.Navigation.Screen
//import com.damiens.mjoba.ViewModel.CategoryViewModel
//import com.damiens.mjoba.ViewModel.CategoriesUiState
//import com.damiens.mjoba.ViewModel.ServiceViewModel
//import com.damiens.mjoba.ViewModel.ServicesUiState
//import com.damiens.mjoba.ui.theme.SafaricomGreen
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CategoryDetailsScreen(
//    navController: NavHostController,
//    categoryId: String,
//    categoryViewModel: CategoryViewModel = viewModel(),
//    serviceViewModel: ServiceViewModel = viewModel()
//) {
//    // Load category and services data
//    LaunchedEffect(categoryId) {
//        categoryViewModel.loadCategories()
//        serviceViewModel.loadServicesByCategory(categoryId)
//    }
//    // Add this line near your other state collections
//    val categoriesState by categoryViewModel.categoriesState.collectAsState()
//
//    // Get category state from ViewModel
//    val selectedCategoryState by categoryViewModel.selectedCategoryState.collectAsState()
//
//// Get category information
//    var category by remember { mutableStateOf<ServiceCategory?>(null) }
//
//// Process category data when state changes
//    LaunchedEffect(categoryId) {
//        categoryViewModel.getCategoryById(categoryId)
//        serviceViewModel.loadServicesByCategory(categoryId)
//    }
//
//// Update category when state changes
//    LaunchedEffect(selectedCategoryState) {
//        when (selectedCategoryState) {
//            is CategoryViewModel.SelectedCategoryState.Success -> {
//                category = (selectedCategoryState as CategoryViewModel.SelectedCategoryState.Success).category
//            }
//            else -> {
//                // Handle other states if needed
//            }
//        }
//    }
//
//    // Get services state from ViewModel
//    val servicesState by serviceViewModel.servicesState.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = category?.name ?: "Category Details") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Go back"
//                        )
//                    }
//                },
//                actions = {
//                    // Add map view button
//                    IconButton(
//                        onClick = {
//                            navController.navigate(Screen.NearbyProviders.createRoute(categoryId))
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Map,
//                            contentDescription = "Map View"
//                        )
//                    }
//
//                    // Add filter button
//                    IconButton(onClick = { /* TODO: Implement filtering */ }) {
//                        Icon(
//                            imageVector = Icons.Default.FilterList,
//                            contentDescription = "Filter"
//                        )
//                    }
//                }
//            )
//        },
//        floatingActionButton = {
//            // Add toggle view FAB
//            FloatingActionButton(
//                onClick = { /* TODO: Toggle between list and grid view */ },
//                containerColor = SafaricomGreen,
//                contentColor = MaterialTheme.colorScheme.onPrimary
//            ) {
//                Icon(Icons.Default.ViewList, "Toggle View")
//            }
//        }
//    ) { paddingValues ->
//        when {
//            // Show loading state
//            categoriesState is CategoriesUiState.Loading || servicesState is ServicesUiState.Loading -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(color = SafaricomGreen)
//                }
//            }
//
//            // Show error state
//            categoriesState is CategoriesUiState.Error || servicesState is ServicesUiState.Error -> {
//                val errorMessage = when {
//                    categoriesState is CategoriesUiState.Error -> (categoriesState as CategoriesUiState.Error).message
//                    servicesState is ServicesUiState.Error -> (servicesState as ServicesUiState.Error).message
//                    else -> "An error occurred"
//                }
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center,
//                        modifier = Modifier.padding(horizontal = 16.dp)
//                    ) {
//                        Text(
//                            text = errorMessage,
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = MaterialTheme.colorScheme.error
//                        )
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Button(
//                            onClick = {
//                                categoryViewModel.loadCategories()
//                                serviceViewModel.loadServicesByCategory(categoryId)
//                            }
//                        ) {
//                            Text("Retry")
//                        }
//                    }
//                }
//            }
//
//            // Show content when data is available
//            categoriesState is CategoriesUiState.Success && servicesState is ServicesUiState.Success -> {
//                // Get services from success state
//                val services = if (servicesState is ServicesUiState.Success) {
//                    (servicesState as ServicesUiState.Success).services
//                } else {
//                    emptyList()
//                }
//
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues)
//                        .padding(16.dp)
//                ) {
//                    // Category description
//                    Text(
//                        text = category?.description ?: "Explore services in this category",
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier.padding(bottom = 16.dp)
//                    )
//
//                    // Services in this category
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = "Available Providers",
//                            style = MaterialTheme.typography.titleLarge,
//                            fontWeight = FontWeight.Bold
//                        )
//
//                        Text(
//                            text = "${services.size} providers",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    if (services.isEmpty()) {
//                        // Show message when no services are available
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(200.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.Center
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.Info,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(48.dp),
//                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
//                                )
//
//                                Spacer(modifier = Modifier.height(16.dp))
//
//                                Text(
//                                    text = "No services available in this category yet",
//                                    style = MaterialTheme.typography.bodyLarge,
//                                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                                )
//                            }
//                        }
//                    } else {
//                        // Display services in a grid
//                        LazyVerticalGrid(
//                            columns = GridCells.Fixed(2),
//                            horizontalArrangement = Arrangement.spacedBy(12.dp),
//                            verticalArrangement = Arrangement.spacedBy(12.dp),
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            items(services) { service ->
//                                ServiceProviderCard(
//                                    service = service,
//                                    onClick = {
//                                        navController.navigate(Screen.ProviderDetails.createRoute(service.providerId))
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ServiceProviderCard(
//    service: Service,
//    onClick: () -> Unit
//) {
//    Card(
//        onClick = onClick,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(180.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(12.dp)
//        ) {
//            // This would be an actual image in a real app
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(90.dp)
//                    .padding(bottom = 8.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Surface(
//                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    // Placeholder for image
//                    Text(
//                        text = service.name.first().toString(),
//                        style = MaterialTheme.typography.headlineMedium,
//                        color = SafaricomGreen,
//                        modifier = Modifier.padding(4.dp)
//                    )
//                }
//            }
//
//            Text(
//                text = service.name,
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Star,
//                    contentDescription = null,
//                    modifier = Modifier.size(16.dp),
//                    tint = MaterialTheme.colorScheme.secondary
//                )
//
//                Text(
//                    text = "4.5",  // This would come from the service provider's rating
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(start = 4.dp)
//                )
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                Icon(
//                    imageVector = Icons.Default.LocationOn,
//                    contentDescription = null,
//                    modifier = Modifier.size(16.dp),
//                    tint = MaterialTheme.colorScheme.secondary
//                )
//
//                Text(
//                    text = "2.3 km",  // This would be calculated distance
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(start = 4.dp)
//                )
//            }
//
//            Text(
//                text = "Ksh ${service.price.toInt()}",
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Bold,
//                color = SafaricomGreen
//            )
//        }
//    }
//}

package com.damiens.mjoba.ui.theme.Screens.CategoryDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceCategory
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.ViewModel.CategoryViewModel
import com.damiens.mjoba.ViewModel.CategoriesUiState
import com.damiens.mjoba.ViewModel.ServiceViewModel
import com.damiens.mjoba.ViewModel.ServicesUiState
import com.damiens.mjoba.ui.theme.SafaricomGreen
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailsScreen(
    navController: NavHostController,
    categoryId: String,
    categoryViewModel: CategoryViewModel = viewModel(),
    serviceViewModel: ServiceViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Load category and services data
    LaunchedEffect(categoryId) {
        Log.d("CategoryDetailsScreen", "Loading data for category: $categoryId")
        categoryViewModel.loadCategories()
        categoryViewModel.getCategoryById(categoryId)
        serviceViewModel.loadServicesByCategory(categoryId)

        // Attempt to generate mock services if none exist
        scope.launch {
            val userId = "demo_provider_${categoryId}" // Create a demo provider ID related to category
            serviceViewModel.populateServicesByCategory(categoryId, userId)
        }
    }

    // Add this line near your other state collections
    val categoriesState by categoryViewModel.categoriesState.collectAsState()

    // Get category state from ViewModel
    val selectedCategoryState by categoryViewModel.selectedCategoryState.collectAsState()

    // Get category information
    var category by remember { mutableStateOf<ServiceCategory?>(null) }

    // Update category when state changes
    LaunchedEffect(selectedCategoryState) {
        when (selectedCategoryState) {
            is CategoryViewModel.SelectedCategoryState.Success -> {
                category = (selectedCategoryState as CategoryViewModel.SelectedCategoryState.Success).category
                Log.d("CategoryDetailsScreen", "Category loaded: ${category?.name}")
            }
            is CategoryViewModel.SelectedCategoryState.Error -> {
                Log.e("CategoryDetailsScreen", "Error loading category: ${(selectedCategoryState as CategoryViewModel.SelectedCategoryState.Error).message}")
            }
            else -> {
                // Handle other states if needed
                Log.d("CategoryDetailsScreen", "Category state: $selectedCategoryState")
            }
        }
    }

    // Get services state from ViewModel
    val servicesState by serviceViewModel.servicesState.collectAsState()

    // Monitor services state
    LaunchedEffect(servicesState) {
        when (servicesState) {
            is ServicesUiState.Loading -> {
                Log.d("CategoryDetailsScreen", "Services are loading...")
            }
            is ServicesUiState.Success -> {
                val services = (servicesState as ServicesUiState.Success).services
                Log.d("CategoryDetailsScreen", "Services loaded: ${services.size}")

                if (services.isEmpty()) {
                    Log.d("CategoryDetailsScreen", "No services found, attempting to populate demo services")
                    // If no services are found, create some demo data
                    val userId = "demo_provider_${categoryId}"
                    serviceViewModel.populateServicesByCategory(categoryId, userId)
                }
            }
            is ServicesUiState.Error -> {
                Log.e("CategoryDetailsScreen", "Error loading services: ${(servicesState as ServicesUiState.Error).message}")
            }
            else -> {
                // No action for other states
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = category?.name ?: "Category Details") },
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
        when {
            // Show loading state
            categoriesState is CategoriesUiState.Loading || servicesState is ServicesUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SafaricomGreen)
                }
            }

            // Show error state
            categoriesState is CategoriesUiState.Error || servicesState is ServicesUiState.Error -> {
                val errorMessage = when {
                    categoriesState is CategoriesUiState.Error -> (categoriesState as CategoriesUiState.Error).message
                    servicesState is ServicesUiState.Error -> (servicesState as ServicesUiState.Error).message
                    else -> "An error occurred"
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                categoryViewModel.loadCategories()
                                serviceViewModel.loadServicesByCategory(categoryId)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SafaricomGreen
                            )
                        ) {
                            Text("Retry")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Add demo data button
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    val userId = "demo_provider_${categoryId}"
                                    serviceViewModel.populateServicesByCategory(categoryId, userId)
                                    Toast.makeText(context, "Creating demo services...", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text("Create Demo Services")
                        }
                    }
                }
            }

            // Show content when data is available
            categoriesState is CategoriesUiState.Success && servicesState is ServicesUiState.Success -> {
                // Get services from success state
                val services = if (servicesState is ServicesUiState.Success) {
                    (servicesState as ServicesUiState.Success).services
                } else {
                    emptyList()
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    // Category description
                    Text(
                        text = category?.description ?: "Explore services in this category",
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
                            text = "Available Services",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${services.size} services",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (services.isEmpty()) {
                        // Show message when no services are available and offer to create demo services
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "No services available in this category yet",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        scope.launch {
                                            val userId = "demo_provider_${categoryId}"
                                            serviceViewModel.populateServicesByCategory(categoryId, userId)
                                            Toast.makeText(context, "Creating demo services...", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SafaricomGreen
                                    )
                                ) {
                                    Text("Create Demo Services")
                                }
                            }
                        }
                    } else {
                        // Display services in a grid
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(services) { service ->
                                ServiceCard(
                                    service = service,
                                    onClick = {
                                        // Instead of navigating to provider details, navigate to booking screen
                                        navController.navigate(Screen.BookService.createRoute(service.id))
                                    }
                                )
                            }
                        }
                    }
                }
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
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
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
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SafaricomGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for image
                Text(
                    text = service.name.first().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = SafaricomGreen,
                    modifier = Modifier.padding(4.dp)
                )
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
                    tint = Color(0xFFFFC107) // Amber color for stars
                )

                Text(
                    text = "4.5",  // This would come from the service provider's rating
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = "${service.estimatedDuration} min",
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

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SafaricomGreen
                )
            ) {
                Text("Book Now", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}