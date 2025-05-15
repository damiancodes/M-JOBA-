//
//package com.damiens.mjoba.ViewModel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.damiens.mjoba.Data.remote.ServiceProviderRepository
//import com.damiens.mjoba.Model.Service
//import com.damiens.mjoba.Model.ServiceProvider
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import android.util.Log
//
//
//class ServiceViewModel : ViewModel() {
//    private val serviceProviderRepository = ServiceProviderRepository()
//
//    // State for services
//    private val _servicesState = MutableStateFlow<ServicesUiState>(ServicesUiState.Initial)
//    val servicesState: StateFlow<ServicesUiState> = _servicesState.asStateFlow()
//
//    // Add a new service for a provider
//    fun addService(providerId: String, service: Service) {
//        // Show loading state
//        _servicesState.value = ServicesUiState.Loading
//
//        Log.d("ServiceViewModel", "Adding service: ${service.name} for provider: $providerId")
//
//        viewModelScope.launch {
//            try {
//                // Call the repository method to add the service
//                val result = serviceProviderRepository.addService(providerId, service)
//
//                result.fold(
//                    onSuccess = { newService ->
//                        Log.d("ServiceViewModel", "Service added successfully: ${newService.id}")
//
//                        // After successfully adding the service, reload the provider's services
//                        // This will update the UI with the latest list including the new service
//                        loadServicesByProvider(providerId)
//                    },
//                    onFailure = { exception ->
//                        Log.e("ServiceViewModel", "Failed to add service: ${exception.message}")
//                        _servicesState.value = ServicesUiState.Error(exception.message ?: "Failed to add service")
//                    }
//                )
//            } catch (e: Exception) {
//                Log.e("ServiceViewModel", "Exception adding service: ${e.message}")
//                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
//            }
//        }
//    }
//
//    // Load services by provider
//    fun loadServicesByProvider(providerId: String) {
//        _servicesState.value = ServicesUiState.Loading
//
//        Log.d("ServiceViewModel", "Loading services for provider: $providerId")
//
//        viewModelScope.launch {
//            try {
//                val result = serviceProviderRepository.getProviderServices(providerId)
//
//                result.fold(
//                    onSuccess = { services ->
//                        Log.d("ServiceViewModel", "Loaded ${services.size} services")
//                        _servicesState.value = ServicesUiState.Success(services)
//                    },
//                    onFailure = { exception ->
//                        Log.e("ServiceViewModel", "Failed to load services: ${exception.message}")
//                        _servicesState.value = ServicesUiState.Error(exception.message ?: "Failed to load services")
//                    }
//                )
//            } catch (e: Exception) {
//                Log.e("ServiceViewModel", "Exception loading services: ${e.message}")
//                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
//            }
//        }
//    }
//
//    // Add this method to your ServiceViewModel.kt file
//
//    /**
//     * Load all services from the repository
//     */
//    fun loadAllServices() {
//        // Update the UI state to Loading
//        _servicesState.value = ServicesUiState.Loading
//
//        viewModelScope.launch {
//            try {
//                // Call the repository method to get all services
//                val result = serviceProviderRepository.getAllServices()
//
//                result.fold(
//                    onSuccess = { services ->
//                        // Update UI state with the loaded services
//                        _servicesState.value = ServicesUiState.Success(services)
//                    },
//                    onFailure = { exception ->
//                        // Update UI state with the error message
//                        _servicesState.value = ServicesUiState.Error(
//                            exception.message ?: "Failed to load services"
//                        )
//                    }
//                )
//            } catch (e: Exception) {
//                // Handle unexpected errors
//                _servicesState.value = ServicesUiState.Error(
//                    e.message ?: "An unexpected error occurred"
//                )
//            }
//        }
//    }
//
//    // Add this method to your ServiceViewModel class
//    fun populateMultipleServices(providerId: String) {
//        _servicesState.value = ServicesUiState.Loading
//
//        viewModelScope.launch {
//            try {
//                // Create a list of sample services across different categories
//                val services = listOf(
//                    // Home Services (Category 101)
//                    Service(
//                        id = "",  // Will be generated by Firebase
//                        providerId = providerId,
//                        categoryId = "101",
//                        name = "Basic House Cleaning",
//                        description = "General cleaning of your home including sweeping, mopping, and dusting",
//                        price = 1500.0,
//                        priceType = com.damiens.mjoba.Model.PriceType.FIXED,
//                        estimatedDuration = 120
//                    ),
//                    Service(
//                        id = "",
//                        providerId = providerId,
//                        categoryId = "101",
//                        name = "Deep House Cleaning",
//                        description = "Thorough cleaning of all surfaces including behind furniture and appliances",
//                        price = 3000.0,
//                        priceType = com.damiens.mjoba.Model.PriceType.FIXED,
//                        estimatedDuration = 240
//                    ),
//
//                    // Laundry Services (Category 102)
//                    Service(
//                        id = "",
//                        providerId = providerId,
//                        categoryId = "102",
//                        name = "Wash & Fold",
//                        description = "Washing, drying, and folding of your clothes",
//                        price = 800.0,
//                        priceType = com.damiens.mjoba.Model.PriceType.FIXED,
//                        estimatedDuration = 120
//                    ),
//
//                    // Beauty Services (Category 201)
//                    Service(
//                        id = "",
//                        providerId = providerId,
//                        categoryId = "201",
//                        name = "Hair Styling",
//                        description = "Professional styling for all occasions",
//                        price = 1200.0,
//                        priceType = com.damiens.mjoba.Model.PriceType.FIXED,
//                        estimatedDuration = 90
//                    ),
//
//                    // Repair Services (Category 301)
//                    Service(
//                        id = "",
//                        providerId = providerId,
//                        categoryId = "301",
//                        name = "Plumbing Repair",
//                        description = "Fix leaky faucets, clogged drains, and other plumbing issues",
//                        price = 2000.0,
//                        priceType = com.damiens.mjoba.Model.PriceType.HOURLY,
//                        estimatedDuration = 120
//                    )
//                )
//
//                // Add each service to Firebase
//                val addedServices = mutableListOf<Service>()
//                for (service in services) {
//                    val result = serviceProviderRepository.addService(providerId, service)
//                    result.fold(
//                        onSuccess = { newService ->
//                            addedServices.add(newService)
//                        },
//                        onFailure = { exception ->
//                            // Log error but continue with other services
//                            Log.e("ServiceViewModel", "Failed to add service ${service.name}: ${exception.message}")
//                        }
//                    )
//                }
//
//                // Update UI state with the added services
//                if (addedServices.isNotEmpty()) {
//                    _servicesState.value = ServicesUiState.Success(addedServices)
//                } else {
//                    _servicesState.value = ServicesUiState.Error("Failed to add any services")
//                }
//
//            } catch (e: Exception) {
//                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
//            }
//        }
//    }
//
//    // Load services by category ID
//    fun loadServicesByCategory(categoryId: String) {
//        _servicesState.value = ServicesUiState.Loading
//
//        Log.d("ServiceViewModel", "Loading services for category: $categoryId")
//
//        viewModelScope.launch {
//            try {
//                // Get all services
//                val allServicesResult = serviceProviderRepository.getAllServices()
//
//                allServicesResult.fold(
//                    onSuccess = { allServices ->
//                        // Filter services by category ID
//                        val categoryServices = allServices.filter { it.categoryId == categoryId }
//                        Log.d("ServiceViewModel", "Loaded ${categoryServices.size} services for category $categoryId")
//                        _servicesState.value = ServicesUiState.Success(categoryServices)
//                    },
//                    onFailure = { exception ->
//                        Log.e("ServiceViewModel", "Failed to load services: ${exception.message}")
//                        _servicesState.value = ServicesUiState.Error(exception.message ?: "Failed to load services")
//                    }
//                )
//            } catch (e: Exception) {
//                Log.e("ServiceViewModel", "Exception loading services: ${e.message}")
//                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
//            }
//        }
//    }
//
//
//    // Load a specific service by ID
//    fun loadServiceById(serviceId: String) {
//        _servicesState.value = ServicesUiState.Loading
//
//        Log.d("ServiceViewModel", "Loading service details for ID: $serviceId")
//
//        viewModelScope.launch {
//            try {
//                // Get all services to find the one we want
//                // This is a workaround until you implement a direct getServiceById method
//                val service = findServiceById(serviceId)
//
//                if (service != null) {
//                    // Now get the provider
//                    val providerResult = serviceProviderRepository.getServiceProviderByUserId(service.providerId)
//
//                    providerResult.fold(
//                        onSuccess = { provider ->
//                            Log.d("ServiceViewModel", "Loaded service and provider successfully")
//                            _servicesState.value = ServicesUiState.ServiceDetails(service, provider)
//                        },
//                        onFailure = { exception ->
//                            Log.e("ServiceViewModel", "Failed to load provider: ${exception.message}")
//
//                            // Create a minimal provider object to display service details
//                            val dummyProvider = ServiceProvider(
//                                userId = service.providerId,
//                                businessName = "Unknown Provider",
//                                description = "",
//                                isVerified = false,
//                                isAvailable = true
//                            )
//
//                            _servicesState.value = ServicesUiState.ServiceDetails(service, dummyProvider)
//                        }
//                    )
//                } else {
//                    _servicesState.value = ServicesUiState.Error("Service not found")
//                }
//            } catch (e: Exception) {
//                Log.e("ServiceViewModel", "Exception loading service details: ${e.message}")
//                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
//            }
//        }
//    }
//
//    // Helper method to find a service by ID
//    // This is a workaround until you implement a direct getServiceById method
//    private suspend fun findServiceById(serviceId: String): Service? {
//        val allServicesResult = serviceProviderRepository.getAllServices()
//
//        return allServicesResult.fold(
//            onSuccess = { services ->
//                services.find { it.id == serviceId }
//            },
//            onFailure = {
//                null
//            }
//        )
//    }
//}


package com.damiens.mjoba.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damiens.mjoba.Data.remote.ServiceProviderRepository
import com.damiens.mjoba.Model.PriceType
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class ServiceViewModel : ViewModel() {
    private val serviceProviderRepository = ServiceProviderRepository()

    // State for services
    private val _servicesState = MutableStateFlow<ServicesUiState>(ServicesUiState.Initial)
    val servicesState: StateFlow<ServicesUiState> = _servicesState.asStateFlow()

    // Add a new service for a provider
    fun addService(providerId: String, service: Service) {
        // Show loading state
        _servicesState.value = ServicesUiState.Loading

        Log.d("ServiceViewModel", "Adding service: ${service.name} for provider: $providerId")

        viewModelScope.launch {
            try {
                // Call the repository method to add the service
                val result = serviceProviderRepository.addService(providerId, service)

                result.fold(
                    onSuccess = { newService ->
                        Log.d("ServiceViewModel", "Service added successfully: ${newService.id}")

                        // After successfully adding the service, reload the provider's services
                        // This will update the UI with the latest list including the new service
                        loadServicesByProvider(providerId)
                    },
                    onFailure = { exception ->
                        Log.e("ServiceViewModel", "Failed to add service: ${exception.message}")
                        _servicesState.value = ServicesUiState.Error(exception.message ?: "Failed to add service")
                    }
                )
            } catch (e: Exception) {
                Log.e("ServiceViewModel", "Exception adding service: ${e.message}")
                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    // Load services by provider
    fun loadServicesByProvider(providerId: String) {
        _servicesState.value = ServicesUiState.Loading

        Log.d("ServiceViewModel", "Loading services for provider: $providerId")

        viewModelScope.launch {
            try {
                val result = serviceProviderRepository.getProviderServices(providerId)

                result.fold(
                    onSuccess = { services ->
                        Log.d("ServiceViewModel", "Loaded ${services.size} services")
                        _servicesState.value = ServicesUiState.Success(services)
                    },
                    onFailure = { exception ->
                        Log.e("ServiceViewModel", "Failed to load services: ${exception.message}")
                        _servicesState.value = ServicesUiState.Error(exception.message ?: "Failed to load services")
                    }
                )
            } catch (e: Exception) {
                Log.e("ServiceViewModel", "Exception loading services: ${e.message}")
                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    /**
     * Load all services from the repository
     */
    fun loadAllServices() {
        // Update the UI state to Loading
        _servicesState.value = ServicesUiState.Loading

        viewModelScope.launch {
            try {
                // Call the repository method to get all services
                val result = serviceProviderRepository.getAllServices()

                result.fold(
                    onSuccess = { services ->
                        // Update UI state with the loaded services
                        _servicesState.value = ServicesUiState.Success(services)
                    },
                    onFailure = { exception ->
                        // Update UI state with the error message
                        _servicesState.value = ServicesUiState.Error(
                            exception.message ?: "Failed to load services"
                        )
                    }
                )
            } catch (e: Exception) {
                // Handle unexpected errors
                _servicesState.value = ServicesUiState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    // Add sample services to a provider (for demo/testing)
    fun populateMultipleServices(providerId: String) {
        _servicesState.value = ServicesUiState.Loading

        viewModelScope.launch {
            try {
                // Create a list of sample services across different categories
                val services = listOf(
                    // Home Services (Category 101)
                    Service(
                        id = "",  // Will be generated by Firebase
                        providerId = providerId,
                        categoryId = "101",
                        name = "Basic House Cleaning",
                        description = "General cleaning of your home including sweeping, mopping, and dusting",
                        price = 1500.0,
                        priceType = com.damiens.mjoba.Model.PriceType.FIXED,
                        estimatedDuration = 120
                    ),
                    Service(
                        id = "",
                        providerId = providerId,
                        categoryId = "101",
                        name = "Deep House Cleaning",
                        description = "Thorough cleaning of all surfaces including behind furniture and appliances",
                        price = 3000.0,
                        priceType = com.damiens.mjoba.Model.PriceType.FIXED,
                        estimatedDuration = 240
                    ),

                    // Laundry Services (Category 102)
                    Service(
                        id = "",
                        providerId = providerId,
                        categoryId = "102",
                        name = "Wash & Fold",
                        description = "Washing, drying, and folding of your clothes",
                        price = 800.0,
                        priceType = com.damiens.mjoba.Model.PriceType.FIXED,
                        estimatedDuration = 120
                    ),

                    // Beauty Services (Category 201)
                    Service(
                        id = "",
                        providerId = providerId,
                        categoryId = "201",
                        name = "Hair Styling",
                        description = "Professional styling for all occasions",
                        price = 1200.0,
                        priceType = com.damiens.mjoba.Model.PriceType.FIXED,
                        estimatedDuration = 90
                    ),

                    // Repair Services (Category 301)
                    Service(
                        id = "",
                        providerId = providerId,
                        categoryId = "301",
                        name = "Plumbing Repair",
                        description = "Fix leaky faucets, clogged drains, and other plumbing issues",
                        price = 2000.0,
                        priceType = com.damiens.mjoba.Model.PriceType.HOURLY,
                        estimatedDuration = 120
                    )
                )

                // Add each service to Firebase
                val addedServices = mutableListOf<Service>()
                for (service in services) {
                    val result = serviceProviderRepository.addService(providerId, service)
                    result.fold(
                        onSuccess = { newService ->
                            addedServices.add(newService)
                        },
                        onFailure = { exception ->
                            // Log error but continue with other services
                            Log.e("ServiceViewModel", "Failed to add service ${service.name}: ${exception.message}")
                        }
                    )
                }

                // Update UI state with the added services
                if (addedServices.isNotEmpty()) {
                    _servicesState.value = ServicesUiState.Success(addedServices)
                } else {
                    _servicesState.value = ServicesUiState.Error("Failed to add any services")
                }

            } catch (e: Exception) {
                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    // Load services by category ID
    fun loadServicesByCategory(categoryId: String) {
        _servicesState.value = ServicesUiState.Loading

        Log.d("ServiceViewModel", "Loading services for category: $categoryId")

        viewModelScope.launch {
            try {
                // Get all services
                val allServicesResult = serviceProviderRepository.getAllServices()

                allServicesResult.fold(
                    onSuccess = { allServices ->
                        // Filter services by category ID
                        val categoryServices = allServices.filter { it.categoryId == categoryId }
                        Log.d("ServiceViewModel", "Loaded ${categoryServices.size} services for category $categoryId")
                        _servicesState.value = ServicesUiState.Success(categoryServices)
                    },
                    onFailure = { exception ->
                        Log.e("ServiceViewModel", "Failed to load services: ${exception.message}")
                        _servicesState.value = ServicesUiState.Error(exception.message ?: "Failed to load services")
                    }
                )
            } catch (e: Exception) {
                Log.e("ServiceViewModel", "Exception loading services: ${e.message}")
                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    // Load a specific service by ID
    fun loadServiceById(serviceId: String) {
        _servicesState.value = ServicesUiState.Loading

        Log.d("ServiceViewModel", "Loading service details for ID: $serviceId")

        viewModelScope.launch {
            try {
                // Get all services to find the one we want
                // This is a workaround until you implement a direct getServiceById method
                val service = findServiceById(serviceId)

                if (service != null) {
                    // Now get the provider
                    val providerResult = serviceProviderRepository.getServiceProviderByUserId(service.providerId)

                    providerResult.fold(
                        onSuccess = { provider ->
                            Log.d("ServiceViewModel", "Loaded service and provider successfully")
                            _servicesState.value = ServicesUiState.ServiceDetails(service, provider)
                        },
                        onFailure = { exception ->
                            Log.e("ServiceViewModel", "Failed to load provider: ${exception.message}")

                            // Create a minimal provider object to display service details
                            val dummyProvider = ServiceProvider(
                                userId = service.providerId,
                                businessName = "Unknown Provider",
                                description = "",
                                isVerified = false,
                                isAvailable = true
                            )

                            _servicesState.value = ServicesUiState.ServiceDetails(service, dummyProvider)
                        }
                    )
                } else {
                    _servicesState.value = ServicesUiState.Error("Service not found")
                }
            } catch (e: Exception) {
                Log.e("ServiceViewModel", "Exception loading service details: ${e.message}")
                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    // Helper method to find a service by ID
    // This is a workaround until you implement a direct getServiceById method
    private suspend fun findServiceById(serviceId: String): Service? {
        val allServicesResult = serviceProviderRepository.getAllServices()

        return allServicesResult.fold(
            onSuccess = { services ->
                services.find { it.id == serviceId }
            },
            onFailure = {
                null
            }
        )
    }

    /**
     * Function to populate services for a specific category
     * This helps ensure there are services to show in category details
     */
    fun populateServicesByCategory(categoryId: String, providerId: String) {
        // Show loading state
        _servicesState.value = ServicesUiState.Loading

        Log.d("ServiceViewModel", "Populating services for category: $categoryId with provider: $providerId")

        viewModelScope.launch {
            try {
                // SIMPLIFIED APPROACH: Skip provider creation completely
                // Just create services using the provided providerId

                Log.d("ServiceViewModel", "Creating demo services for category: $categoryId")

                // Get existing services for this category
                val allServicesResult = serviceProviderRepository.getAllServices()
                val existingCategoryServices = allServicesResult.fold(
                    onSuccess = { services ->
                        services.filter { it.categoryId == categoryId }
                    },
                    onFailure = { emptyList() }
                )

                // Only create new services if there are none or few for this category
                if (existingCategoryServices.size < 2) {
                    Log.d("ServiceViewModel", "Not enough services for category $categoryId, creating demo services")

                    // Create demo services based on category
                    val demoServices = getDemoServicesForCategory(categoryId, providerId)

                    // Add each service to Firebase
                    val addedServices = mutableListOf<Service>()
                    for (service in demoServices) {
                        try {
                            val result = serviceProviderRepository.addService(providerId, service)
                            result.fold(
                                onSuccess = { newService ->
                                    Log.d("ServiceViewModel", "Added demo service: ${newService.name}")
                                    addedServices.add(newService)
                                },
                                onFailure = { exception ->
                                    Log.e("ServiceViewModel", "Failed to add demo service: ${exception.message}")
                                }
                            )
                        } catch (e: Exception) {
                            Log.e("ServiceViewModel", "Error adding service: ${e.message}")
                        }
                    }

                    if (addedServices.isNotEmpty()) {
                        // If we added any services, update the UI with those
                        _servicesState.value = ServicesUiState.Success(addedServices)
                    } else {
                        // Otherwise, just reload services for this category
                        loadServicesByCategory(categoryId)
                    }
                } else {
                    // We already have services, just update the UI
                    Log.d("ServiceViewModel", "Category already has ${existingCategoryServices.size} services")
                    _servicesState.value = ServicesUiState.Success(existingCategoryServices)
                }

            } catch (e: Exception) {
                Log.e("ServiceViewModel", "Error populating services: ${e.message}")
                _servicesState.value = ServicesUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    /**
     * Helper function to generate a business name based on category ID
     */
    private fun getCategoryBusinessName(categoryId: String): String {
        return when (categoryId.take(1)) {
            "1" -> when (categoryId) {
                "101" -> "CleanHome Pro"
                "102" -> "FreshLaundry Services"
                "103" -> "HomeChef Kitchen"
                "104" -> "Helping Hands"
                else -> "Home Services Ltd"
            }
            "2" -> when (categoryId) {
                "201" -> "Glamour Hair Salon"
                "202" -> "Men's Barber Shop"
                "203" -> "Nail Art Studio"
                "204" -> "MakeupPro Artists"
                else -> "Beauty Salon"
            }
            "3" -> when (categoryId) {
                "301" -> "PlumbFix Experts"
                "302" -> "PowerElectric Solutions"
                "303" -> "WoodCraft Carpentry"
                "304" -> "PhoneFix Center"
                else -> "Repair Services"
            }
            "4" -> "Wholesale Distributors"
            "6" -> "Entertainment Services"
            else -> "Professional Services"
        }
    }

    /**
     * Helper function to create category-specific services
     */
    private fun getDemoServicesForCategory(categoryId: String, providerId: String): List<Service> {
        return when (categoryId) {
            // Home Cleaning
            "101" -> listOf(
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Basic House Cleaning",
                    description = "General cleaning including sweeping, dusting, and mopping",
                    price = 1500.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 120
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Deep House Cleaning",
                    description = "Thorough cleaning of all surfaces including hard to reach areas",
                    price = 3000.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 240
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Window Cleaning",
                    description = "Professional cleaning of all windows and glass surfaces",
                    price = 800.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 90
                )
            )

            // Laundry
            "102" -> listOf(
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Wash & Fold",
                    description = "Complete laundry service - washing, drying, and folding",
                    price = 500.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 120
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Dry Cleaning",
                    description = "Professional dry cleaning for delicate fabrics",
                    price = 1200.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 180
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Ironing Service",
                    description = "Professional ironing of your clothes",
                    price = 400.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 90
                )
            )

            // Women's Hair
            "201" -> listOf(
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Hair Styling",
                    description = "Professional styling for all occasions",
                    price = 1200.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 90
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Hair Coloring",
                    description = "Full hair coloring or highlights",
                    price = 2500.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 150
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Hair Treatment",
                    description = "Deep conditioning and repair treatment",
                    price = 1800.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 120
                )
            )

            // Plumbing
            "301" -> listOf(
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Pipe Repair",
                    description = "Fix leaking or damaged pipes",
                    price = 2000.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 120
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Drain Unclogging",
                    description = "Clear blocked drains and sinks",
                    price = 1500.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 90
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Toilet Repair",
                    description = "Fix toilet flushing issues and leaks",
                    price = 1800.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 60
                )
            )

            // Electrical
            "302" -> listOf(
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Electrical Wiring",
                    description = "Installation and repair of electrical wiring",
                    price = 2500.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 180
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Light Fixture Installation",
                    description = "Install new light fixtures or replace old ones",
                    price = 1200.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 60
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Circuit Breaker Repair",
                    description = "Diagnose and fix circuit breaker issues",
                    price = 1800.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 90
                )
            )

            // Default services for any other category
            else -> listOf(
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Basic Service",
                    description = "Standard service in this category",
                    price = 1500.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 60
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Premium Service",
                    description = "Premium service with additional benefits",
                    price = 2500.0,
                    priceType = PriceType.FIXED,
                    estimatedDuration = 120
                ),
                Service(
                    id = "",
                    providerId = providerId,
                    categoryId = categoryId,
                    name = "Custom Service",
                    description = "Tailored to your specific needs",
                    price = 2000.0,
                    priceType = PriceType.HOURLY,
                    estimatedDuration = 90
                )
            )
        }
    }
}

