package com.damiens.mjoba.ui.theme.Screens.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.ViewModel.AuthViewModel
import com.damiens.mjoba.ui.theme.SafaricomGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    // Get current user
    val currentUser by authViewModel.currentUser.collectAsState()

    // Theme colors
    val darkBackground = Color(0xFF121212)
    val darkCard = Color(0xFF2C2C2C)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkBackground,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = darkBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Admin welcome
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = darkCard
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Welcome, Admin",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Manage all aspects of your M-Joba platform",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Admin actions
            item {
                Text(
                    text = "MANAGEMENT",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                )
            }

            // Management options
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = darkCard
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Categories Management
                        AdminActionItem(
                            title = "Manage Categories",
                            description = "Add, edit, or delete service categories",
                            icon = Icons.Default.Category,
                            onClick = {
                                navController.navigate(Screen.AdminCategories.route)
                            }
                        )

                        Divider(color = darkBackground.copy(alpha = 0.5f))

                        // Services Management
                        AdminActionItem(
                            title = "Manage Services",
                            description = "Review and manage service listings",
                            icon = Icons.Default.Handyman,
                            onClick = {
                                navController.navigate(Screen.AdminServices.route)
                            }
                        )

                        Divider(color = darkBackground.copy(alpha = 0.5f))

                        // Provider Management
                        AdminActionItem(
                            title = "Manage Providers",
                            description = "Approve and manage service providers",
                            icon = Icons.Default.PersonSearch,
                            onClick = {
                                navController.navigate(Screen.AdminProviders.route)
                            }
                        )
                    }
                }
            }

            // System section
            item {
                Text(
                    text = "SYSTEM",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                )
            }

            // System options
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = darkCard
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Analytics
                        AdminActionItem(
                            title = "Analytics",
                            description = "View platform statistics and metrics",
                            icon = Icons.Default.BarChart,
                            onClick = {
                                // Navigate to analytics screen
                            }
                        )

                        Divider(color = darkBackground.copy(alpha = 0.5f))

                        // Settings
                        AdminActionItem(
                            title = "System Settings",
                            description = "Configure platform behavior",
                            icon = Icons.Default.Settings,
                            onClick = {
                                // Navigate to settings screen
                            }
                        )

                        Divider(color = darkBackground.copy(alpha = 0.5f))

                        // Initialize Data
                        AdminActionItem(
                            title = "Initialize Data",
                            description = "Reset or populate system data",
                            icon = Icons.Default.Refresh,
                            onClick = {
                                // Show dialog to confirm data initialization
                                // Call categoryViewModel.initializeAllCategories()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminActionItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SafaricomGreen,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}