package com.damiens.mjoba.ui.theme.Screens.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.widget.Toast
import androidx.compose.material.icons.filled.Code
import androidx.compose.ui.platform.LocalContext
import com.damiens.mjoba.ViewModel.CategoryViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.damiens.mjoba.Model.User
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.R
import com.damiens.mjoba.ViewModel.AuthViewModel
import com.damiens.mjoba.ui.theme.SafaricomGreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavHostController,
    userId: String? = null, // If null, show current user's profile
    authViewModel: AuthViewModel = viewModel()
) {
    // Get current user from ViewModel
    val currentUser by authViewModel.currentUser.collectAsState()

    // State for developer options dialog
    var showDevOptionsDialog by remember { mutableStateOf(false) }

    // This would eventually come from Firebase
    val user = currentUser ?: getSampleUser(userId)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.EditProfile.createRoute()) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile"
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
                .verticalScroll(scrollState)
        ) {
            // Profile Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile picture (now using AsyncImage)
                    if (user.profileImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.profileImageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Profile Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    } else {
                        // Fallback to app logo or initials if no profile image
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = R.drawable.mjoblogo,
                                contentDescription = "Profile Logo",
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // User name
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    // User role
                    if (user.isServiceProvider) {
                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            color = SafaricomGreen,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "Service Provider",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Member since
                    val memberSince = "Member since ${formatDate(user.dateCreated)}"
                    Text(
                        text = memberSince,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Profile Information Section
            ProfileSection(
                title = "Contact Information",
                items = listOf(
                    ProfileItem(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = user.email
                    ),
                    ProfileItem(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = user.phone
                    ),
                    ProfileItem(
                        icon = Icons.Default.LocationOn,
                        label = "Address",
                        value = user.address.ifEmpty { "Not specified" }
                    )
                )
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // App Settings Section
            ProfileSection(
                title = "App Settings",
                items = listOf(
                    ProfileItem(
                        icon = Icons.Default.Notifications,
                        label = "Notifications",
                        value = "On",
                        clickable = true,
                        onClick = { /* TODO: Navigate to notifications settings */ }
                    ),
                    ProfileItem(
                        icon = Icons.Default.Language,
                        label = "Language",
                        value = "English",
                        clickable = true,
                        onClick = { /* TODO: Navigate to language settings */ }
                    ),
                    ProfileItem(
                        icon = Icons.Default.Security,
                        label = "Security",
                        value = "",
                        clickable = true,
                        onClick = { /* TODO: Navigate to security settings */ }
                    )
                )
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // Bookings Section
            ListItem(
                headlineContent = { Text("My Bookings") },
                supportingContent = { Text("View your current and past bookings") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null
                    )
                },
                trailingContent = {
                    IconButton(onClick = { navController.navigate(Screen.Bookings.route) }) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "View Bookings"
                        )
                    }
                },
                modifier = Modifier.clickable { navController.navigate(Screen.Bookings.route) }
            )


            // If the user is a service provider, show dashboard option
            if (user.isServiceProvider) {
                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                ListItem(
                    headlineContent = { Text("Provider Dashboard") },
                    supportingContent = { Text("Manage your services and bookings") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = {
                            // Navigate to provider dashboard
                            navController.navigate(Screen.ProviderDashboard.route)
                        }) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "View Dashboard"
                            )
                        }
                    },
                    modifier = Modifier.clickable {
                        // Navigate to the provider dashboard
                        navController.navigate(Screen.ProviderDashboard.route)
                    }
                )
            }
            else {
                // Option to become a provider for regular users
                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                ListItem(
                    headlineContent = { Text("Become a Service Provider") },
                    supportingContent = { Text("Offer your services and earn money") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.WorkOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = {
                            // Update user to be a service provider
                            authViewModel.updateUserProfile(isServiceProvider = true)
                            // Navigate to provider dashboard
                            navController.navigate(Screen.ProviderDashboard.route)
                        }) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Become Provider"
                            )
                        }
                    },
                    modifier = Modifier.clickable {
                        // Update user to be a service provider
                        authViewModel.updateUserProfile(isServiceProvider = true)
                        // Navigate to provider dashboard
                        navController.navigate(Screen.ProviderDashboard.route)
                    }
                )
            }
            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // Help & Support
            ListItem(
                headlineContent = { Text("Help & Support") },
                supportingContent = { Text("Get help with the app") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = null
                    )
                },
                trailingContent = {
                    IconButton(onClick = { /* TODO: Navigate to help screen */ }) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Get Help"
                        )
                    }
                },
                modifier = Modifier.clickable { /* TODO: Navigate to help screen */ }
            )

            // About
            ListItem(
                headlineContent = { Text("About M-Joba") },
                supportingContent = { Text("Version 1.0.0") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null
                    )
                },
                trailingContent = {
                    IconButton(onClick = {
                        // Display an about dialog or navigate to about screen
                        // For now, we'll just show a dialog through state
                    }) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "About"
                        )
                    }
                },
                modifier = Modifier.clickable {
                    // For demonstration, we could navigate to an about screen
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Developer Options (only in debug)
            val isDebugMode = true // Set to false before production release

            if (isDebugMode) {
                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                ListItem(
                    headlineContent = { Text("Developer Options") },
                    supportingContent = { Text("Debug tools and settings") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = {
                            // Show developer options dialog
                            showDevOptionsDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Dev Options"
                            )
                        }
                    },
                    modifier = Modifier.clickable {
                        // Show developer options dialog
                        showDevOptionsDialog = true
                    }
                )
            }

            // Logout button
            OutlinedButton(
                onClick = {
                    // Call the logout function from AuthViewModel
                    authViewModel.logout()
                    // Navigate back to login screen
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Logout")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Developer Options Dialog - outside the Column but inside the Scaffold padding
        if (showDevOptionsDialog) {
            val categoryViewModel: CategoryViewModel = viewModel()
            val context = LocalContext.current

            AlertDialog(
                onDismissRequest = { showDevOptionsDialog = false },
                title = { Text("Developer Options") },
                text = {
                    Column {
                        Text("These options are for development and testing purposes only.")

                        Spacer(modifier = Modifier.height(16.dp))

                        // First button - Reset and Initialize Categories
                        Button(
                            onClick = {
                                categoryViewModel.resetAndInitializeCategories()
                                Toast.makeText(context, "Initializing default categories...", Toast.LENGTH_SHORT).show()
                                showDevOptionsDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Reset and Initialize Categories")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Second button - Add Missing Categories
                        Button(
                            onClick = {
                                categoryViewModel.initializeAllCategories() // Use the new function here
                                Toast.makeText(context, "Adding missing categories...", Toast.LENGTH_SHORT).show()
                                showDevOptionsDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add Missing Categories")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Third button - Enable Provider Mode
                        Button(
                            onClick = {
                                authViewModel.updateUserProfile(isServiceProvider = true)
                                Toast.makeText(context, "Enabled service provider mode", Toast.LENGTH_SHORT).show()
                                showDevOptionsDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Enable Provider Mode")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDevOptionsDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileSection(
    title: String,
    items: List<ProfileItem>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        items.forEach { item ->
            ProfileItemRow(item = item)
        }
    }
}

@Composable
fun ProfileItemRow(
    item: ProfileItem
) {
    val rowModifier = if (item.clickable) {
        Modifier
            .fillMaxWidth()
            .clickable { item.onClick?.invoke() }
            .padding(vertical = 12.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    }

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            if (item.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.value,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (item.clickable) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class ProfileItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val value: String,
    val clickable: Boolean = false,
    val onClick: (() -> Unit)? = null
)

// Helper function to format date
private fun formatDate(timestamp: Long): String {
    val date = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.getDefault())
        .format(java.util.Date(timestamp))
    return date
}

// Sample data function - in a real app, this would come from a repository
private fun getSampleUser(userId: String?): User {
    return User(
        id = userId ?: "u123",
        name = "Damien padri",
        email = "damien.padri@example.com",
        phone = "+254 712 345 678",
        profileImageUrl = "",
        address = "123 Main St, Nairobi",
        isServiceProvider = true,
        dateCreated = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000) // 30 days ago
    )
}