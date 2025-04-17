
package com.damiens.mjoba.ui.theme.Screens.Booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.Navigation.Screen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    navController: NavHostController,
    serviceId: String
) {
    // This would come from a ViewModel in a real app
    val service = getSampleService(serviceId)
    val provider = getSampleProvider(service.providerId)

    var selectedDateIndex by remember { mutableStateOf(0) }
    var selectedTimeIndex by remember { mutableStateOf(0) }
    var specialInstructions by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    // Generate available dates (next 7 days)
    val availableDates = generateAvailableDates()

    // Generate available time slots
    val availableTimeSlots = listOf(
        "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM",
        "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM",
        "4:00 PM", "5:00 PM", "6:00 PM"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Book Service") },
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
            // Service and Provider Info
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = provider.businessName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )

                        Text(
                            text = "Duration: ~${service.estimatedDuration} mins",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Price: ",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "Ksh ${service.price}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Select Date
            Text(
                text = "Select Date",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableDates.size) { index ->
                    val date = availableDates[index]
                    DateCard(
                        date = date,
                        isSelected = selectedDateIndex == index,
                        onClick = { selectedDateIndex = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Select Time
            Text(
                text = "Select Time",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableTimeSlots.size) { index ->
                    val time = availableTimeSlots[index]
                    TimeCard(
                        time = time,
                        isSelected = selectedTimeIndex == index,
                        onClick = { selectedTimeIndex = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Location
            Text(
                text = "Service Location",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Current Location",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "123 Main St, Nairobi",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    TextButton(onClick = { /* TODO: Change location */ }) {
                        Text(text = "Change")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Contact Phone
            Text(
                text = "Contact Phone",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = contactPhone,
                onValueChange = { contactPhone = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter your phone number") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Special Instructions
            Text(
                text = "Special Instructions (Optional)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = specialInstructions,
                onValueChange = { specialInstructions = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                placeholder = { Text("Add any special instructions for the service provider") }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Booking Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Service:")
                        Text(
                            text = service.name,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Date:")
                        Text(
                            text = availableDates[selectedDateIndex],
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Time:")
                        Text(
                            text = availableTimeSlots[selectedTimeIndex],
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Service fee:")
                        Text(
                            text = "Ksh ${service.price}",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Platform fee:")
                        Text(
                            text = "Ksh ${service.price * 0.05}",  // 5% platform fee
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total amount:",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ksh ${service.price + (service.price * 0.05)}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Proceed to payment button
            Button(
                onClick = {
                    // Get the selected date and time strings
                    val selectedDate = availableDates[selectedDateIndex]
                    val selectedTime = availableTimeSlots[selectedTimeIndex]

                    // Navigate to payment with all required parameters
                    navController.navigate(Screen.Payment.createRoute(
                        providerId = provider.userId,
                        serviceId = service.id,
                        date = selectedDate,
                        time = selectedTime
                    ))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Proceed to Payment")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateCard(
    date: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surface

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            // Split date into parts (e.g., "Mon, Apr 16")
            val parts = date.split(", ")

            Text(
                text = parts[0], // Day of week
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = parts[1], // Month + day
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCard(
    time: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surface

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
        modifier = Modifier.width(100.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Helper function to generate available dates for the next 7 days
private fun generateAvailableDates(): List<String> {
    val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    val calendar = Calendar.getInstance()

    return List(7) { index ->
        calendar.add(Calendar.DAY_OF_YEAR, if (index == 0) 0 else 1)
        dateFormat.format(calendar.time)
    }
}

// Sample data functions - in a real app, this would come from a repository
private fun getSampleService(serviceId: String): Service {
    return when (serviceId) {
        "1001" -> Service(id = "1001", categoryId = "101", providerId = "2001", name = "Basic House Cleaning", description = "General cleaning of your home", price = 1500.0, estimatedDuration = 120)
        "2001" -> Service(id = "2001", categoryId = "201", providerId = "3001", name = "Hair Styling", description = "Professional styling for all occasions", price = 1000.0, estimatedDuration = 60)
        else -> Service(id = serviceId, categoryId = "101", providerId = "2001", name = "Service", description = "Service description", price = 1000.0, estimatedDuration = 60)
    }
}

private fun getSampleProvider(providerId: String): ServiceProvider {
    return when (providerId) {
        "2001" -> ServiceProvider(
            userId = "u2001",
            businessName = "CleanHome Services",
            serviceCategories = listOf("101"),
            description = "We provide professional cleaning services for homes, offices and commercial spaces.",
            rating = 4.8f,
            reviewCount = 124,
            isVerified = true,
            isAvailable = true
        )
        "3001" -> ServiceProvider(
            userId = "u3001",
            businessName = "Glamour Hair Salon",
            serviceCategories = listOf("201"),
            description = "Professional hair styling for all occasions.",
            rating = 4.6f,
            reviewCount = 78,
            isVerified = true,
            isAvailable = true
        )
        else -> ServiceProvider(
            userId = "u${providerId.substring(1)}",
            businessName = "Service Provider $providerId",
            serviceCategories = listOf("101", "201", "301"),
            description = "Professional service provider with years of experience.",
            rating = 4.0f,
            reviewCount = 50,
            isVerified = true,
            isAvailable = true
        )
    }
}

