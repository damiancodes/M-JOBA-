package com.damiens.mjoba.ui.theme.Screens.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.ServiceCategory
import com.damiens.mjoba.ViewModel.CategoriesUiState
import com.damiens.mjoba.ViewModel.CategoryViewModel
import com.damiens.mjoba.ui.theme.SafaricomGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoriesScreen(
    navController: NavHostController,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    val categoriesState by categoryViewModel.categoriesState.collectAsState()

    // Theme colors
    val darkBackground = Color(0xFF121212)
    val darkCard = Color(0xFF2C2C2C)

    // States for dialogs
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableStateOf("") }
    var selectedCategoryName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Categories") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showAddCategoryDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Category"
                        )
                    }

                    IconButton(onClick = { categoryViewModel.initializeAllCategories() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Initialize Categories"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkBackground,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = darkBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCategoryDialog = true },
                containerColor = SafaricomGreen,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, "Add Category")
            }
        }
    ) { paddingValues ->
        when (categoriesState) {
            is CategoriesUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SafaricomGreen)
                }
            }

            is CategoriesUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = (categoriesState as CategoriesUiState.Error).message,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { categoryViewModel.loadCategories() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SafaricomGreen
                            )
                        ) {
                            Text("Retry")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { categoryViewModel.initializeAllCategories() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SafaricomGreen
                            )
                        ) {
                            Text("Initialize Categories")
                        }
                    }
                }
            }

            is CategoriesUiState.Success -> {
                val categoryGroups = (categoriesState as CategoriesUiState.Success).categoryGroups

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Display categories by group
                    categoryGroups.forEach { (groupId, group) ->
                        item {
                            Text(
                                text = group.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = darkCard
                                )
                            ) {
                                Column {
                                    group.categories.forEach { category ->
                                        CategoryListItem(
                                            categoryId = category.id,
                                            categoryName = category.name,
                                            onEditClick = {
                                                // Handle edit - navigate to edit screen or show edit dialog
                                            },
                                            onDeleteClick = {
                                                selectedCategoryId = category.id
                                                selectedCategoryName = category.name
                                                showDeleteConfirmDialog = true
                                            }
                                        )

                                        Divider(color = darkBackground.copy(alpha = 0.5f))
                                    }
                                }
                            }
                        }
                    }

                    // Add category button at the bottom
                    item {
                        Button(
                            onClick = { showAddCategoryDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SafaricomGreen
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text("Add New Category")
                            }
                        }
                    }
                }
            }

            else -> {
                // Handle initial state if needed
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No categories found", color = Color.White)
                }
            }
        }
    }

    // Add Category Dialog
    if (showAddCategoryDialog) {
        var categoryName by remember { mutableStateOf("") }
        var categoryDescription by remember { mutableStateOf("") }
        var categoryGroupId by remember { mutableStateOf("100") } // Default to Home Services

        AlertDialog(
            onDismissRequest = { showAddCategoryDialog = false },
            title = { Text("Add New Category") },
            containerColor = darkCard,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            text = {
                Column {
                    // Category Name
                    OutlinedTextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        label = { Text("Category Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = SafaricomGreen,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = SafaricomGreen,
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Category Description
                    OutlinedTextField(
                        value = categoryDescription,
                        onValueChange = { categoryDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = SafaricomGreen,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = SafaricomGreen,
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Group Dropdown
                    Text(
                        text = "Category Group",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    // Simplified dropdown for group selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CategoryGroupChip(
                            text = "Home",
                            selected = categoryGroupId == "100",
                            onClick = { categoryGroupId = "100" }
                        )

                        CategoryGroupChip(
                            text = "Beauty",
                            selected = categoryGroupId == "200",
                            onClick = { categoryGroupId = "200" }
                        )

                        CategoryGroupChip(
                            text = "Repair",
                            selected = categoryGroupId == "300",
                            onClick = { categoryGroupId = "300" }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (categoryName.isNotEmpty()) {
                            // Generate an ID based on the group and next available number
                            val newId = when (categoryGroupId) {
                                "100" -> "10${getNextIdSuffix(categoryGroupId)}"
                                "200" -> "20${getNextIdSuffix(categoryGroupId)}"
                                "300" -> "30${getNextIdSuffix(categoryGroupId)}"
                                else -> "10${getNextIdSuffix("100")}"
                            }

                            // Create and add the new category
                            val newCategory = ServiceCategory(
                                id = newId,
                                name = categoryName,
                                description = categoryDescription
                            )

                            // Call repository to add category
                            // categoryViewModel.addCategory(newCategory)

                            showAddCategoryDialog = false
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = SafaricomGreen
                    )
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddCategoryDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Category") },
            containerColor = darkCard,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            text = {
                Text("Are you sure you want to delete \"$selectedCategoryName\"? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Delete the category
                        // categoryViewModel.deleteCategory(selectedCategoryId)
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CategoryListItem(
    categoryId: String,
    categoryName: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category icon or identifier
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF333333), shape = MaterialTheme.shapes.small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = categoryId.takeLast(1),
                color = SafaricomGreen,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Category name
        Text(
            text = categoryName,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )

        // Action buttons
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.White
            )
        }

        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun CategoryGroupChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.small,
        color = if (selected) SafaricomGreen else Color(0xFF333333)
    ) {
        Text(
            text = text,
            color = if (selected) Color.Black else Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

// Helper function to determine the next ID suffix for a category group
fun getNextIdSuffix(groupId: String): String {
    // In a real implementation, you would query your repository to find the highest existing ID in this group
    // and then return the next number as a string
    return "5" // Placeholder
}