//package com.damiens.mjoba.ui.theme.Screens.Profile
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import com.damiens.mjoba.Model.User
//import com.damiens.mjoba.Navigation.Screen
//import com.damiens.mjoba.R
//import com.damiens.mjoba.ViewModel.AuthViewModel
//import com.damiens.mjoba.util.LocationSelectionField
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditProfileScreen(
//    navController: NavHostController,
//    authViewModel: AuthViewModel = viewModel()
//) {
//    // Get current user from ViewModel
//    val currentUser by authViewModel.currentUser.collectAsState()
//
//    // Use current user or sample data if not available
//    val user = currentUser ?: getSampleUser(null)
//    val scrollState = rememberScrollState()
//
//    // State for form fields
//    var name by remember { mutableStateOf(user.name) }
//    var email by remember { mutableStateOf(user.email) }
//    var phone by remember { mutableStateOf(user.phone) }
//    var address by remember { mutableStateOf(user.address) }
//    var profileImageUrl by remember { mutableStateOf(user.profileImageUrl) }
//
//    var isProfileUpdating by remember { mutableStateOf(false) }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Edit Profile") },
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
//        ) {
//            // Profile Picture Section
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.primaryContainer)
//                    .padding(vertical = 24.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    // Profile picture with AsyncImage
//                    if (profileImageUrl.isNotEmpty()) {
//                        AsyncImage(
//                            model = ImageRequest.Builder(LocalContext.current)
//                                .data(profileImageUrl)
//                                .crossfade(true)
//                                .build(),
//                            contentDescription = "Profile Photo",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .size(120.dp)
//                                .clip(CircleShape)
//                                .background(MaterialTheme.colorScheme.primary)
//                        )
//                    } else {
//                        // Fallback to app logo if no profile image
//                        Box(
//                            modifier = Modifier
//                                .size(120.dp)
//                                .clip(CircleShape)
//                                .background(MaterialTheme.colorScheme.primary)
//                                .padding(4.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            AsyncImage(
//                                model = R.drawable.mjoblogo,
//                                contentDescription = "Profile Logo",
//                                modifier = Modifier.size(80.dp)
//                            )
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    OutlinedButton(
//                        onClick = {
//                            // This would open an image picker in a real app
//                            // For now, we'll simulate changing the profile image
//                            // In a real implementation, you would upload to Firebase Storage
//                            profileImageUrl = "https://randomuser.me/api/portraits/men/1.jpg"
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.PhotoCamera,
//                            contentDescription = null
//                        )
//
//                        Spacer(modifier = Modifier.width(8.dp))
//
//                        Text("Change Photo")
//                    }
//                }
//            }
//
//            // Form Fields
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                Text(
//                    text = "Personal Information",
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                // Name Field
//                OutlinedTextField(
//                    value = name,
//                    onValueChange = { name = it },
//                    label = { Text("Full Name") },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.Person,
//                            contentDescription = null
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Email Field
//                OutlinedTextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text("Email") },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.Email,
//                            contentDescription = null
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Phone Field
//                OutlinedTextField(
//                    value = phone,
//                    onValueChange = { phone = it },
//                    label = { Text("Phone Number") },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.Phone,
//                            contentDescription = null
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Address Field
//                OutlinedTextField(
//                    value = address,
//                    onValueChange = { address = it },
//                    label = { Text("Address") },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.LocationOn,
//                            contentDescription = null
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(32.dp))
//
//                // In your form fields section
//                LocationSelectionField(
//                    value = address,
//                    onValueChange = { address = it },
//                    onLocationSelected = { addressText, geoPoint ->
//                        // Store both text address and coordinates
//                        address = addressText
//                        // In a ViewModel, you would save geoPoint to the user's profile
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(32.dp))
//
//                // Account Type Section
//                Text(
//                    text = "Account Type",
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                // Account Type Switch
//                var isServiceProvider by remember { mutableStateOf(user.isServiceProvider) }
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column(modifier = Modifier.weight(1f)) {
//                        Text(
//                            text = "Service Provider Account",
//                            style = MaterialTheme.typography.bodyLarge,
//                            fontWeight = FontWeight.Bold
//                        )
//
//                        Text(
//                            text = "Enable to offer services on M-Joba",
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    }
//
//                    Switch(
//                        checked = isServiceProvider,
//                        onCheckedChange = { isServiceProvider = it }
//                    )
//                }
//
//                if (isServiceProvider) {
//                    Text(
//                        text = "As a service provider, you will need to complete your business profile to start offering services.",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.secondary,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//
//                    OutlinedButton(
//                        onClick = {
//                            // Navigate to the provider setup screen
//                            // For now, we'll just show the success dialog
//                            showSuccessDialog = true
//                        },
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    ) {
//                        Text("Setup Provider Profile")
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(32.dp))
//
//                // Save Button
//                Button(
//                    onClick = {
//                        // Save profile changes using AuthViewModel
//                        isProfileUpdating = true
//
//                        // In a real app, you would update the user profile in Firebase
//                        // For now, we'll simulate the API call
//                        authViewModel.updateUserProfile(
//                            name = name,
//                            email = email,
//                            phone = phone,
//                            address = address,
//                            profileImageUrl = profileImageUrl,
//                            isServiceProvider = isServiceProvider
//                        )
//
//                        // Simulate API call delay
//                        android.os.Handler().postDelayed({
//                            isProfileUpdating = false
//                            showSuccessDialog = true
//                        }, 1500)
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    enabled = !isProfileUpdating
//                ) {
//                    if (isProfileUpdating) {
//                        CircularProgressIndicator(
//                            color = MaterialTheme.colorScheme.onPrimary,
//                            modifier = Modifier.size(24.dp),
//                            strokeWidth = 2.dp
//                        )
//                    } else {
//                        Text("Save Changes")
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Cancel Button
//                OutlinedButton(
//                    onClick = { navController.popBackStack() },
//                    modifier = Modifier.fillMaxWidth(),
//                    enabled = !isProfileUpdating
//                ) {
//                    Text("Cancel")
//                }
//
//                Spacer(modifier = Modifier.height(32.dp))
//            }
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
//            title = { Text("Profile Updated") },
//            text = { Text("Your profile has been updated successfully.") },
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
//// Use the same getSampleUser function from UserProfileScreen
//private fun getSampleUser(userId: String?): User {
//    return User(
//        id = userId ?: "u123",
//        name = "Damien padri",
//        email = "damien.padri@example.com",
//        phone = "+254 712 345 678",
//        profileImageUrl = "",
//        address = "123 Main St, Nairobi",
//        isServiceProvider = true,
//        dateCreated = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000) // 30 days ago
//    )
//}


// Updated EditProfileScreen with proper Firebase integration

package com.damiens.mjoba.ui.theme.Screens.Profile

import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.damiens.mjoba.Model.GeoPoint
import com.damiens.mjoba.Model.User
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.R
import com.damiens.mjoba.ViewModel.AuthState
import com.damiens.mjoba.ViewModel.AuthViewModel
import com.damiens.mjoba.util.LocationSelectionField
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.damiens.mjoba.Data.remote.StorageRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    // Get current user from ViewModel
    val currentUser by authViewModel.currentUser.collectAsState()
    val authState by authViewModel.authState.collectAsState()

    // Create storage repository for profile image uploads
    val storageRepository = remember { StorageRepository() }

    // Context for image picker
    val context = LocalContext.current

    // Use current user or empty profile if not available
    val user = currentUser ?: User(
        id = "",
        name = "",
        email = "",
        phone = "",
        profileImageUrl = "",
        dateCreated = System.currentTimeMillis()
    )

    val scrollState = rememberScrollState()

    // State for form fields
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }
    var address by remember { mutableStateOf(user.address ?: "") }
    var profileImageUrl by remember { mutableStateOf(user.profileImageUrl) }
    var location by remember { mutableStateOf(user.location) }
    var isServiceProvider by remember { mutableStateOf(user.isServiceProvider) }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri ->
            // User has selected an image, upload to Firebase Storage
            uploadProfileImage(
                storageRepository = storageRepository,
                userId = user.id,
                imageUri = selectedImageUri,
                onSuccess = { downloadUrl ->
                    profileImageUrl = downloadUrl
                },
                onError = { error ->
                    errorMessage = "Failed to upload image: $error"
                }
            )
        }
    }

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                // Profile update successful
                if (errorMessage == null) {
                    showSuccessDialog = true
                }
            }
            is AuthState.Error -> {
                errorMessage = (authState as AuthState.Error).message
            }
            else -> {
                // Do nothing for other states
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Profile") },
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
        ) {
            // Error message
            if (errorMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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

            // Profile Picture Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile picture with AsyncImage
                    if (profileImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(profileImageUrl)
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
                        // Fallback to app logo if no profile image
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Icon",
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            // Launch the image picker
                            imagePickerLauncher.launch("image/*")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Change Photo")
                    }
                }
            }

            // Form Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Personal Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Field
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Location Selection Field
                LocationSelectionField(
                    value = address,
                    onValueChange = { address = it },
                    onLocationSelected = { addressText, geoPoint ->
                        address = addressText
                        location = geoPoint
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Account Type Section
                Text(
                    text = "Account Type",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Account Type Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Service Provider Account",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Enable to offer services on TaskNow",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Switch(
                        checked = isServiceProvider,
                        onCheckedChange = { isServiceProvider = it }
                    )
                }

                if (isServiceProvider) {
                    Text(
                        text = "As a service provider, you will need to complete your business profile to start offering services.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            if (user.id.isNotEmpty()) {
                                // Save changes first
                                updateUserProfile(
                                    authViewModel = authViewModel,
                                    name = name,
                                    email = email,
                                    phone = phone,
                                    address = address,
                                    profileImageUrl = profileImageUrl,
                                    isServiceProvider = true,
                                    location = location
                                )

                                // Navigate to provider setup
                                navController.navigate(Screen.ProviderDashboard.route)
                            } else {
                                errorMessage = "Please sign in before setting up provider profile"
                            }
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Setup Provider Profile")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Save Button
                Button(
                    onClick = {
                        // Reset error message
                        errorMessage = null

                        // Update profile using AuthViewModel
                        updateUserProfile(
                            authViewModel = authViewModel,
                            name = name,
                            email = email,
                            phone = phone,
                            address = address,
                            profileImageUrl = profileImageUrl,
                            isServiceProvider = isServiceProvider,
                            location = location
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = authState !is AuthState.Loading && currentUser != null
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Save Changes")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cancel Button
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = authState !is AuthState.Loading
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                navController.popBackStack()
            },
            title = { Text("Profile Updated") },
            text = { Text("Your profile has been updated successfully.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

// Helper function to update user profile
private fun updateUserProfile(
    authViewModel: AuthViewModel,
    name: String,
    email: String,
    phone: String,
    address: String,
    profileImageUrl: String,
    isServiceProvider: Boolean,
    location: GeoPoint?
) {
    authViewModel.updateUserProfile(
        name = name,
        email = email,
        phone = phone,
        address = address,
        profileImageUrl = profileImageUrl,
        isServiceProvider = isServiceProvider,
        location = location
    )
}

// Helper function to upload profile image
private fun uploadProfileImage(
    storageRepository: StorageRepository,
    userId: String,
    imageUri: Uri,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    if (userId.isEmpty()) {
        onError("User ID is missing")
        return
    }

    // Launch a coroutine to upload the image
    kotlinx.coroutines.MainScope().launch {
        try {
            val result = storageRepository.uploadUserProfileImage(userId, imageUri)

            result.fold(
                onSuccess = { downloadUrl ->
                    onSuccess(downloadUrl)
                },
                onFailure = { exception ->
                    onError(exception.message ?: "Failed to upload image")
                }
            )
        } catch (e: Exception) {
            onError(e.message ?: "An error occurred while uploading the image")
        }
    }
}