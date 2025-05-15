package com.damiens.mjoba.ui.theme.Screens.CategoryList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.ViewModel.CategoriesUiState
import com.damiens.mjoba.ViewModel.CategoryItem
import com.damiens.mjoba.ViewModel.CategoryViewModel
import com.damiens.mjoba.ui.theme.SafaricomGreen
import com.damiens.mjoba.ui.theme.SafaricomLightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    navController: NavHostController,
    categoryGroupId: String,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    // Get categories state
    val categoriesState by categoryViewModel.categoriesState.collectAsState()

    var groupTitle by remember { mutableStateOf("Categories") }
    var categories by remember { mutableStateOf<List<CategoryItem>>(emptyList()) }

    // Process the categories when state changes
    LaunchedEffect(categoriesState) {
        when (categoriesState) {
            is CategoriesUiState.Success -> {
                val catGroups = (categoriesState as CategoriesUiState.Success).categoryGroups
                val group = catGroups[categoryGroupId]
                if (group != null) {
                    groupTitle = group.title
                    categories = group.categories
                }
            }
            else -> { /* Loading or error state handled by the UI */ }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(groupTitle) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
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
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Failed to load categories",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { categoryViewModel.loadCategories() }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            is CategoriesUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    // Get screen width for responsive sizing
                    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

                    // Display all categories in a grid
                    if (categories.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            items(categories) { category ->
                                CategoryIcon(
                                    icon = category.icon,
                                    label = category.name,
                                    onClick = {
                                        navController.navigate(
                                            Screen.CategoryDetails.createRoute(category.id)
                                        )
                                    }
                                )
                            }
                        }
                    } else {
                        // Show message if category group not found
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No categories found for this group",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            else -> {
                // Handle other states if needed
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading categories...")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryIcon(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .widthIn(min = 80.dp)
            .padding(8.dp)
    ) {
        // Circular icon background
        Card(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = SafaricomLightGreen.copy(alpha = 0.3f)
            ),
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = SafaricomGreen,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}