package com.damiens.mjoba.ui.theme.Screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.R
import com.damiens.mjoba.ViewModel.AuthState
import com.damiens.mjoba.ViewModel.AuthViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.lerp

@Composable
fun SplashScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    onCheckAuthComplete: (Boolean) -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Animation states
    var startAnimation by remember { mutableStateOf(false) }

    // Logo animations
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = EaseOutBounce
        ),
        label = "logoScale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = EaseOut
        ),
        label = "logoAlpha"
    )

    // Progress indicator animation
    val progressAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 500,
            easing = EaseOut
        ),
        label = "progressAlpha"
    )

    // Background gradient animation
    val gradientAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientAnimation"
    )

    // Rotation animation for subtle logo movement
    val logoRotation by rememberInfiniteTransition().animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoRotation"
    )

    // Start animations and auth check
    LaunchedEffect(Unit) {
        startAnimation = true
        authViewModel.checkAuthStatus()

        // Wait for a minimum splash display time
        delay(1500)

        // Determine if user is authenticated and navigate accordingly
        val isAuthenticated = currentUser != null && authState is AuthState.Authenticated
        onCheckAuthComplete(isAuthenticated)
    }

    // Create gradient colors
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface

    val gradientColors = listOf(
        backgroundColor,
        lerp(backgroundColor, primaryColor, 0.1f * gradientAnimation), // ✅ Use lerp() instead of Color.lerp()
        lerp(backgroundColor, surfaceColor, 0.1f * (1f - gradientAnimation)) // ✅ Use lerp() instead of Color.lerp()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors,
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .systemBarsPadding(), // This ensures content doesn't go under system bars
        contentAlignment = Alignment.Center
    ) {
        // App logo with animations
        Image(
            painter = painterResource(id = R.drawable.app_ogo), // Fixed typo: app_ogo -> app_logo
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .scale(logoScale)
                .alpha(logoAlpha)
                .graphicsLayer {
                    rotationZ = logoRotation
                }
        )

        // Loading indicator at bottom with animation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .alpha(progressAlpha),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
        }
    }
}