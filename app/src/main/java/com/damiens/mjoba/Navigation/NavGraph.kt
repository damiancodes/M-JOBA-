//package com.damiens.mjoba.Navigation
//
//import androidx.compose.runtime.*
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.navArgument
//import com.damiens.mjoba.Model.User
//import com.damiens.mjoba.ViewModel.AuthState
//import com.damiens.mjoba.ViewModel.AuthViewModel
//import com.damiens.mjoba.ui.theme.Screens.Authentication.ForgotPasswordScreen
//import com.damiens.mjoba.ui.theme.Screens.Authentication.LoginScreen
//import com.damiens.mjoba.ui.theme.Screens.Authentication.RegisterScreen
//import com.damiens.mjoba.ui.theme.Screens.Booking.BookingScreen
//import com.damiens.mjoba.ui.theme.Screens.CategoryDetails.CategoryDetailsScreen
//import com.damiens.mjoba.ui.theme.Screens.CategoryList.CategoryListScreen
//import com.damiens.mjoba.ui.theme.Screens.Home.HomeScreen
//import com.damiens.mjoba.ui.theme.Screens.Maps.LocationSelectionScreen
//import com.damiens.mjoba.ui.theme.Screens.Map.NearbyProvidersScreen
//import com.damiens.mjoba.ui.theme.Screens.Orders.BookingDetailsScreen
//import com.damiens.mjoba.ui.theme.Screens.Orders.BookingsListScreen
//import com.damiens.mjoba.ui.theme.Screens.Payment.PaymentScreen
//import com.damiens.mjoba.ui.theme.Screens.Payment.PaymentConfirmationScreen
//import com.damiens.mjoba.ui.theme.Screens.Profile.EditProfileScreen
//import com.damiens.mjoba.ui.theme.Screens.Profile.UserProfileScreen
//import com.damiens.mjoba.ui.theme.Screens.ServiceProviderDetailsScreen
//import com.damiens.mjoba.ui.theme.Screens.SplashScreen
//import com.damiens.mjoba.ui.theme.Screens.ProviderDashboardScreen
//
//sealed class Screen(val route: String) {
//    object Login : Screen("login")
//    object Register : Screen("register")
//    object Home : Screen("home")
//    object Splash : Screen("splash")
//    object ForgotPassword : Screen("forgot_password")
//    object CategoryDetails : Screen("category_details/{categoryId}") {
//        fun createRoute(categoryId: String) = "category_details/$categoryId"
//    }
//    object CategoryList : Screen("category_list/{categoryGroupId}") {
//        fun createRoute(categoryGroupId: String) = "category_list/$categoryGroupId"
//    }
//    object ServiceDetails : Screen("service_details/{serviceId}") {
//        fun createRoute(serviceId: String) = "service_details/$serviceId"
//    }
//    object BookService : Screen("book_service/{serviceId}") {
//        fun createRoute(serviceId: String) = "book_service/$serviceId"
//    }
//
//    object Profile : Screen("profile")
//    object EditProfile : Screen("edit-profile") {
//        fun createRoute() = "edit-profile"
//    }
//    object ProviderDetails : Screen("provider_details/{providerId}") {
//        fun createRoute(providerId: String) = "provider_details/$providerId"
//    }
//    object Bookings : Screen("bookings")
//    object Payment : Screen("payment/{providerId}/{serviceId}/{date}/{time}") {
//        fun createRoute(providerId: String, serviceId: String, date: String, time: String) =
//            "payment/$providerId/$serviceId/$date/$time"
//    }
//    object PaymentConfirmation : Screen("payment-confirmation/{providerId}/{serviceId}/{amount}") {
//        fun createRoute(providerId: String, serviceId: String, amount: String) =
//            "payment-confirmation/$providerId/$serviceId/$amount"
//    }
//    object BookingDetails : Screen("booking_details/{bookingId}") {
//        fun createRoute(bookingId: String) = "booking_details/$bookingId"
//    }
//    object NearbyProviders : Screen("nearby_providers/{categoryId}") {
//        fun createRoute(categoryId: String) = "nearby_providers/$categoryId"
//    }
//    // New route for LocationSelectionScreen
//    object LocationSelection : Screen("location_selection") {
//        fun createRoute() = "location_selection"
//    }
//    object ProviderDashboard : Screen("provider_dashboard") {
//        fun createRoute() = "provider_dashboard"
//    }
//    object AddService : Screen("add_service") {
//        fun createRoute() = "add_service"
//    }
//}
//
//@Composable
//fun NavGraph(
//    navController: NavHostController,
//    startDestination: String = Screen.Splash.route,
//    authViewModel: AuthViewModel = viewModel()
//) {
//    // Get current user from ViewModel
//    val currentUser by authViewModel.currentUser.collectAsState()
//    val authState by authViewModel.authState.collectAsState()
//
//    // Check auth status when NavGraph is first composed
//    LaunchedEffect(Unit) {
//        authViewModel.checkAuthStatus()
//    }
//
//    // List of routes that don't require authentication
//    val publicRoutes = listOf(
//        Screen.Login.route,
//        Screen.Register.route,
//        Screen.ForgotPassword.route,
//        Screen.Splash.route
//    )
//
//    NavHost(
//        navController = navController,
//        startDestination = startDestination
//    ) {
//        composable(Screen.Home.route) {
//            // Check if user is authenticated before showing Home screen
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.Home.route) { inclusive = true }
//                    }
//                }
//            } else {
//                HomeScreen(navController)
//            }
//        }
//
//        composable(Screen.Profile.route) {
//            // Check if user is authenticated before showing Profile screen
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.Profile.route) { inclusive = true }
//                    }
//                }
//            } else {
//                UserProfileScreen(navController = navController, viewModel = authViewModel)
//            }
//        }
//
//        composable(Screen.EditProfile.route) {
//            // Check if user is authenticated before showing EditProfile screen
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.EditProfile.route) { inclusive = true }
//                    }
//                }
//            } else {
//                EditProfileScreen(navController = navController)
//            }
//        }
//
//        composable(Screen.Login.route) {
//            // If already authenticated, navigate to Home
//            if (currentUser != null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Home.route) {
//                        popUpTo(Screen.Login.route) { inclusive = true }
//                    }
//                }
//            } else {
//                LoginScreen(navController = navController, viewModel = authViewModel)
//            }
//        }
//
//        composable(Screen.Register.route) {
//            // If already authenticated, navigate to Home
//            if (currentUser != null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Home.route) {
//                        popUpTo(Screen.Register.route) { inclusive = true }
//                    }
//                }
//            } else {
//                RegisterScreen(navController = navController, viewModel = authViewModel)
//            }
//        }
//
//        composable(Screen.ForgotPassword.route) {
//            ForgotPasswordScreen(navController = navController, viewModel = authViewModel)
//        }
//
//        composable(Screen.Splash.route) {
//            SplashScreen(
//                navController = navController,
//                onCheckAuthComplete = { isAuthenticated ->
//                    if (isAuthenticated) {
//                        navController.navigate(Screen.Home.route) {
//                            popUpTo(Screen.Splash.route) { inclusive = true }
//                        }
//                    } else {
//                        navController.navigate(Screen.Login.route) {
//                            popUpTo(Screen.Splash.route) { inclusive = true }
//                        }
//                    }
//                }
//            )
//        }
//
//        composable(
//            route = Screen.CategoryDetails.route,
//            arguments = listOf(
//                navArgument("categoryId") {
//                    type = NavType.StringType
//                }
//            )
//        ) { backStackEntry ->
//            // Check if user is authenticated
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.CategoryDetails.route) { inclusive = true }
//                    }
//                }
//            } else {
//                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
//                CategoryDetailsScreen(navController = navController, categoryId = categoryId)
//            }
//        }
//
//        // Add the new CategoryList route
//        composable(
//            route = Screen.CategoryList.route,
//            arguments = listOf(
//                navArgument("categoryGroupId") {
//                    type = NavType.StringType
//                }
//            )
//        ) { backStackEntry ->
//            // Check if user is authenticated
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.CategoryList.route) { inclusive = true }
//                    }
//                }
//            } else {
//                val categoryGroupId = backStackEntry.arguments?.getString("categoryGroupId") ?: ""
//                CategoryListScreen(navController = navController, categoryGroupId = categoryGroupId)
//            }
//        }
//
//        composable(
//            route = Screen.ProviderDetails.route,
//            arguments = listOf(
//                navArgument("providerId") {
//                    type = NavType.StringType
//                }
//            )
//        ) { backStackEntry ->
//            // Check if user is authenticated
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.ProviderDetails.route) { inclusive = true }
//                    }
//                }
//            } else {
//                val providerId = backStackEntry.arguments?.getString("providerId") ?: ""
//                ServiceProviderDetailsScreen(navController = navController, providerId = providerId)
//            }
//        }
//
//        composable(
//            route = Screen.BookService.route,
//            arguments = listOf(
//                navArgument("serviceId") {
//                    type = NavType.StringType
//                }
//            )
//        ) { backStackEntry ->
//            // Check if user is authenticated
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.BookService.route) { inclusive = true }
//                    }
//                }
//            } else {
//                val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
//                BookingScreen(navController = navController, serviceId = serviceId)
//            }
//        }
//
//        composable(
//            route = Screen.Payment.route,
//            arguments = listOf(
//                navArgument("providerId") { type = NavType.StringType },
//                navArgument("serviceId") { type = NavType.StringType },
//                navArgument("date") { type = NavType.StringType },
//                navArgument("time") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            // Check if user is authenticated
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.Payment.route) { inclusive = true }
//                    }
//                }
//            } else {
//                val providerId = backStackEntry.arguments?.getString("providerId") ?: ""
//                val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
//                val date = backStackEntry.arguments?.getString("date") ?: ""
//                val time = backStackEntry.arguments?.getString("time") ?: ""
//
//                PaymentScreen(
//                    navController = navController,
//                    providerId = providerId,
//                    serviceId = serviceId,
//                    date = date,
//                    time = time
//                )
//            }
//        }
//
//        composable(
//            route = Screen.PaymentConfirmation.route,
//            arguments = listOf(
//                navArgument("providerId") { type = NavType.StringType },
//                navArgument("serviceId") { type = NavType.StringType },
//                navArgument("amount") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            // Check if user is authenticated
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.PaymentConfirmation.route) { inclusive = true }
//                    }
//                }
//            } else {
//                val providerId = backStackEntry.arguments?.getString("providerId") ?: ""
//                val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
//                val amount = backStackEntry.arguments?.getString("amount") ?: ""
//
//                PaymentConfirmationScreen(
//                    navController = navController,
//                    providerId = providerId,
//                    serviceId = serviceId,
//                    amount = amount
//                )
//            }
//        }
//
//        composable(Screen.Bookings.route) {
//            // Check if user is authenticated
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.Bookings.route) { inclusive = true }
//                    }
//                }
//            } else {
//                BookingsListScreen(navController = navController)
//            }
//        }
//
//        composable(
//            route = Screen.NearbyProviders.route,
//            arguments = listOf(
//                navArgument("categoryId") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            // Check if user is authenticated
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.NearbyProviders.route) { inclusive = true }
//                    }
//                }
//            } else {
//                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
//                NearbyProvidersScreen(navController = navController, categoryId = categoryId)
//            }
//        }
//
//        composable(
//            route = Screen.BookingDetails.route,
//            arguments = listOf(
//                navArgument("bookingId") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            // Check if user is authenticated
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.BookingDetails.route) { inclusive = true }
//                    }
//                }
//            } else {
//                val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
//                BookingDetailsScreen(navController = navController, bookingId = bookingId)
//            }
//        }
//
//        // Add the LocationSelectionScreen route
//        composable(route = Screen.LocationSelection.route) {
//            // Check if user is authenticated
//            if (currentUser == null && authState !is AuthState.Loading) {
//                LaunchedEffect(Unit) {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.LocationSelection.route) { inclusive = true }
//                    }
//                }
//            } else {
//                LocationSelectionScreen(navController = navController)
//            }
//        }
//
//        composable(Screen.ProviderDashboard.route) {
//            ProviderDashboardScreen(navController = navController)
//        }
//
//        composable(Screen.AddService.route) {
//            AddServiceScreen(navController = navController)
//        }
//    }
//}
//





package com.damiens.mjoba.Navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.damiens.mjoba.Model.User
import com.damiens.mjoba.ViewModel.AuthState
import com.damiens.mjoba.ViewModel.AuthViewModel
import com.damiens.mjoba.ui.theme.Screens.Admin.AdminDashboardScreen
import com.damiens.mjoba.ui.theme.Screens.Admin.ManageCategoriesScreen
import com.damiens.mjoba.ui.theme.Screens.Admin.ManageProvidersScreen
import com.damiens.mjoba.ui.theme.Screens.Admin.ManageServicesScreen
import com.damiens.mjoba.ui.theme.Screens.Authentication.ForgotPasswordScreen
import com.damiens.mjoba.ui.theme.Screens.Authentication.LoginScreen
import com.damiens.mjoba.ui.theme.Screens.Authentication.RegisterScreen
import com.damiens.mjoba.ui.theme.Screens.Booking.BookingScreen
import com.damiens.mjoba.ui.theme.Screens.CategoryDetails.CategoryDetailsScreen
import com.damiens.mjoba.ui.theme.Screens.CategoryList.CategoryListScreen
import com.damiens.mjoba.ui.theme.Screens.Home.HomeScreen
import com.damiens.mjoba.ui.theme.Screens.Maps.LocationSelectionScreen
import com.damiens.mjoba.ui.theme.Screens.Map.NearbyProvidersScreen
import com.damiens.mjoba.ui.theme.Screens.Orders.BookingDetailsScreen
import com.damiens.mjoba.ui.theme.Screens.Orders.BookingsListScreen
import com.damiens.mjoba.ui.theme.Screens.Payment.PaymentScreen
import com.damiens.mjoba.ui.theme.Screens.Payment.PaymentConfirmationScreen
import com.damiens.mjoba.ui.theme.Screens.Profile.EditProfileScreen
import com.damiens.mjoba.ui.theme.Screens.Profile.UserProfileScreen
import com.damiens.mjoba.ui.theme.Screens.ServiceProviderDetailsScreen
import com.damiens.mjoba.ui.theme.Screens.SplashScreen
import com.damiens.mjoba.ui.theme.Screens.Provider.ProviderDashboardScreen
import com.damiens.mjoba.ui.theme.Screens.Provider.AddServiceScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Splash : Screen("splash")
    object ForgotPassword : Screen("forgot_password")
    object CategoryDetails : Screen("category_details/{categoryId}") {
        fun createRoute(categoryId: String) = "category_details/$categoryId"
    }
    object CategoryList : Screen("category_list/{categoryGroupId}") {
        fun createRoute(categoryGroupId: String) = "category_list/$categoryGroupId"
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
    object BookingDetails : Screen("booking_details/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_details/$bookingId"
    }
    object NearbyProviders : Screen("nearby_providers/{categoryId}") {
        fun createRoute(categoryId: String) = "nearby_providers/$categoryId"
    }
    // New route for LocationSelectionScreen
    object LocationSelection : Screen("location_selection") {
        fun createRoute() = "location_selection"
    }
    object ProviderDashboard : Screen("provider_dashboard") {
        fun createRoute() = "provider_dashboard"
    }
    object AddService : Screen("add_service") {
        fun createRoute() = "add_service"
    }
    object AdminDashboard : Screen("admin_dashboard") {
        fun createRoute() = "admin_dashboard"
    }

    object ManageCategories : Screen("manage_categories") {
        fun createRoute() = "manage_categories"
    }

    object ManageProviders : Screen("manage_providers") {
        fun createRoute() = "manage_providers"
    }

    // Add these to your Screen sealed class
    object AdminCategories : Screen("admin_categories") {
        fun createRoute() = "admin_categories"
    }

    object AdminServices : Screen("admin_services") {
        fun createRoute() = "admin_services"
    }

    object AdminProviders : Screen("admin_providers") {
        fun createRoute() = "admin_providers"
    }

    object ManageServices : Screen("manage_services") {
        fun createRoute() = "manage_services"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route,
    authViewModel: AuthViewModel = viewModel()
) {
    // Get current user from ViewModel
    val currentUser by authViewModel.currentUser.collectAsState()
    val authState by authViewModel.authState.collectAsState()

    // Check auth status when NavGraph is first composed
    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
    }

    // List of routes that don't require authentication
    val publicRoutes = listOf(
        Screen.Login.route,
        Screen.Register.route,
        Screen.ForgotPassword.route,
        Screen.Splash.route
    )

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            // Check if user is authenticated before showing Home screen
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            } else {
                HomeScreen(navController)
            }
        }

        composable(Screen.Profile.route) {
            // Check if user is authenticated before showing Profile screen
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            } else {
                UserProfileScreen(navController = navController, authViewModel = authViewModel)
            }
        }

        composable(Screen.EditProfile.route) {
            // Check if user is authenticated before showing EditProfile screen
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.EditProfile.route) { inclusive = true }
                    }
                }
            } else {
                EditProfileScreen(navController = navController)
            }
        }

        composable(Screen.Login.route) {
            // If already authenticated, navigate to Home
            if (currentUser != null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            } else {
                LoginScreen(navController = navController, viewModel = authViewModel)
            }
        }

        composable(Screen.Register.route) {
            // If already authenticated, navigate to Home
            if (currentUser != null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            } else {
                RegisterScreen(navController = navController, viewModel = authViewModel)
            }
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController, viewModel = authViewModel)
        }

        composable(Screen.Splash.route) {
            SplashScreen(
                navController = navController,
                onCheckAuthComplete = { isAuthenticated ->
                    if (isAuthenticated) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        // Admin Dashboard Screen
        composable(route = Screen.AdminDashboard.route) {
            // Check if user is authenticated and has admin privileges
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.AdminDashboard.route) { inclusive = true }
                    }
                }
            } else {
                // Check if the current user is an admin (you'll need to add this field to your User class)
                if (currentUser?.isAdmin == true) {
                    AdminDashboardScreen(navController = navController)
                } else {
                    // If the user is not an admin, redirect to home
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.AdminDashboard.route) { inclusive = true }
                        }
                    }
                }
            }
        }

// Similar composable entries for ManageCategories, ManageProviders, and ManageServices



        composable(
            route = Screen.CategoryDetails.route,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.CategoryDetails.route) { inclusive = true }
                    }
                }
            } else {
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                CategoryDetailsScreen(navController = navController, categoryId = categoryId)
            }
        }

        // Add the new CategoryList route
        composable(
            route = Screen.CategoryList.route,
            arguments = listOf(
                navArgument("categoryGroupId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.CategoryList.route) { inclusive = true }
                    }
                }
            } else {
                val categoryGroupId = backStackEntry.arguments?.getString("categoryGroupId") ?: ""
                CategoryListScreen(navController = navController, categoryGroupId = categoryGroupId)
            }
        }

        composable(
            route = Screen.ProviderDetails.route,
            arguments = listOf(
                navArgument("providerId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ProviderDetails.route) { inclusive = true }
                    }
                }
            } else {
                val providerId = backStackEntry.arguments?.getString("providerId") ?: ""
                ServiceProviderDetailsScreen(navController = navController, providerId = providerId)
            }
        }

        composable(
            route = Screen.BookService.route,
            arguments = listOf(
                navArgument("serviceId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.BookService.route) { inclusive = true }
                    }
                }
            } else {
                val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
                BookingScreen(navController = navController, serviceId = serviceId)
            }
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
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Payment.route) { inclusive = true }
                    }
                }
            } else {
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
        }

        composable(
            route = Screen.PaymentConfirmation.route,
            arguments = listOf(
                navArgument("providerId") { type = NavType.StringType },
                navArgument("serviceId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.PaymentConfirmation.route) { inclusive = true }
                    }
                }
            } else {
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
        }

        composable(Screen.Bookings.route) {
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Bookings.route) { inclusive = true }
                    }
                }
            } else {
                BookingsListScreen(navController = navController)
            }
        }

        composable(
            route = Screen.NearbyProviders.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.NearbyProviders.route) { inclusive = true }
                    }
                }
            } else {
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                NearbyProvidersScreen(navController = navController, categoryId = categoryId)
            }
        }

        composable(
            route = Screen.BookingDetails.route,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.BookingDetails.route) { inclusive = true }
                    }
                }
            } else {
                val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
                BookingDetailsScreen(navController = navController, bookingId = bookingId)
            }
        }

        // Add the LocationSelectionScreen route
        composable(route = Screen.LocationSelection.route) {
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.LocationSelection.route) { inclusive = true }
                    }
                }
            } else {
                LocationSelectionScreen(navController = navController)
            }
        }


        // Add these composable entries to your NavHost
        composable(route = Screen.AdminCategories.route) {
            // Check if user is authenticated and has admin privileges
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.AdminCategories.route) { inclusive = true }
                    }
                }
            } else {
                // Check if the current user is an admin
                if (currentUser?.isAdmin == true) {
                    ManageCategoriesScreen(navController = navController)
                } else {
                    // If the user is not an admin, redirect to home
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.AdminCategories.route) { inclusive = true }
                        }
                    }
                }
            }
        }

        composable(route = Screen.AdminServices.route) {
            // Check if user is authenticated and has admin privileges
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.AdminServices.route) { inclusive = true }
                    }
                }
            } else {
                // Check if the current user is an admin
                if (currentUser?.isAdmin == true) {
                    ManageServicesScreen(navController = navController)
                } else {
                    // If the user is not an admin, redirect to home
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.AdminServices.route) { inclusive = true }
                        }
                    }
                }
            }
        }

        composable(route = Screen.AdminProviders.route) {
            // Check if user is authenticated and has admin privileges
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.AdminProviders.route) { inclusive = true }
                    }
                }
            } else {
                // Check if the current user is an admin
                if (currentUser?.isAdmin == true) {
                    ManageProvidersScreen(navController = navController)
                } else {
                    // If the user is not an admin, redirect to home
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.AdminProviders.route) { inclusive = true }
                        }
                    }
                }
            }
        }

        // Provider Dashboard Screen
        composable(route = Screen.ProviderDashboard.route) {
            // Check if user is authenticated
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ProviderDashboard.route) { inclusive = true }
                    }
                }
            } else {
                // Check if the current user is a service provider
                if (currentUser?.isServiceProvider == true) {
                    ProviderDashboardScreen(navController = navController)
                } else {
                    // If the user is not a service provider, redirect to home
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.ProviderDashboard.route) { inclusive = true }
                        }
                    }
                }
            }
        }

        // Add Service Screen
        composable(route = Screen.AddService.route) {
            // Check if user is authenticated and is a service provider
            if (currentUser == null && authState !is AuthState.Loading) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.AddService.route) { inclusive = true }
                    }
                }
            } else {
                // Check if user is a service provider
                if (currentUser?.isServiceProvider == true) {
                    AddServiceScreen(navController = navController)
                } else {
                    // If not a service provider, redirect to home
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.AddService.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}

