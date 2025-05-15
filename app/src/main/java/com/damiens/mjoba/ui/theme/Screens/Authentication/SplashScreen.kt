package com.damiens.mjoba.ui.theme.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.R
import com.damiens.mjoba.ViewModel.AuthState
import com.damiens.mjoba.ViewModel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    onCheckAuthComplete: (Boolean) -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Check auth status as soon as the screen is displayed
    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()

        // Wait for a minimum splash display time
        delay(1500)

        // Determine if user is authenticated and navigate accordingly
        val isAuthenticated = currentUser != null && authState is AuthState.Authenticated
        onCheckAuthComplete(isAuthenticated)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // App logo or splash image
        Image(
            painter = painterResource(id = R.drawable.app_ogo), // Make sure you have this resource
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )

        // Loading indicator at bottom
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 64.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}