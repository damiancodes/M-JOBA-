package com.damiens.mjoba.ui.theme.Screens.Orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.BookingStatus
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.util.SampleData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.filled.ArrowBack
import com.damiens.mjoba.Model.PaymentStatus
import com.damiens.mjoba.Model.Booking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailsScreen(
    navController: NavHostController,
    bookingId: String
) {
    // This would come from a ViewModel in a real app
    val booking = SampleData.getBooking(bookingId)
    val scrollState = rememberScrollState()

    if (booking == null) {
        // Handle case when booking not found
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Booking not found")
        }
        return
    }

    // Get service and provider info
    val service = SampleData.getService(booking.serviceId)
    val provider = SampleData.getServiceProvider(booking.serviceProviderId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Booking Details") },
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
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Status card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BookingStatusChip(status = booking.status)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = getStatusDescription(booking.status),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Service details
            Text(
                text = "Service Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = service.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = provider.businessName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )

                            if (provider.isVerified) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = "Verified Provider",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Booking details
            Text(
                text = "Booking Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    DetailRow(
                        icon = Icons.Default.CalendarToday,
                        label = "Date",
                        value = booking.date
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow(
                        icon = Icons.Default.Schedule,
                        label = "Time",
                        value = booking.time
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow(
                        icon = Icons.Default.Receipt,
                        label = "Booking ID",
                        value = booking.id
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow(
                        icon = Icons.Default.DateRange,
                        label = "Booked on",
                        value = formatTimestamp(booking.createdAt)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Payment details
            Text(
                text = "Payment Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Service fee:")
                        Text(
                            text = "Ksh ${(booking.totalAmount / 1.05).toInt()}",
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Platform fee (5%):")
                        Text(
                            text = "Ksh ${(booking.totalAmount - (booking.totalAmount / 1.05)).toInt()}",
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total amount:",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ksh ${booking.totalAmount.toInt()}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Paid with M-Pesa",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        PaymentStatusChip(status = booking.paymentStatus)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action buttons
            if (booking.status == BookingStatus.PENDING || booking.status == BookingStatus.CONFIRMED) {
                Button(
                    onClick = { /* TODO: Implement cancellation */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Cancel Booking")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (booking.status == BookingStatus.COMPLETED) {
                Button(
                    onClick = { /* TODO: Navigate to review screen */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Rate Service")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedButton(
                onClick = { /* TODO: Implement contacting provider */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Contact Provider")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PaymentStatusChip(status: PaymentStatus) {
    val (backgroundColor, contentColor, statusText) = when (status) {
        PaymentStatus.PENDING -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "Pending"
        )
        PaymentStatus.PROCESSING -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "Processing"
        )
        PaymentStatus.COMPLETED -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            "Completed"
        )
        PaymentStatus.FAILED -> Triple(
            MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
            MaterialTheme.colorScheme.error,
            "Failed"
        )
    }

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

// Helper functions
private fun getStatusDescription(status: BookingStatus): String {
    return when (status) {
        BookingStatus.PENDING -> "Your booking is waiting for confirmation from the service provider."
        BookingStatus.CONFIRMED -> "Your booking has been confirmed. The service provider will arrive at the scheduled time."
        BookingStatus.IN_PROGRESS -> "Your service is currently in progress."
        BookingStatus.COMPLETED -> "Your service has been completed. Thank you for using M-Joba!"
        BookingStatus.CANCELLED -> "This booking has been cancelled."
        BookingStatus.REJECTED -> "This booking has been rejected."
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}
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
            totalAmount = 1575.0,
            paymentStatus = PaymentStatus.COMPLETED,
            createdAt = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000)
        ),
        // Add more sample bookings
    )
}

