//package com.damiens.mjoba.ui.theme.Screens.Provider
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import com.damiens.mjoba.Model.PriceType
//import com.damiens.mjoba.Model.Service
//import com.damiens.mjoba.Navigation.Screen
//import com.damiens.mjoba.ViewModel.AuthViewModel
//import com.damiens.mjoba.ViewModel.ServiceViewModel
//import com.damiens.mjoba.ui.theme.SafaricomGreen
//import kotlinx.coroutines.launch
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.material3.OutlinedTextField
//
//
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddServiceScreen(
//    navController: NavHostController,
//    serviceViewModel: ServiceViewModel = viewModel(),
//    authViewModel: AuthViewModel = viewModel()
//) {
//    // Get current user
//    val currentUser by authViewModel.currentUser.collectAsState()
//    val userId = currentUser?.id ?: ""
//
//    // Form state
//    var name by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var price by remember { mutableStateOf("") }
//    var estimatedDuration by remember { mutableStateOf("60") } // Default 60 minutes
//    var categoryId by remember { mutableStateOf("") }
//    var priceType by remember { mutableStateOf(PriceType.FIXED) }
//
//    // State for the dialog
//    var showSuccessDialog by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    var isSubmitting by remember { mutableStateOf(false) }
//
//    val scrollState = rememberScrollState()
//    val coroutineScope = rememberCoroutineScope()
//
//    // Services state
//    val servicesState by serviceViewModel.servicesState.collectAsState()
//
//    // Categories state (this should be from a CategoryViewModel in a real implementation)
//    val categories = listOf(
//        Pair("101", "House Cleaning"),
//        Pair("102", "Laundry"),
//        Pair("103", "Cooking"),
//        Pair("201", "Women's Hair"),
//        Pair("202", "Men's Grooming"),
//        Pair("301", "Plumbing"),
//        Pair("302", "Electrical"),
//        Pair("303", "Carpentry")
//    )
//
//    // Monitor services state for success/failure
//    LaunchedEffect(servicesState) {
//        when (servicesState) {
//            is com.damiens.mjoba.ViewModel.ServicesUiState.ServiceDetails -> {
//                // Service has been added successfully
//                isSubmitting = false
//                showSuccessDialog = true
//            }
//            is com.damiens.mjoba.ViewModel.ServicesUiState.Error -> {
//                val error = (servicesState as com.damiens.mjoba.ViewModel.ServicesUiState.Error).message
//                errorMessage = error
//                isSubmitting = false
//            }
//            else -> {
//                // Do nothing for other states
//            }
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Add New Service") },
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
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .verticalScroll(scrollState)
//                .padding(16.dp)
//        ) {
//            // Error message
//            if (errorMessage != null) {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.errorContainer
//                    )
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Error,
//                            contentDescription = "Error",
//                            tint = MaterialTheme.colorScheme.error
//                        )
//
//                        Spacer(modifier = Modifier.width(16.dp))
//
//                        Text(
//                            text = errorMessage ?: "",
//                            color = MaterialTheme.colorScheme.error
//                        )
//                    }
//                }
//            }
//
//            // Service Name
//            Text(
//                text = "Basic Information",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            OutlinedTextField(
//                value = name,
//                onValueChange = { name = it },
//                label = { Text("Service Name") },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Work,
//                        contentDescription = null
//                    )
//                },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Service Category
//            Text(
//                text = "Category",
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(bottom = 4.dp)
//            )
//
//            // Category Dropdown
//            ExposedDropdownMenuBox(
//                expanded = false,
//                onExpandedChange = { },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                OutlinedTextField(
//                    value = categories.find { it.first == categoryId }?.second ?: "Select Category",
//                    onValueChange = { },
//                    readOnly = true,
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
//                    },
//                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
//                    modifier = Modifier
//                        .menuAnchor()
//                        .fillMaxWidth()
//                )
//
//                DropdownMenu(
//                    expanded = false,
//                    onDismissRequest = { },
//                    modifier = Modifier.exposedDropdownSize()
//                ) {
//                    categories.forEach { (id, name) ->
//                        DropdownMenuItem(
//                            text = { Text(name) },
//                            onClick = {
//                                categoryId = id
//                            }
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Service Description
//            OutlinedTextField(
//                value = description,
//                onValueChange = { description = it },
//                label = { Text("Description") },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Description,
//                        contentDescription = null
//                    )
//                },
//                modifier = Modifier.fillMaxWidth(),
//                minLines = 3,
//                maxLines = 5
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Pricing Section
//            Text(
//                text = "Pricing & Duration",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            // Price
//            OutlinedTextField(
//                value = price,
//                onValueChange = {
//                    // Only allow numeric input with decimal
//                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
//                        price = it
//                    }
//                },
//                label = { Text("Price (KSh)") },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.AttachMoney,
//                        contentDescription = null
//                    )
//                },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true,
////                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
////                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
////                )
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Price Type
//            Text(
//                text = "Price Type",
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(bottom = 4.dp)
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                RadioButton(
//                    selected = priceType == PriceType.FIXED,
//                    onClick = { priceType = PriceType.FIXED }
//                )
//
//                Text(
//                    text = "Fixed Price",
//                    style = MaterialTheme.typography.bodyMedium,
//                    modifier = Modifier.padding(start = 4.dp)
//                )
//
//                Spacer(modifier = Modifier.width(24.dp))
//
//                RadioButton(
//                    selected = priceType == PriceType.HOURLY,
//                    onClick = { priceType = PriceType.HOURLY }
//                )
//
//                Text(
//                    text = "Hourly Rate",
//                    style = MaterialTheme.typography.bodyMedium,
//                    modifier = Modifier.padding(start = 4.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Duration
//            OutlinedTextField(
//                value = estimatedDuration,
//                onValueChange = {
//                    // Only allow numeric input
//                    if (it.isEmpty() || it.matches(Regex("^\\d+$"))) {
//                        estimatedDuration = it
//                    }
//                },
//                label = { Text("Estimated Duration (minutes)") },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Timer,
//                        contentDescription = null
//                    )
//                },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true,
//
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Submit Button
//            Button(
//                onClick = {
//                    // Validate inputs
//                    when {
//                        name.isBlank() -> errorMessage = "Please enter a service name"
//                        categoryId.isBlank() -> errorMessage = "Please select a category"
//                        description.isBlank() -> errorMessage = "Please provide a description"
//                        price.isBlank() || price.toDoubleOrNull() == null || price.toDouble() <= 0 ->
//                            errorMessage = "Please enter a valid price"
//                        estimatedDuration.isBlank() || estimatedDuration.toIntOrNull() == null ||
//                                estimatedDuration.toInt() <= 0 ->
//                            errorMessage = "Please enter a valid duration"
//                        userId.isBlank() -> errorMessage = "User not logged in"
//                        else -> {
//                            // Clear error message
//                            errorMessage = null
//                            isSubmitting = true
//
//                            // Create service object
//                            val service = Service(
//                                id = "",  // Will be generated by Firebase
//                                providerId = userId,
//                                categoryId = categoryId,
//                                name = name,
//                                description = description,
//                                price = price.toDouble(),
//                                priceType = priceType,
//                                estimatedDuration = estimatedDuration.toInt()
//                            )
//
//                            // Add service to Firebase
//                            coroutineScope.launch {
//                                serviceViewModel.addService(userId, service)
//                            }
//                        }
//                    }
//                },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = !isSubmitting
//            ) {
//                if (isSubmitting) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(24.dp),
//                        strokeWidth = 2.dp,
//                        color = SafaricomGreen
//                    )
//                } else {
//                    Text("Add Service")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Cancel Button
//            OutlinedButton(
//                onClick = { navController.popBackStack() },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = !isSubmitting
//            ) {
//                Text("Cancel")
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//        }
//    }
//
//    // Success Dialog
//    if (showSuccessDialog) {
//        AlertDialog(
//            onDismissRequest = {
//                showSuccessDialog = false
//                navController.popBackStack()
//            },
//            title = { Text("Service Added") },
//            text = { Text("Your service has been added successfully.") },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        showSuccessDialog = false
//                        navController.popBackStack()
//                    }
//                ) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//}
//
//
//



package com.damiens.mjoba.ui.theme.Screens.Provider

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.PriceType
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.ViewModel.*
import com.damiens.mjoba.ui.theme.SafaricomGreen
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    serviceViewModel: ServiceViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel()
) {
    // Get current user
    val currentUser by authViewModel.currentUser.collectAsState()
    val userId = currentUser?.id ?: return

    // Initialize state
    val categoriesState by categoryViewModel.categoriesState.collectAsState()
    val servicesState by serviceViewModel.servicesState.collectAsState()

    // Form state
    var serviceName by remember { mutableStateOf("") }
    var serviceDescription by remember { mutableStateOf("") }
    var servicePrice by remember { mutableStateOf("") }
    var estimatedDuration by remember { mutableStateOf("60") } // Default 60 minutes
    var categoryId by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf("Select Category") }
    var priceType by remember { mutableStateOf(PriceType.FIXED) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    // UI state
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Load categories when screen is displayed
    LaunchedEffect(Unit) {
        categoryViewModel.loadCategories()
        Log.d("AddServiceScreen", "Loading categories")
    }

    // Monitor service state
    LaunchedEffect(servicesState) {
        when (servicesState) {
            is ServicesUiState.Loading -> {
                isSubmitting = true
            }
            is ServicesUiState.Success -> {
                isSubmitting = false
                Toast.makeText(context, "Service added successfully!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            is ServicesUiState.Error -> {
                isSubmitting = false
                errorMessage = (servicesState as ServicesUiState.Error).message
            }
            else -> {
                // Do nothing for other states
            }
        }
    }

    // Monitor categories state for logging
    LaunchedEffect(categoriesState) {
        when (categoriesState) {
            is CategoriesUiState.Loading -> {
                Log.d("AddServiceScreen", "Categories are loading")
            }
            is CategoriesUiState.Success -> {
                val categoryGroups = (categoriesState as CategoriesUiState.Success).categoryGroups
                Log.d("AddServiceScreen", "Categories loaded successfully. Groups: ${categoryGroups.size}")
                categoryGroups.forEach { (groupId, group) ->
                    Log.d("AddServiceScreen", "Group: $groupId - ${group.title} - Categories: ${group.categories.size}")
                    group.categories.forEach { category ->
                        Log.d("AddServiceScreen", "  - Category: ${category.id} - ${category.name}")
                    }
                }
            }
            is CategoriesUiState.Error -> {
                Log.d("AddServiceScreen", "Error loading categories: ${(categoriesState as CategoriesUiState.Error).message}")
            }
        }
    }

    // Helper function to extract all categories from groups
    fun getAllCategories(): List<CategoryItem> {
        return if (categoriesState is CategoriesUiState.Success) {
            (categoriesState as CategoriesUiState.Success).categoryGroups.values
                .flatMap { it.categories }
        } else {
            emptyList()
        }
    }

    // Reset error message when form changes
    LaunchedEffect(serviceName, categoryId, servicePrice) {
        errorMessage = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Service") },
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
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Error message
            AnimatedVisibility(
                visible = errorMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Service Information Section
            Text(
                text = "Basic Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Service Name
            OutlinedTextField(
                value = serviceName,
                onValueChange = { serviceName = it },
                label = { Text("Service Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection
            Text(
                text = "Category",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Custom dropdown for categories
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Category"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCategoryDropdown = true },
                    label = { Text("Select Category") }
                )

                DropdownMenu(
                    expanded = showCategoryDropdown,
                    onDismissRequest = { showCategoryDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    val allCategories = getAllCategories()

                    if (allCategories.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No categories found") },
                            onClick = { /* Do nothing */ }
                        )
                    } else {
                        // Group categories by their parent group for better organization
                        val categoriesByGroup = if (categoriesState is CategoriesUiState.Success) {
                            (categoriesState as CategoriesUiState.Success).categoryGroups
                        } else {
                            emptyMap()
                        }

                        categoriesByGroup.forEach { (groupId, group) ->
                            if (group.categories.isNotEmpty()) {
                                // Add group header
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = group.title,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    onClick = { /* Group headers are not clickable */ },
                                    enabled = false
                                )

                                // Add categories in this group
                                group.categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category.name) },
                                        onClick = {
                                            categoryId = category.id
                                            categoryName = category.name
                                            showCategoryDropdown = false
                                        }
                                    )
                                }

                                // Add divider between groups
                                if (groupId != categoriesByGroup.keys.last()) {
                                    Divider()
                                }
                            }
                        }
                    }
                }
            }

            // Initialize categories if empty
            if (categoriesState is CategoriesUiState.Success && getAllCategories().isEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            categoryViewModel.initializeAllCategories()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Initialize Categories")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Service Description
            OutlinedTextField(
                value = serviceDescription,
                onValueChange = { serviceDescription = it },
                label = { Text("Description") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Pricing Section
            Text(
                text = "Pricing & Duration",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Price
            OutlinedTextField(
                value = servicePrice,
                onValueChange = {
                    // Only allow numeric input with decimal
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        servicePrice = it
                    }
                },
                label = { Text("Price (KSh)") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Price Type
            Text(
                text = "Price Type",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = priceType == PriceType.FIXED,
                    onClick = { priceType = PriceType.FIXED },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = SafaricomGreen
                    )
                )

                Text(
                    text = "Fixed Price",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .clickable { priceType = PriceType.FIXED }
                        .padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.width(24.dp))

                RadioButton(
                    selected = priceType == PriceType.HOURLY,
                    onClick = { priceType = PriceType.HOURLY },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = SafaricomGreen
                    )
                )

                Text(
                    text = "Hourly Rate",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .clickable { priceType = PriceType.HOURLY }
                        .padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration
            OutlinedTextField(
                value = estimatedDuration,
                onValueChange = {
                    // Only allow numeric input
                    if (it.isEmpty() || it.matches(Regex("^\\d+$"))) {
                        estimatedDuration = it
                    }
                },
                label = { Text("Estimated Duration (minutes)") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    // Validate inputs
                    when {
                        serviceName.isBlank() -> errorMessage = "Please enter a service name"
                        categoryId.isBlank() -> errorMessage = "Please select a category"
                        servicePrice.isBlank() || servicePrice.toDoubleOrNull() == null || servicePrice.toDouble() <= 0 ->
                            errorMessage = "Please enter a valid price"
                        estimatedDuration.isBlank() || estimatedDuration.toIntOrNull() == null ||
                                estimatedDuration.toInt() <= 0 ->
                            errorMessage = "Please enter a valid duration"
                        else -> {
                            // Create service object
                            val service = Service(
                                id = "",  // Will be generated by Firebase
                                providerId = userId,
                                categoryId = categoryId,
                                name = serviceName,
                                description = serviceDescription,
                                price = servicePrice.toDouble(),
                                priceType = priceType,
                                estimatedDuration = estimatedDuration.toInt()
                            )

                            // Add service
                            serviceViewModel.addService(userId, service)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SafaricomGreen,
                    disabledContainerColor = SafaricomGreen.copy(alpha = 0.5f)
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Add Service")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add sample services (for testing/demo)
            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        if (userId.isNotEmpty()) {
                            serviceViewModel.populateMultipleServices(userId)
                        } else {
                            errorMessage = "User not logged in"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting
            ) {
                Text("Add Sample Services")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel Button
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isSubmitting
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}