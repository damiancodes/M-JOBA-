package com.damiens.mjoba.ViewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damiens.mjoba.Data.remote.ServiceCategoryRepository
import com.damiens.mjoba.Model.ServiceCategory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CategoryViewModel : ViewModel() {
    private val categoryRepository = ServiceCategoryRepository()

    // State for categories
    private val _categoriesState = MutableStateFlow<CategoriesUiState>(CategoriesUiState.Loading)
    val categoriesState: StateFlow<CategoriesUiState> = _categoriesState.asStateFlow()

    // Mapping of category IDs to icons
    private val categoryIcons = mapOf(
        // Home Services
        "101" to Icons.Default.CleaningServices,
        "102" to Icons.Default.LocalLaundryService,
        "103" to Icons.Default.Restaurant,
        "104" to Icons.Default.Person,
        "105" to Icons.Default.Yard,
        "106" to Icons.Default.PestControl,
        "107" to Icons.Default.LocalShipping,
        "108" to Icons.Default.Weekend,

        // Beauty Services
        "201" to Icons.Default.Spa,
        "202" to Icons.Default.ContentCut,
        "203" to Icons.Default.Spa,
        "204" to Icons.Default.Face,
        "205" to Icons.Default.Face,
        "206" to Icons.Default.SelfImprovement,
        "207" to Icons.Default.Spa,
        "208" to Icons.Default.Create,

        // Repair Services
        "301" to Icons.Default.Plumbing,
        "302" to Icons.Default.ElectricBolt,
        "303" to Icons.Default.Handyman,
        "304" to Icons.Default.PhoneAndroid,
        "305" to Icons.Default.Computer,
        "306" to Icons.Default.Kitchen,
        "307" to Icons.Default.DirectionsCar,
        "308" to Icons.Default.Roofing,

        // Wholesale Products
        "401" to Icons.Default.Stars,
        "402" to Icons.Default.Home,
        "403" to Icons.Default.LocalGroceryStore,
        "404" to Icons.Default.Devices,
        "405" to Icons.Default.Face,
        "406" to Icons.Default.Toys,
        "407" to Icons.Default.DesktopWindows,
        "408" to Icons.Default.Medication,

        // Entertainment
        "601" to Icons.Default.MusicNote,
        "602" to Icons.Default.Movie,
        "603" to Icons.Default.SportsBasketball,
        "604" to Icons.Default.Celebration,
        "605" to Icons.Default.TheaterComedy,
        "606" to Icons.Default.Museum,
        "607" to Icons.Default.Nightlife,
        "608" to Icons.Default.FamilyRestroom
    )

    // Category groupings
    private val categoryGroups = mapOf(
        "100" to "Home Services",
        "200" to "Beauty Services",
        "300" to "Repair Services",
        "400" to "Wholesale Products",
        "600" to "Entertainment"
    )

    init {
        loadCategories()

    }

    // Load categories from Firebase
    fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = CategoriesUiState.Loading

            try {
                // First check if we need to initialize categories in Firestore
                val result = categoryRepository.getAllCategories()

                result.fold(
                    onSuccess = { categories ->
                        if (categories.isEmpty()) {
                            // Initialize with default categories if none exist
                            initializeDefaultCategories()
                        } else {
                            // Map Firebase categories to UI categories with icons
                            val categoryList = categories.map { category ->
                                CategoryItem(
                                    id = category.id,
                                    name = category.name,
                                    icon = categoryIcons[category.id] ?: Icons.Default.Category
                                )
                            }

                            // Group categories by their group ID (first digit of category ID)
                            val groupedCategories = categoryList.groupBy {
                                "${it.id.first()}00" // Get group ID from category ID
                            }.mapValues { (groupId, categories) ->
                                CategoryGroup(
                                    id = groupId,
                                    title = categoryGroups[groupId] ?: "Other Categories",
                                    categories = categories
                                )
                            }

                            _categoriesState.value = CategoriesUiState.Success(groupedCategories)
                        }
                    },
                    onFailure = { exception ->
                        _categoriesState.value = CategoriesUiState.Error(exception.message ?: "Failed to load categories")
                    }
                )
            } catch (e: Exception) {
                _categoriesState.value = CategoriesUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    // Initialize default categories in Firestore
    private suspend fun initializeDefaultCategories() {
        try {
            // Create a list of default categories
            val defaultCategories = listOf(
                // Home Services
                ServiceCategory(id = "101", name = "House Cleaning", description = "Professional cleaning services"),
                ServiceCategory(id = "102", name = "Laundry", description = "Laundry and dry cleaning services"),
                ServiceCategory(id = "103", name = "Cooking", description = "Professional cooking services"),
                ServiceCategory(id = "104", name = "House Help", description = "Household assistance services"),

                // Beauty Services
                ServiceCategory(id = "201", name = "Women's Hair", description = "Women's hair styling and treatment"),
                ServiceCategory(id = "202", name = "Men's Grooming", description = "Men's haircuts and grooming"),
                ServiceCategory(id = "203", name = "Nail Care", description = "Manicure and pedicure services"),
                ServiceCategory(id = "204", name = "Makeup", description = "Professional makeup application"),

                // Repair Services
                ServiceCategory(id = "301", name = "Plumbing", description = "Plumbing repair and installation"),
                ServiceCategory(id = "302", name = "Electrical", description = "Electrical repair and installation"),
                ServiceCategory(id = "303", name = "Carpentry", description = "Carpentry and woodworking services"),
                ServiceCategory(id = "304", name = "Phone Repair", description = "Mobile phone repair services"),

                // Wholesale Products
                ServiceCategory(id = "401", name = "Clothing", description = "Wholesale clothing products"),
                ServiceCategory(id = "402", name = "Home Goods", description = "Wholesale home products"),
                ServiceCategory(id = "403", name = "Food Products", description = "Wholesale food products"),
                ServiceCategory(id = "404", name = "Electronics", description = "Wholesale electronic products"),

                // Entertainment
                ServiceCategory(id = "601", name = "Music Events", description = "Live music and events"),
                ServiceCategory(id = "602", name = "Movies", description = "Movie screenings and cinema"),
                ServiceCategory(id = "603", name = "Sports", description = "Sports events and activities"),
                ServiceCategory(id = "604", name = "Concerts", description = "Live concerts and performances")
            )

            // Add each category to Firestore
            for (category in defaultCategories) {
                categoryRepository.addCategory(category)
            }

            // Now load the categories
            loadCategories()
        } catch (e: Exception) {
            _categoriesState.value = CategoriesUiState.Error("Failed to initialize categories: ${e.message}")
        }
    }

    // Get categories for a specific group
    fun getCategoriesForGroup(groupId: String): List<CategoryItem> {
        val state = _categoriesState.value
        if (state is CategoriesUiState.Success) {
            return state.categoryGroups[groupId]?.categories ?: emptyList()
        }
        return emptyList()
    }

    // Add public access to initializeDefaultCategories so it can be called from UI
    fun resetAndInitializeCategories() {
        _categoriesState.value = CategoriesUiState.Loading

        viewModelScope.launch {
            try {
                // First clear any existing categories
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("serviceCategories")
                    .get()
                    .await()

                val batch = FirebaseFirestore.getInstance().batch()
                snapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)
                }
                batch.commit().await()

                // Then initialize with default categories
                initializeDefaultCategories()

                // Show success message
                _categoriesState.value = CategoriesUiState.Success(emptyMap())
            } catch (e: Exception) {
                _categoriesState.value = CategoriesUiState.Error(e.message ?: "Failed to reset categories")
            }
        }
    }

    // Add this function to your CategoryViewModel class
    fun initializeAllCategories() {
        viewModelScope.launch {
            _categoriesState.value = CategoriesUiState.Loading

            try {
                // First, check if categories already exist
                val result = categoryRepository.getAllCategories()

                result.fold(
                    onSuccess = { existingCategories ->
                        if (existingCategories.isNotEmpty()) {
                            // Categories already exist, just refresh the UI
                            loadCategories()
                            return@fold
                        }

                        // Define all categories to add
                        val categories = listOf(
                            // Home Services
                            ServiceCategory(id = "101", name = "House Cleaning", description = "Professional cleaning services"),
                            ServiceCategory(id = "102", name = "Laundry", description = "Laundry and dry cleaning services"),
                            ServiceCategory(id = "103", name = "Cooking", description = "Professional cooking services"),
                            ServiceCategory(id = "104", name = "House Help", description = "Household assistance services"),

                            // Beauty Services
                            ServiceCategory(id = "201", name = "Women's Hair", description = "Women's hair styling and treatment"),
                            ServiceCategory(id = "202", name = "Men's Grooming", description = "Men's haircuts and grooming"),
                            ServiceCategory(id = "203", name = "Nail Care", description = "Manicure and pedicure services"),
                            ServiceCategory(id = "204", name = "Makeup", description = "Professional makeup application"),

                            // Repair Services
                            ServiceCategory(id = "301", name = "Plumbing", description = "Plumbing repair and installation"),
                            ServiceCategory(id = "302", name = "Electrical", description = "Electrical repair and installation"),
                            ServiceCategory(id = "303", name = "Carpentry", description = "Carpentry and woodworking services"),
                            ServiceCategory(id = "304", name = "Phone Repair", description = "Mobile phone repair services"),

                            // Wholesale Products
                            ServiceCategory(id = "401", name = "Clothing", description = "Wholesale clothing products"),
                            ServiceCategory(id = "402", name = "Home Goods", description = "Wholesale home products"),
                            ServiceCategory(id = "403", name = "Food Products", description = "Wholesale food products"),
                            ServiceCategory(id = "404", name = "Electronics", description = "Wholesale electronic products"),

                            // Entertainment
                            ServiceCategory(id = "601", name = "Music Events", description = "Live music and events"),
                            ServiceCategory(id = "602", name = "Movies", description = "Movie screenings and cinema"),
                            ServiceCategory(id = "603", name = "Sports", description = "Sports events and activities"),
                            ServiceCategory(id = "604", name = "Concerts", description = "Live concerts and performances")
                        )

                        // Add each category to Firestore
                        for (category in categories) {
                            categoryRepository.addCategory(category)
                        }

                        // After adding all categories, reload them
                        loadCategories()
                    },
                    onFailure = { exception ->
                        _categoriesState.value = CategoriesUiState.Error("Failed to initialize categories: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _categoriesState.value = CategoriesUiState.Error("Failed to initialize categories: ${e.message}")
            }
        }
    }

    // Add this state class for the selected category
    private val _selectedCategoryState = MutableStateFlow<SelectedCategoryState>(SelectedCategoryState.Initial)
    val selectedCategoryState: StateFlow<SelectedCategoryState> = _selectedCategoryState.asStateFlow()

    // Get category by ID
    fun getCategoryById(categoryId: String) {
        _selectedCategoryState.value = SelectedCategoryState.Loading

        viewModelScope.launch {
            try {
                val result = categoryRepository.getCategoryById(categoryId)

                result.fold(
                    onSuccess = { category ->
                        _selectedCategoryState.value = SelectedCategoryState.Success(category)
                    },
                    onFailure = { exception ->
                        // If we couldn't get it directly, try to find it from loaded categories
                        val categoriesState = _categoriesState.value
                        if (categoriesState is CategoriesUiState.Success) {
                            // Find the category across all groups
                            val allCategoryItems = categoriesState.categoryGroups.values
                                .flatMap { it.categories }

                            val matchingItem = allCategoryItems.find { it.id == categoryId }

                            if (matchingItem != null) {
                                // Create a ServiceCategory from the CategoryItem
                                val serviceCategory = ServiceCategory(
                                    id = matchingItem.id,
                                    name = matchingItem.name,
                                    description = "Services in ${matchingItem.name} category"
                                )
                                _selectedCategoryState.value = SelectedCategoryState.Success(serviceCategory)
                            } else {
                                _selectedCategoryState.value = SelectedCategoryState.Error("Category not found")
                            }
                        } else {
                            _selectedCategoryState.value = SelectedCategoryState.Error(exception.message ?: "Failed to load category")
                        }
                    }
                )
            } catch (e: Exception) {
                _selectedCategoryState.value = SelectedCategoryState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }



    // Add this sealed class at the bottom of your CategoryViewModel.kt file
    sealed class SelectedCategoryState {
        object Initial : SelectedCategoryState()
        object Loading : SelectedCategoryState()
        data class Success(val category: ServiceCategory) : SelectedCategoryState()
        data class Error(val message: String) : SelectedCategoryState()
    }
}



// UI State for categories
sealed class CategoriesUiState {
    object Loading : CategoriesUiState()
    data class Success(val categoryGroups: Map<String, CategoryGroup>) : CategoriesUiState()
    data class Error(val message: String) : CategoriesUiState()
}

// Data classes for UI
data class CategoryGroup(
    val id: String,
    val title: String,
    val categories: List<CategoryItem>
)

data class CategoryItem(
    val id: String,
    val name: String,
    val icon: ImageVector
)

