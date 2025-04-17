package com.damiens.mjoba.ui.theme.Screens.Orders

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.Booking
import com.damiens.mjoba.Model.BookingStatus
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.util.SampleData
import com.damiens.mjoba.Model.PaymentStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsListScreen(
    navController: NavHostController
) {
    // This would come from a ViewModel in a real app
    val bookings = getSampleBookings()

    // Group bookings by status
    val activeBookings = bookings.filter {
        it.status == BookingStatus.PENDING ||
                it.status == BookingStatus.CONFIRMED ||
                it.status == BookingStatus.IN_PROGRESS
    }
    val completedBookings = bookings.filter { it.status == BookingStatus.COMPLETED }
    val cancelledBookings = bookings.filter { it.status == BookingStatus.CANCELLED }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Active", "Completed", "Cancelled")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Bookings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
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
            // Tab layout
            TabRow(
                selectedTabIndex = selectedTab
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Bookings list based on selected tab
            when (selectedTab) {
                0 -> BookingsList(activeBookings, navController)
                1 -> BookingsList(completedBookings, navController)
                2 -> BookingsList(cancelledBookings, navController)
            }
        }
    }
}

@Composable
fun BookingsList(
    bookings: List<Booking>,
    navController: NavHostController
) {
    if (bookings.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Info, // Changed from InfoOutline
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No bookings found",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(bookings) { booking ->
                BookingCard(booking = booking, navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingCard(
    booking: Booking,
    navController: NavHostController
) {
    val service = SampleData.getService(booking.serviceId)
    val provider = SampleData.getServiceProvider(booking.serviceProviderId)
    val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    Card(
        onClick = {
            navController.navigate(Screen.BookingDetails.createRoute(booking.id))
        },
        modifier = Modifier.fillMaxWidth()
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
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                BookingStatusChip(status = booking.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = provider.businessName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = booking.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = booking.time,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ksh ${booking.totalAmount.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = {
                        navController.navigate(Screen.BookingDetails.createRoute(booking.id))
                    }
                ) {
                    Text("View Details")
                }
            }
        }
    }
}

@Composable
fun BookingStatusChip(status: BookingStatus) {
    val (backgroundColor, contentColor, statusText) = getStatusColors(
        status = status,
        tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer,
        onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer,
        primaryContainer = MaterialTheme.colorScheme.primaryContainer,
        onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer,
        secondaryContainer = MaterialTheme.colorScheme.secondaryContainer,
        onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer,
        primary = MaterialTheme.colorScheme.primary,
        error = MaterialTheme.colorScheme.error
    )

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

// Sample data - in a real app, this would come from a repository
private fun getSampleBookings(): List<Booking> {
    return listOf(
        Booking(
            id = "b001",
            customerId = "u123",
            serviceProviderId = "2001",
            serviceId = "1001",
            date = "Mon, Apr 17",
            time = "10:00 AM",
            status = BookingStatus.CONFIRMED,
            totalAmount = 1575.0, // Service fee + 5% platform fee
            paymentStatus = PaymentStatus.COMPLETED,
            createdAt = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000) // 2 days ago
        ),
        Booking(
            id = "b002",
            customerId = "u123",
            serviceProviderId = "3001",
            serviceId = "2001",
            date = "Wed, Apr 19",
            time = "2:00 PM",
            status = BookingStatus.PENDING,
            totalAmount = 1050.0,
            paymentStatus = PaymentStatus.COMPLETED,
            createdAt = System.currentTimeMillis() - (1 * 24 * 60 * 60 * 1000) // 1 day ago
        ),
        Booking(
            id = "b003",
            customerId = "u123",
            serviceProviderId = "2002",
            serviceId = "1002",
            date = "Fri, Apr 14",
            time = "9:00 AM",
            status = BookingStatus.COMPLETED,
            totalAmount = 3150.0,
            paymentStatus = PaymentStatus.COMPLETED,
            createdAt = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000) // 5 days ago
        ),
        Booking(
            id = "b004",
            customerId = "u123",
            serviceProviderId = "3002",
            serviceId = "2002",
            date = "Sat, Apr 8",
            time = "11:00 AM",
            status = BookingStatus.CANCELLED,
            totalAmount = 2625.0,
            paymentStatus = PaymentStatus.FAILED,
            createdAt = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000) // 10 days ago
        )
    )
}

// Helper function to get status colors
private fun getStatusColors(
    status: BookingStatus,
    tertiaryContainer: Color,
    onTertiaryContainer: Color,
    primaryContainer: Color,
    onPrimaryContainer: Color,
    secondaryContainer: Color,
    onSecondaryContainer: Color,
    primary: Color,
    error: Color
): Triple<Color, Color, String> {
    return when (status) {
        BookingStatus.PENDING -> Triple(
            tertiaryContainer,
            onTertiaryContainer,
            "Pending"
        )
        BookingStatus.CONFIRMED -> Triple(
            primaryContainer,
            onPrimaryContainer,
            "Confirmed"
        )
        BookingStatus.IN_PROGRESS -> Triple(
            secondaryContainer,
            onSecondaryContainer,
            "In Progress"
        )
        BookingStatus.COMPLETED -> Triple(
            primary.copy(alpha = 0.12f),
            primary,
            "Completed"
        )
        BookingStatus.CANCELLED -> Triple(
            error.copy(alpha = 0.12f),
            error,
            "Cancelled"
        )
        BookingStatus.REJECTED -> Triple(
            error.copy(alpha = 0.12f),
            error,
            "Rejected"
        )
    }
}