package com.damiens.mjoba.ui.theme.Screens.Payment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.util.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavHostController,
    providerId: String,
    serviceId: String,
    date: String,
    time: String,
    viewModel: PaymentViewModel = viewModel()
) {
    // Get service and provider data
    val service = SampleData.getService(serviceId)
    val provider = SampleData.getServiceProvider(providerId)

    val totalAmount = service.price + (service.price * 0.05) // 5% platform fee

    var phoneNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Collect UI state from ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (val currentState = uiState) {
            is PaymentUiState.Success -> {
                // Navigate to confirmation screen
                navController.navigate(
                    Screen.PaymentConfirmation.createRoute(
                        providerId = providerId,
                        serviceId = serviceId,
                        amount = totalAmount.toInt().toString()
                    )
                )

                // Reset the state to avoid navigation issues when returning
                viewModel.resetState()
            }

            is PaymentUiState.Error -> {
                errorMessage = currentState.message
            }

            else -> { /* No action needed for Initial and Loading states */ }
        }
    }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Payment") },
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
            // Booking Summary Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Booking Summary",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = provider.businessName,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Date:")
                        Text(
                            text = date,
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
                            text = time,
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
                            text = "Ksh ${service.price.toInt()}",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Platform fee (5%):")
                        Text(
                            text = "Ksh ${(service.price * 0.05).toInt()}",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total amount:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Ksh ${totalAmount.toInt()}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // M-Pesa Payment Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "M-Pesa Payment",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Enter your M-Pesa phone number to receive an STK push notification for payment.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            // Only allow digits and limit to 12 characters
                            if (it.all { char -> char.isDigit() } && it.length <= 12) {
                                phoneNumber = it
                                errorMessage = null
                            }
                        },
                        label = { Text("M-Pesa Phone Number") },
                        placeholder = { Text("e.g. 07XXXXXXXX or 01XXXXXXXX") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        isError = errorMessage != null
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "By proceeding, you agree to pay Ksh ${totalAmount.toInt()} to M-Joba for ${service.name}.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Security Note
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Secure Payment",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Your payment is secure and processed via official M-Pesa channels.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Pay Button - Updated to use ViewModel
            Button(
                onClick = {
                    // Validate phone number format first
                    if (phoneNumber.isEmpty()) {
                        errorMessage = "Please enter your M-Pesa phone number"
                        return@Button
                    }

                    if (phoneNumber.length < 10) {
                        errorMessage = "Please enter a valid phone number"
                        return@Button
                    }

                    // Process payment via ViewModel
                    viewModel.processPayment(
                        phoneNumber = phoneNumber,
                        amount = totalAmount.toInt(),
                        providerId = providerId,
                        serviceId = serviceId,
                        date = date,
                        time = time
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState != PaymentUiState.Loading
            ) {
                if (uiState == PaymentUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Processing...")
                } else {
                    Text("Pay Ksh ${totalAmount.toInt()} with M-Pesa")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel Button
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState != PaymentUiState.Loading
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}