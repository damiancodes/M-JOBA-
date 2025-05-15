package com.damiens.mjoba.ui.theme.Screens.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.ViewModel.ServiceProviderViewModel
import com.damiens.mjoba.ViewModel.ProvidersUiState
import com.damiens.mjoba.ui.theme.SafaricomGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProvidersScreen(
    navController: NavHostController,
    serviceProviderViewModel: ServiceProviderViewModel = viewModel()
) {
    // State
    val providersState by serviceProviderViewModel.providersState.collectAsState()
    var filterVerified by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Load service providers when the screen is first displayed
    LaunchedEffect(Unit) {
        serviceProviderViewModel.getAllProviders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Providers") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Filter button
                    IconButton(onClick = { filterVerified = !filterVerified }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
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
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    if (it.isNotEmpty()) {
                        serviceProviderViewModel.searchProviders(it)
                    } else {
                        serviceProviderViewModel.getAllProviders()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search providers") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            serviceProviderViewModel.getAllProviders()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                singleLine = true
            )

            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = !filterVerified,
                    onClick = { filterVerified = false },
                    label = { Text("All") }
                )

                FilterChip(
                    selected = filterVerified,
                    onClick = { filterVerified = true },
                    label = { Text("Verified Only") }
                )
            }

            when (providersState) {
                is ProvidersUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SafaricomGreen)
                    }
                }

                is ProvidersUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = (providersState as ProvidersUiState.Error).message,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { serviceProviderViewModel.getAllProviders() }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                is ProvidersUiState.Success -> {
                    val providers = (providersState as ProvidersUiState.Success).providers

                    // Apply filters
                    val filteredProviders = if (filterVerified) {
                        providers.filter { it.isVerified }
                    } else {
                        providers
                    }

                    if (filteredProviders.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchQuery.isNotEmpty())
                                    "No providers found matching '$searchQuery'"
                                else if (filterVerified)
                                    "No verified providers found"
                                else
                                    "No providers found",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredProviders) { provider ->
                                ProviderListItem(
                                    provider = provider,
                                    onVerifyClick = {
                                        // Handle verification toggle
                                        serviceProviderViewModel.updateVerificationStatus(
                                            providerId = provider.userId,
                                            isVerified = !provider.isVerified
                                        )
                                    },
                                    onViewDetailsClick = {
                                        navController.navigate(
                                            "provider_details/${provider.userId}"
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                else -> {
                    // Initial state or other states
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Loading providers...")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderListItem(
    provider: ServiceProvider,
    onVerifyClick: () -> Unit,
    onViewDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onViewDetailsClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Provider avatar/icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(SafaricomGreen.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (provider.coverImageUrl.isNotEmpty()) {
                        // Use Coil or other image loading library here
                        // Image goes here
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            tint = SafaricomGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            tint = SafaricomGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Provider details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = provider.businessName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = provider.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Rating and review count if available
                    if (provider.reviewCount > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
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

                // Verification status
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (provider.isVerified) "Verified" else "Unverified",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (provider.isVerified)
                                Color(0xFF4CAF50) else
                                MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = if (provider.isVerified)
                                Icons.Default.Verified else
                                Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (provider.isVerified)
                                Color(0xFF4CAF50) else
                                MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Verify/Unverify button
                    Button(
                        onClick = onVerifyClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (provider.isVerified)
                                MaterialTheme.colorScheme.error else
                                SafaricomGreen
                        ),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = if (provider.isVerified) "Unverify" else "Verify",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

