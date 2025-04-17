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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.User
import com.damiens.mjoba.Navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavHostController,
    userId: String? = null // If null, show current user's profile
) {
    // This would come from a ViewModel in a real app
    val user = getSampleUser(userId)
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
                    // Profile picture
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // This would be an actual image in a real app
                        Text(
                            text = user.name.first().toString(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
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
                            color = MaterialTheme.colorScheme.secondary,
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
                            contentDescription = null
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = { /* TODO: Navigate to provider dashboard */ }) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "View Dashboard"
                            )
                        }
                    },
                    modifier = Modifier.clickable { /* TODO: Navigate to provider dashboard */ }
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
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null
                    )
                },
                trailingContent = {
                    IconButton(onClick = { /* TODO: Navigate to about screen */ }) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "About"
                        )
                    }
                },
                modifier = Modifier.clickable { /* TODO: Navigate to about screen */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Logout button
            OutlinedButton(
                onClick = { /* TODO: Implement logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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

