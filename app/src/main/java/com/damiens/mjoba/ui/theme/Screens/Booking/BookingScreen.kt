//package com.damiens.mjoba.ui.theme.Screens.Booking
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import com.damiens.mjoba.Data.AppLocationRepository
//import com.damiens.mjoba.Data.LocationData
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.runtime.collectAsState
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import com.damiens.mjoba.Navigation.Screen
//import com.damiens.mjoba.Model.Service
//import com.damiens.mjoba.Model.ServiceProvider
//import com.damiens.mjoba.ViewModel.AuthViewModel
//import com.damiens.mjoba.ViewModel.ServiceViewModel
//import com.damiens.mjoba.ViewModel.ServicesUiState
//import com.damiens.mjoba.ui.theme.SafaricomGreen
//import org.threeten.bp.LocalDate
//import org.threeten.bp.format.DateTimeFormatter
//import org.threeten.bp.format.TextStyle
//import com.damiens.mjoba.ViewModel.BookingViewModel
//import java.util.*
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BookingScreen(
//    navController: NavHostController,
//    serviceId: String,
//    serviceViewModel: ServiceViewModel = viewModel()
//) {
//    // Load service data
//    LaunchedEffect(serviceId) {
//        serviceViewModel.loadServiceById(serviceId)
//    }
//
//    // Add BookingViewModel
//    val bookingViewModel: BookingViewModel = viewModel()
//
//    // Add AuthViewModel
//    val authViewModel: AuthViewModel = viewModel()
//    val currentUser by authViewModel.currentUser.collectAsState()
//
//    // Get service state from ViewModel
//    val servicesState by serviceViewModel.servicesState.collectAsState()
//
//    // Service and provider data
//    var service by remember { mutableStateOf<Service?>(null) }
//    var provider by remember { mutableStateOf<ServiceProvider?>(null) }
//
//    // Process services state changes
//    LaunchedEffect(servicesState) {
//        if (servicesState is ServicesUiState.ServiceDetails) {
//            val details = servicesState as ServicesUiState.ServiceDetails
//            service = details.service
//            provider = details.provider
//        }
//    }
//
//    // Selected date and time
//    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
//    var selectedTime by remember { mutableStateOf("8:00 AM") }
//
//    // Phone number with validation
//    var phoneNumber by remember { mutableStateOf(currentUser?.phone ?: "") }
//    var isPhoneValid by remember { mutableStateOf(true) }
//
//    // Special instructions
//    var specialInstructions by remember { mutableStateOf("") }
//
//    // Get location from repository
//    val selectedLocation by AppLocationRepository.selectedLocation.collectAsState()
//
//    // Available times
//    val availableTimes = listOf(
//        "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
//        "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM"
//    )
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Book Service") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Go back"
//                        )
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        when (servicesState) {
//            is ServicesUiState.Loading -> {
//                // Show loading state
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
//            is ServicesUiState.Error -> {
//                // Show error state
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center,
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        Text(
//                            text = (servicesState as ServicesUiState.Error).message,
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = MaterialTheme.colorScheme.error
//                        )
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Button(
//                            onClick = { serviceViewModel.loadServiceById(serviceId) }
//                        ) {
//                            Text("Retry")
//                        }
//                    }
//                }
//            }
//
//            is ServicesUiState.ServiceDetails -> {
//                val serviceDetails = servicesState as ServicesUiState.ServiceDetails
//                service = serviceDetails.service
//                provider = serviceDetails.provider
//
//                if (service == null || provider == null) {
//                    // Show error if service or provider is not found
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(paddingValues),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text("Service not found")
//                    }
//                } else {
//                    // Show booking form
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(paddingValues)
//                            .verticalScroll(rememberScrollState())
//                    ) {
//                        // Service details card
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        ) {
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp)
//                            ) {
//                                Text(
//                                    text = provider!!.businessName,
//                                    style = MaterialTheme.typography.bodyLarge,
//                                    color = SafaricomGreen
//                                )
//
//                                Text(
//                                    text = service!!.name,
//                                    style = MaterialTheme.typography.titleLarge,
//                                    fontWeight = FontWeight.Bold
//                                )
//
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically,
//                                    modifier = Modifier.padding(vertical = 4.dp)
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Default.Schedule,
//                                        contentDescription = null,
//                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                                        modifier = Modifier.size(20.dp)
//                                    )
//
//                                    Spacer(modifier = Modifier.width(4.dp))
//
//                                    Text(
//                                        text = "Duration: ~${service!!.estimatedDuration} mins",
//                                        style = MaterialTheme.typography.bodyMedium,
//                                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                                    )
//                                }
//
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Text(
//                                        text = "Price: ",
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
//
//                                    Text(
//                                        text = "Ksh ${service!!.price.toInt()}",
//                                        style = MaterialTheme.typography.bodyLarge,
//                                        fontWeight = FontWeight.Bold,
//                                        color = SafaricomGreen
//                                    )
//                                }
//                            }
//                        }
//
//                        // Date selection
//                        Text(
//                            text = "Select Date",
//                            style = MaterialTheme.typography.titleMedium,
//                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                        )
//
//                        // Date picker row
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp),
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            // Show next 5 days
//                            for (i in 0..4) {
//                                val date = LocalDate.now().plusDays(i.toLong())
//                                val isSelected = date == selectedDate
//
//                                OutlinedButton(
//                                    onClick = { selectedDate = date },
//                                    modifier = Modifier.weight(1f),
//                                    colors = ButtonDefaults.outlinedButtonColors(
//                                        containerColor = if (isSelected) SafaricomGreen else Color.Transparent,
//                                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
//                                    )
//                                ) {
//                                    Column(
//                                        horizontalAlignment = Alignment.CenterHorizontally
//                                    ) {
//                                        Text(
//                                            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
//                                            style = MaterialTheme.typography.bodyMedium
//                                        )
//
//                                        Text(
//                                            text = DateTimeFormatter.ofPattern("MMM d").format(date),
//                                            style = MaterialTheme.typography.bodyMedium
//                                        )
//                                    }
//                                }
//                            }
//                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Time selection
//                        Text(
//                            text = "Select Time",
//                            style = MaterialTheme.typography.titleMedium,
//                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                        )
//
//                        // Time picker row
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp),
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            // Show available times (first 5)
//                            for (i in 0 until minOf(5, availableTimes.size)) {
//                                val time = availableTimes[i]
//                                val isSelected = time == selectedTime
//
//                                OutlinedButton(
//                                    onClick = { selectedTime = time },
//                                    modifier = Modifier.weight(1f),
//                                    colors = ButtonDefaults.outlinedButtonColors(
//                                        containerColor = if (isSelected) SafaricomGreen else Color.Transparent,
//                                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
//                                    )
//                                ) {
//                                    Text(
//                                        text = time,
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
//                                }
//                            }
//                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Service Location
//                        Text(
//                            text = "Service Location",
//                            style = MaterialTheme.typography.titleMedium,
//                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                        )
//
//                        // Location selection card
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp),
//                            onClick = { navController.navigate(Screen.LocationSelection.createRoute()) }
//                        ) {
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp),
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically,
//                                    modifier = Modifier.weight(1f)
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Default.LocationOn,
//                                        contentDescription = null,
//                                        tint = SafaricomGreen
//                                    )
//
//                                    Spacer(modifier = Modifier.width(16.dp))
//
//                                    Column {
//                                        selectedLocation?.let { location ->
//                                            Text(
//                                                text = location.displayName,
//                                                style = MaterialTheme.typography.bodyLarge,
//                                                fontWeight = FontWeight.Medium
//                                            )
//
//                                            Text(
//                                                text = location.address,
//                                                style = MaterialTheme.typography.bodySmall,
//                                                color = MaterialTheme.colorScheme.onSurfaceVariant
//                                            )
//                                        } ?: run {
//                                            Text(
//                                                text = "Set your location",
//                                                style = MaterialTheme.typography.bodyMedium,
//                                                color = MaterialTheme.colorScheme.error
//                                            )
//                                        }
//                                    }
//                                }
//
//                                TextButton(
//                                    onClick = { navController.navigate(Screen.LocationSelection.createRoute()) }
//                                ) {
//                                    Text("Change", color = SafaricomGreen)
//                                }
//                            }
//                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Contact Phone
//                        Text(
//                            text = "Contact Phone",
//                            style = MaterialTheme.typography.titleMedium,
//                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                        )
//
//                        OutlinedTextField(
//                            value = phoneNumber,
//                            onValueChange = {
//                                if (it.all { char -> char.isDigit() || char == '+' } && it.length <= 13) {
//                                    phoneNumber = it
//                                    isPhoneValid = phoneNumber.length >= 10
//                                }
//                            },
//                            isError = !isPhoneValid && phoneNumber.isNotEmpty(),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp),
//                            placeholder = { Text("Enter your phone number") },
//                            leadingIcon = {
//                                Icon(
//                                    imageVector = Icons.Default.Phone,
//                                    contentDescription = null
//                                )
//                            },
//                            singleLine = true
//                        )
//
//                        // Phone validation error message
//                        if (!isPhoneValid && phoneNumber.isNotEmpty()) {
//                            Text(
//                                text = "* Please enter a valid phone number",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.error,
//                                modifier = Modifier.padding(horizontal = 16.dp)
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Special Instructions
//                        Text(
//                            text = "Special Instructions (Optional)",
//                            style = MaterialTheme.typography.titleMedium,
//                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                        )
//
//                        OutlinedTextField(
//                            value = specialInstructions,
//                            onValueChange = { specialInstructions = it },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(120.dp)
//                                .padding(horizontal = 16.dp),
//                            placeholder = { Text("Add any special instructions for the service provider") }
//                        )
//
//                        Spacer(modifier = Modifier.height(24.dp))
//
//                        // Booking Summary
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            colors = CardDefaults.cardColors(
//                                containerColor = SafaricomGreen.copy(alpha = 0.1f)
//                            )
//                        ) {
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp)
//                            ) {
//                                Text(
//                                    text = "Booking Summary",
//                                    style = MaterialTheme.typography.titleMedium,
//                                    fontWeight = FontWeight.Bold,
//                                    color = SafaricomGreen
//                                )
//
//                                Spacer(modifier = Modifier.height(8.dp))
//
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Text(
//                                        text = "Service:",
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
//
//                                    Text(
//                                        text = service!!.name,
//                                        style = MaterialTheme.typography.bodyMedium,
//                                        fontWeight = FontWeight.Medium
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(4.dp))
//
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Text(
//                                        text = "Date:",
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
//
//                                    Text(
//                                        text = "${selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())}, ${
//                                            DateTimeFormatter.ofPattern("MMM d").format(selectedDate)
//                                        }",
//                                        style = MaterialTheme.typography.bodyMedium,
//                                        fontWeight = FontWeight.Medium
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(4.dp))
//
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Text(
//                                        text = "Time:",
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
//
//                                    Text(
//                                        text = selectedTime,
//                                        style = MaterialTheme.typography.bodyMedium,
//                                        fontWeight = FontWeight.Medium
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(4.dp))
//
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Text(
//                                        text = "Service fee:",
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
//
//                                    Text(
//                                        text = "Ksh ${service!!.price.toInt()}",
//                                        style = MaterialTheme.typography.bodyMedium,
//                                        fontWeight = FontWeight.Bold,
//                                        color = SafaricomGreen
//                                    )
//                                }
//                            }
//                        }
//
//// Proceed to Payment Button
//                        Button(
//                            onClick = {
//                                // Check if service is not null
//                                service?.let { serviceData ->
//                                    // Create a temporary booking
//                                    bookingViewModel.createTemporaryBooking(
//                                        serviceId = serviceData.id,
//                                        providerId = serviceData.providerId,
//                                        date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(selectedDate),
//                                        time = selectedTime,
//                                        location = selectedLocation?.address ?: "",
//                                        phoneNumber = phoneNumber,
//                                        instructions = specialInstructions,
//                                        amount = serviceData.price
//                                    )
//
//                                    // Convert date and time to URL-safe format
//                                    val dateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(selectedDate)
//                                    val timeStr = selectedTime.replace(":", "-").replace(" ", "")
//
//                                    // Navigate to payment screen
//                                    navController.navigate(
//                                        Screen.Payment.createRoute(
//                                            providerId = serviceData.providerId,
//                                            serviceId = serviceData.id,
//                                            date = dateStr,
//                                            time = timeStr
//                                        )
//                                    )
//                                }
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            enabled = service != null && selectedLocation != null && phoneNumber.isNotEmpty() && isPhoneValid
//                        ){
//                            Text("Proceed to Payment")
//                        }
//
//                        // Show error if location not set
//                        if (selectedLocation == null) {
//                            Text(
//                                text = "* Please set your location to continue",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.error,
//                                modifier = Modifier.padding(horizontal = 16.dp)
//                            )
//                        }
//
//                        // Show error if phone empty
//                        if (phoneNumber.isEmpty()) {
//                            Text(
//                                text = "* Please enter your phone number",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.error,
//                                modifier = Modifier.padding(horizontal = 16.dp)
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.height(32.dp))
//                    }
//                }
//            }
//
//            else -> {
//                // Initial or other states
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(color = SafaricomGreen)
//                }
//            }
//        }
//    }
//}
package com.damiens.mjoba.ui.theme.Screens.Booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.ViewModel.AuthViewModel
import com.damiens.mjoba.ViewModel.ServiceViewModel
import com.damiens.mjoba.ViewModel.ServicesUiState
import com.damiens.mjoba.ui.theme.SafaricomGreen
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import com.damiens.mjoba.ViewModel.BookingViewModel
import java.util.*
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.damiens.mjoba.Data.AppLocationRepository
import com.damiens.mjoba.Data.LocationData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    navController: NavHostController,
    serviceId: String,
    serviceViewModel: ServiceViewModel = viewModel(),
    bookingViewModel: BookingViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    // Context and coroutine scope
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Load service data
    LaunchedEffect(serviceId) {
        Log.d("BookingScreen", "Loading service with ID: $serviceId")
        serviceViewModel.loadServiceById(serviceId)
    }

    // Get current user
    val currentUser by authViewModel.currentUser.collectAsState()

    // Get service state from ViewModel
    val servicesState by serviceViewModel.servicesState.collectAsState()

    // Service and provider data
    var service by remember { mutableStateOf<Service?>(null) }
    var provider by remember { mutableStateOf<ServiceProvider?>(null) }

    // UI State
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Process services state changes
    LaunchedEffect(servicesState) {
        when (servicesState) {
            is ServicesUiState.Loading -> {
                Log.d("BookingScreen", "Service is loading...")
            }
            is ServicesUiState.ServiceDetails -> {
                val details = servicesState as ServicesUiState.ServiceDetails
                Log.d("BookingScreen", "Service loaded: ${details.service.name}")
                service = details.service
                provider = details.provider

                // If provider is null or has minimal info, try to fetch more details
                if (provider == null || provider?.businessName == "Unknown Provider") {
                    Log.d("BookingScreen", "Provider info is missing, trying to fetch from service provider repository")
                    // You might need to implement this in your ServiceViewModel or a separate method
                    // serviceViewModel.loadProviderDetails(details.service.providerId)
                }
            }
            is ServicesUiState.Error -> {
                Log.e("BookingScreen", "Error loading service: ${(servicesState as ServicesUiState.Error).message}")
                errorMessage = (servicesState as ServicesUiState.Error).message
            }
            else -> {
                // Do nothing for other states
            }
        }
    }

    // Booking information state
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf("8:00 AM") }
    var phoneNumber by remember { mutableStateOf(currentUser?.phone ?: "") }
    var isPhoneValid by remember { mutableStateOf(phoneNumber.length >= 10) }
    var specialInstructions by remember { mutableStateOf("") }

    // Get location from repository
    val selectedLocation by AppLocationRepository.selectedLocation.collectAsState()

    // Available times
    val availableTimes = listOf(
        "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
        "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Service") },
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
        when {
            isLoading -> {
                // Show loading state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SafaricomGreen)
                }
            }

            errorMessage != null -> {
                // Show error state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = errorMessage ?: "Failed to load service details",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                errorMessage = null
                                serviceViewModel.loadServiceById(serviceId)
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            service == null || provider == null -> {
                // Show placeholder while loading
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SafaricomGreen)
                }
            }

            else -> {
                // Show booking form
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Service details card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = provider!!.businessName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = SafaricomGreen
                            )

                            Text(
                                text = service!!.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "Duration: ~${service!!.estimatedDuration} mins",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Price: ",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = "Ksh ${service!!.price.toInt()}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = SafaricomGreen
                                )
                            }
                        }
                    }

                    // Date selection
                    Text(
                        text = "Select Date",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Date picker row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Show next 5 days
                        for (i in 0..4) {
                            val date = LocalDate.now().plusDays(i.toLong())
                            val isSelected = date == selectedDate

                            OutlinedButton(
                                onClick = { selectedDate = date },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) SafaricomGreen else Color.Transparent,
                                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Text(
                                        text = DateTimeFormatter.ofPattern("MMM d").format(date),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Time selection
                    Text(
                        text = "Select Time",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Time picker row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Show available times (first 5)
                        for (i in 0 until minOf(5, availableTimes.size)) {
                            val time = availableTimes[i]
                            val isSelected = time == selectedTime

                            OutlinedButton(
                                onClick = { selectedTime = time },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) SafaricomGreen else Color.Transparent,
                                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text(
                                    text = time,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Service Location
                    Text(
                        text = "Service Location",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Location selection card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = { navController.navigate(Screen.LocationSelection.createRoute()) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = SafaricomGreen
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    selectedLocation?.let { location ->
                                        Text(
                                            text = location.displayName,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Text(
                                            text = location.address,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    } ?: run {
                                        Text(
                                            text = "Set your location",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }

                            TextButton(
                                onClick = { navController.navigate(Screen.LocationSelection.createRoute()) }
                            ) {
                                Text("Change", color = SafaricomGreen)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Contact Phone
                    Text(
                        text = "Contact Phone",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() || char == '+' } && it.length <= 13) {
                                phoneNumber = it
                                isPhoneValid = phoneNumber.length >= 10
                            }
                        },
                        isError = !isPhoneValid && phoneNumber.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        placeholder = { Text("Enter your phone number") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null
                            )
                        },
                        singleLine = true
                    )

                    // Phone validation error message
                    if (!isPhoneValid && phoneNumber.isNotEmpty()) {
                        Text(
                            text = "* Please enter a valid phone number",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Special Instructions
                    Text(
                        text = "Special Instructions (Optional)",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    OutlinedTextField(
                        value = specialInstructions,
                        onValueChange = { specialInstructions = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(horizontal = 16.dp),
                        placeholder = { Text("Add any special instructions for the service provider") }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Booking Summary
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SafaricomGreen.copy(alpha = 0.1f)
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
                                fontWeight = FontWeight.Bold,
                                color = SafaricomGreen
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Service:",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = service!!.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Date:",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = "${selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())}, ${
                                        DateTimeFormatter.ofPattern("MMM d").format(selectedDate)
                                    }",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Time:",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = selectedTime,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Service fee:",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = "Ksh ${service!!.price.toInt()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SafaricomGreen
                                )
                            }
                        }
                    }

                    // Proceed to Payment Button
                    Button(
                        onClick = {
                            try {
                                // Check if all data is present
                                if (selectedLocation == null) {
                                    scope.launch {
                                        Toast.makeText(context, "Please set your location", Toast.LENGTH_SHORT).show()
                                    }
                                    return@Button
                                }

                                if (phoneNumber.length < 10) {
                                    scope.launch {
                                        Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                                    }
                                    return@Button
                                }

                                if (currentUser == null) {
                                    scope.launch {
                                        Toast.makeText(context, "You need to be logged in to book a service", Toast.LENGTH_SHORT).show()
                                    }
                                    return@Button
                                }

                                // Create temporary booking
                                Log.d("BookingScreen", "Creating temporary booking for service: ${service!!.id}")
                                isLoading = true

                                // Create a temporary booking
                                bookingViewModel.createTemporaryBooking(
                                    serviceId = service!!.id,
                                    providerId = service!!.providerId,
                                    date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(selectedDate),
                                    time = selectedTime,
                                    location = selectedLocation?.address ?: "",
                                    phoneNumber = phoneNumber,
                                    instructions = specialInstructions,
                                    amount = service!!.price
                                )

                                // Convert date and time to URL-safe format for navigation
                                val dateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(selectedDate)
                                val timeStr = selectedTime.replace(":", "-").replace(" ", "")

                                // Navigate to payment screen
                                navController.navigate(
                                    Screen.Payment.createRoute(
                                        providerId = service!!.providerId,
                                        serviceId = service!!.id,
                                        date = dateStr,
                                        time = timeStr
                                    )
                                )

                                isLoading = false
                            } catch (e: Exception) {
                                Log.e("BookingScreen", "Error creating booking: ${e.message}")
                                scope.launch {
                                    Toast.makeText(
                                        context,
                                        "Error creating booking: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                isLoading = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        enabled = service != null && selectedLocation != null && phoneNumber.isNotEmpty() && isPhoneValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SafaricomGreen,
                            disabledContainerColor = SafaricomGreen.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = "Proceed to Payment",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Show error if location not set
                    if (selectedLocation == null) {
                        Text(
                            text = "* Please set your location to continue",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    // Show error if phone empty
                    if (phoneNumber.isEmpty()) {
                        Text(
                            text = "* Please enter your phone number",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

// Extension function to simulate default location when none is set
// In a real app, this would be handled properly in your AppLocationRepository
//private fun getDefaultLocation(): LocationData? {
//    return LocationData(
//        latitude = -1.2920659,  // Default to Nairobi
//        longitude = 36.8219462,
//        displayName = "Nairobi, Kenya",
//        address = "Nairobi, Kenya"
//    )
//}