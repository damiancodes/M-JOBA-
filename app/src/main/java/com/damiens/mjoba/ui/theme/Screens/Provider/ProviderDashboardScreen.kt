package com.damiens.mjoba.ui.theme.Screens.Provider

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.BookingStatus
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.ViewModel.*
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import com.damiens.mjoba.ui.theme.SafaricomBackground
import com.damiens.mjoba.ui.theme.SafaricomGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDashboardScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    bookingViewModel: BookingViewModel = viewModel(),
    serviceViewModel: ServiceViewModel = viewModel(),
    serviceProviderViewModel: ServiceProviderViewModel = viewModel()
) {
    // Get current user
    val currentUser by authViewModel.currentUser.collectAsState()
    val userId = currentUser?.id ?: return

    // Load provider bookings
    LaunchedEffect(userId) {
        bookingViewModel.getProviderBookings(userId)
    }

    // Load provider services
    LaunchedEffect(userId) {
        serviceViewModel.loadServicesByProvider(userId)
    }

    // Get provider details
    LaunchedEffect(userId) {
        serviceProviderViewModel.getProviderDetails(userId)
    }

    // State
    val bookingsState by bookingViewModel.userBookingsState.collectAsState()
    val servicesState by serviceViewModel.servicesState.collectAsState()
    val providerDetailsState by serviceProviderViewModel.providerDetailsState.collectAsState()

    // Tabs
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Dashboard", "Services", "Bookings", "Profile")

    Scaffold(
        containerColor = SafaricomBackground,
        topBar = {
            TopAppBar(
                title = { Text("Provider Dashboard") },
                actions = {
                    // Notification Icon
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFECF5EE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = SafaricomGreen
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedTabIndex == 1) { // Only show on Services tab
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddService.route) },
                    containerColor = SafaricomGreen,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Service")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Dashboard tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = SafaricomGreen
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> DashboardTab(
                    navController = navController,
                    bookingsState = bookingsState,
                    servicesState = servicesState,
                    onTabChange = { newIndex ->
                        selectedTabIndex = newIndex
                    }
                )
                1 -> ServicesTab(navController, servicesState)
                2 -> BookingsTab(navController, bookingsState)
                3 -> ProfileTab(navController, providerDetailsState)
            }
        }
    }
}

@Composable
fun DashboardTab(
    navController: NavHostController,
    bookingsState: UserBookingsUiState,
    servicesState: ServicesUiState,
    onTabChange: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary cards for key metrics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Pending bookings card
                SummaryCard(
                    icon = Icons.Default.Schedule,
                    title = "Pending",
                    count = countPendingBookings(bookingsState),
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFFFA000)
                )

                // Total services card
                SummaryCard(
                    icon = Icons.Default.Build,
                    title = "Services",
                    count = countServices(servicesState),
                    modifier = Modifier.weight(1f),
                    color = SafaricomGreen
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Completed bookings card
                SummaryCard(
                    icon = Icons.Default.CheckCircle,
                    title = "Completed",
                    count = countCompletedBookings(bookingsState),
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF00C853)
                )

                // Revenue card (placeholder)
                SummaryCard(
                    icon = Icons.Default.AttachMoney,
                    title = "Revenue",
                    value = "KSh " + calculateRevenue(bookingsState).toString(),
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF0288D1)
                )
            }
        }

        // Recent bookings section
        item {
            Text(
                text = "Recent Bookings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        // Display recent bookings or placeholder
        when (bookingsState) {
            is UserBookingsUiState.Success -> {
                val recentBookings = bookingsState.bookings.take(5)
                if (recentBookings.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No bookings yet",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(recentBookings) { booking ->
                        BookingSummaryItem(
                            bookingId = booking.id,
                            date = booking.date,
                            status = booking.status,
                            amount = booking.totalAmount,
                            onClick = {
                                navController.navigate(
                                    Screen.BookingDetails.createRoute(booking.id)
                                )
                            }
                        )
                    }
                }
            }
            is UserBookingsUiState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SafaricomGreen)
                    }
                }
            }
            else -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No bookings available",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Action buttons
        item {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionButton(
                    icon = Icons.Default.Add,
                    title = "Add Service",
                    onClick = { navController.navigate(Screen.AddService.route) },
                    modifier = Modifier.weight(1f)
                )

                ActionButton(
                    icon = Icons.Default.List,
                    title = "View Bookings",
                    onClick = { onTabChange(2) }, // Call the callback instead of directly modifying the variable
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
        }
    }
}

@Composable
fun ServicesTab(
    navController: NavHostController,
    servicesState: ServicesUiState
) {
    when (servicesState) {
        is ServicesUiState.Success -> {
            if (servicesState.services.isEmpty()) {
                // Empty state
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
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No services yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Tap the + button to add your first service",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                // List services
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(servicesState.services) { service ->
                        ServiceCard(
                            service = service,
                            onClick = {
                                // Navigate to service details/edit
                                navController.navigate(Screen.ServiceDetails.createRoute(service.id))
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
                    }
                }
            }
        }
        is ServicesUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SafaricomGreen)
            }
        }
        else -> {
            // Error or Initial state
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
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Could not load services",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Retry loading services
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun BookingsTab(
    navController: NavHostController,
    bookingsState: UserBookingsUiState
) {
    when (bookingsState) {
        is UserBookingsUiState.Success -> {
            if (bookingsState.bookings.isEmpty()) {
                // Empty state
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
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No bookings yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Your bookings will appear here",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                // List bookings
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bookingsState.bookings) { booking ->
                        BookingCard(
                            bookingId = booking.id,
                            date = booking.date,
                            time = booking.time,
                            status = booking.status,
                            amount = booking.totalAmount,
                            onClick = {
                                navController.navigate(
                                    Screen.BookingDetails.createRoute(booking.id)
                                )
                            }
                        )
                    }
                }
            }
        }
        is UserBookingsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SafaricomGreen)
            }
        }
        else -> {
            // Error or initial state
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
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Could not load bookings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Retry loading bookings
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileTab(
    navController: NavHostController,
    providerDetailsState: ProviderDetailsUiState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            when (providerDetailsState) {
                is ProviderDetailsUiState.Success -> {
                    val provider = providerDetailsState.provider

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile image or placeholder
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(SafaricomGreen.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (provider.coverImageUrl.isNotEmpty()) {
                                // Use Coil or other image loading library here
                                // Image goes here
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    tint = SafaricomGreen
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    tint = SafaricomGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Business name
                        Text(
                            text = provider.businessName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        // Verification badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (provider.isVerified) Icons.Default.Verified else Icons.Default.Info,
                                contentDescription = null,
                                tint = if (provider.isVerified) Color(0xFF4CAF50) else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = if (provider.isVerified) "Verified Provider" else "Verification Pending",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (provider.isVerified) Color(0xFF4CAF50) else Color.Gray
                            )
                        }

                        // Description
                        Text(
                            text = provider.description,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                        )

                        // Rating if available
                        if (provider.reviewCount > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "${provider.rating} (${provider.reviewCount} reviews)",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
                is ProviderDetailsUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SafaricomGreen)
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Could not load profile",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Profile actions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileActionItem(
                        icon = Icons.Default.Edit,
                        title = "Edit Business Profile",
                        onClick = { navController.navigate(Screen.EditProfile.route) }
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    ProfileActionItem(
                        icon = Icons.Default.LocationOn,
                        title = "Update Business Location",
                        onClick = { navController.navigate(Screen.LocationSelection.route) }
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    ProfileActionItem(
                        icon = Icons.Default.Settings,
                        title = "Account Settings",
                        onClick = { /* TODO */ }
                    )
                }
            }
        }

        // Logout button
        item {
            Button(
                onClick = {
                    // Handle logout
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE57373)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Logout")
            }
        }
    }
}

// UI Components

@Composable
fun SummaryCard(
    icon: ImageVector,
    title: String,
    count: Int? = null,
    value: String? = null,
    modifier: Modifier = Modifier,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = count?.toString() ?: value ?: "0",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSummaryItem(
    bookingId: String,
    date: String,
    status: BookingStatus,
    amount: Double,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status indicator
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(getStatusColor(status))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Booking info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Booking #${bookingId.takeLast(6)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Amount
            Text(
                text = "KSh ${amount.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SafaricomGreen
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingCard(
    bookingId: String,
    date: String,
    time: String,
    status: BookingStatus,
    amount: Double,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Booking #${bookingId.takeLast(6)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                AssistChip(
                    onClick = { },
                    label = { Text(status.toString()) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = getStatusColor(status).copy(alpha = 0.2f),
                        labelColor = getStatusColor(status)
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = time,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Text(
                    text = "KSh ${amount.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SafaricomGreen
                )
            }

            if (status == BookingStatus.PENDING) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Reject booking */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Text("Decline")
                    }

                    Button(
                        onClick = { /* Accept booking */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SafaricomGreen
                        )
                    ) {
                        Text("Accept")
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Service icon or image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SafaricomGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (service.imageUrl.isNotEmpty()) {
                    // Use Coil or other image loading library here
                    // Image goes here
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = SafaricomGreen,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = SafaricomGreen,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Service details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = service.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${service.estimatedDuration} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Price
            Text(
                text = "KSh ${service.price.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SafaricomGreen
            )
        }
    }
}



@Composable
fun ProfileActionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

// Helper functions
private fun getStatusColor(status: BookingStatus): Color {
    return when (status) {
        BookingStatus.PENDING -> Color(0xFFFFA000)    // Amber
        BookingStatus.CONFIRMED -> Color(0xFF2196F3)  // Blue
        BookingStatus.IN_PROGRESS -> Color(0xFF9C27B0) // Purple
        BookingStatus.COMPLETED -> Color(0xFF4CAF50)  // Green
        BookingStatus.CANCELLED -> Color(0xFFF44336)  // Red
        BookingStatus.REJECTED -> Color(0xFFF44336)   // Red
    }
}

private fun countPendingBookings(bookingsState: UserBookingsUiState): Int {
    return if (bookingsState is UserBookingsUiState.Success) {
        bookingsState.bookings.count { it.status == BookingStatus.PENDING }
    } else {
        0
    }
}

private fun countCompletedBookings(bookingsState: UserBookingsUiState): Int {
    return if (bookingsState is UserBookingsUiState.Success) {
        bookingsState.bookings.count { it.status == BookingStatus.COMPLETED }
    } else {
        0
    }
}

private fun countServices(servicesState: ServicesUiState): Int {
    return if (servicesState is ServicesUiState.Success) {
        servicesState.services.size
    } else {
        0
    }
}

private fun calculateRevenue(bookingsState: UserBookingsUiState): Int {
    return if (bookingsState is UserBookingsUiState.Success) {
        bookingsState.bookings
            .filter { it.status == BookingStatus.COMPLETED }
            .sumOf { it.totalAmount }
            .toInt()
    } else {
        0
    }
}