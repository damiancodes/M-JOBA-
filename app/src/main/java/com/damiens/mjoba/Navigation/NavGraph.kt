package com.damiens.mjoba.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.damiens.mjoba.Model.User
import com.damiens.mjoba.ui.theme.Screens.Booking.BookingScreen
import com.damiens.mjoba.ui.theme.Screens.CategoryDetails.CategoryDetailsScreen
import com.damiens.mjoba.ui.theme.Screens.Home.HomeScreen
import com.damiens.mjoba.ui.theme.Screens.Orders.BookingDetailsScreen
import com.damiens.mjoba.ui.theme.Screens.Orders.BookingsListScreen
import com.damiens.mjoba.ui.theme.Screens.Payment.PaymentScreen
import com.damiens.mjoba.ui.theme.Screens.Payment.PaymentConfirmationScreen
import com.damiens.mjoba.ui.theme.Screens.Profile.EditProfileScreen
import com.damiens.mjoba.ui.theme.Screens.Profile.UserProfileScreen
import com.damiens.mjoba.ui.theme.Screens.ServiceProviderDetailsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object CategoryDetails : Screen("category_details/{categoryId}") {
        fun createRoute(categoryId: String) = "category_details/$categoryId"
    }
    object ServiceDetails : Screen("service_details/{serviceId}") {
        fun createRoute(serviceId: String) = "service_details/$serviceId"
    }
    object BookService : Screen("book_service/{serviceId}") {
        fun createRoute(serviceId: String) = "book_service/$serviceId"
    }
    object Profile : Screen("profile")
    object EditProfile : Screen("edit-profile") {
        fun createRoute() = "edit-profile"
    }
    object ProviderDetails : Screen("provider_details/{providerId}") {
        fun createRoute(providerId: String) = "provider_details/$providerId"
    }
    object Bookings : Screen("bookings")
    object Payment : Screen("payment/{providerId}/{serviceId}/{date}/{time}") {
        fun createRoute(providerId: String, serviceId: String, date: String, time: String) =
            "payment/$providerId/$serviceId/$date/$time"
    }
    object PaymentConfirmation : Screen("payment-confirmation/{providerId}/{serviceId}/{amount}") {
        fun createRoute(providerId: String, serviceId: String, amount: String) =
            "payment-confirmation/$providerId/$serviceId/$amount"
    }
    // In NavGraph.kt, add these to your Screen sealed class
    object BookingDetails : Screen("booking_details/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_details/$bookingId"
    }
}

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = Screen.Home.route) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Profile.route) {
            UserProfileScreen(navController = navController)
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }

        composable(
            route = Screen.CategoryDetails.route,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryDetailsScreen(navController = navController, categoryId = categoryId)
        }

        composable(
            route = Screen.ProviderDetails.route,
            arguments = listOf(
                navArgument("providerId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val providerId = backStackEntry.arguments?.getString("providerId") ?: ""
            ServiceProviderDetailsScreen(navController = navController, providerId = providerId)
        }

        composable(
            route = Screen.BookService.route,
            arguments = listOf(
                navArgument("serviceId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
            BookingScreen(navController = navController, serviceId = serviceId)
        }

        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                navArgument("providerId") { type = NavType.StringType },
                navArgument("serviceId") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val providerId = backStackEntry.arguments?.getString("providerId") ?: ""
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val time = backStackEntry.arguments?.getString("time") ?: ""

            PaymentScreen(
                navController = navController,
                providerId = providerId,
                serviceId = serviceId,
                date = date,
                time = time
            )
        }

        composable(
            route = Screen.PaymentConfirmation.route,
            arguments = listOf(
                navArgument("providerId") { type = NavType.StringType },
                navArgument("serviceId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val providerId = backStackEntry.arguments?.getString("providerId") ?: ""
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
            val amount = backStackEntry.arguments?.getString("amount") ?: ""

            PaymentConfirmationScreen(
                navController = navController,
                providerId = providerId,
                serviceId = serviceId,
                amount = amount
            )
        }
        composable(Screen.Bookings.route) {
            BookingsListScreen(navController = navController)
        }

        composable(
            route = Screen.BookingDetails.route,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            BookingDetailsScreen(navController = navController, bookingId = bookingId)
        }
    }
}