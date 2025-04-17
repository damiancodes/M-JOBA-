// HomeScreen.kt
package com.damiens.mjoba.ui.theme.Screens.Home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.ServiceCategory
import com.damiens.mjoba.Navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "M-Joba") },
                actions = {
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    text = "Welcome to M-Joba",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Find trusted services and wholesale products near you",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Featured Categories
            item {
                Text(
                    text = "Popular Categories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                val featuredCategories = listOf(
                    ServiceCategory(id = "1", name = "Home Cleaning", icon = "", description = "Home cleaning services"),
                    ServiceCategory(id = "2", name = "Beauty", icon = "", description = "Beauty services"),
                    ServiceCategory(id = "3", name = "Repair", icon = "", description = "Repair services"),
                    ServiceCategory(id = "4", name = "Wholesale", icon = "", description = "Wholesale products")
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(featuredCategories) { category ->
                        FeaturedCategoryCard(category = category) {
                            // Navigate to category details
                            navController.navigate(Screen.CategoryDetails.createRoute(category.id))  // Update this line
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }


            item {
                SectionHeader(title = "Services")

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Home Services Categories
            item {
                CategoryGroup(
                    title = "Home Services",
                    categories = listOf(
                        ServiceCategory(id = "101", name = "House Cleaning", icon = "", description = "Home cleaning"),
                        ServiceCategory(id = "102", name = "Laundry", icon = "", description = "Laundry services"),
                        ServiceCategory(id = "103", name = "Cooking", icon = "", description = "Cooking services"),
                        ServiceCategory(id = "104", name = "House Help", icon = "", description = "House help"),
                        ServiceCategory(id = "105", name = "Office Cleaning", icon = "", description = "Office cleaning")
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))
            }


            item {
                CategoryGroup(
                    title = "Beauty Services",
                    categories = listOf(
                        ServiceCategory(id = "201", name = "Women's Hair", icon = "", description = "Women's hair styling"),
                        ServiceCategory(id = "202", name = "Men's Grooming", icon = "", description = "Men's grooming"),
                        ServiceCategory(id = "203", name = "Nail Care", icon = "", description = "Nail services"),
                        ServiceCategory(id = "204", name = "Makeup", icon = "", description = "Makeup services"),
                        ServiceCategory(id = "205", name = "Fitness Training", icon = "", description = "Fitness training")
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))
            }


            item {
                CategoryGroup(
                    title = "Repair Services",
                    categories = listOf(
                        ServiceCategory(id = "301", name = "Plumbing", icon = "", description = "Plumbing"),
                        ServiceCategory(id = "302", name = "Electrical", icon = "", description = "Electrical"),
                        ServiceCategory(id = "303", name = "Carpentry", icon = "", description = "Carpentry"),
                        ServiceCategory(id = "304", name = "Painting", icon = "", description = "Painting"),
                        ServiceCategory(id = "305", name = "Phone/Computer Repair", icon = "", description = "IT repair")
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))
            }


            item {
                SectionHeader(title = "Wholesale Products")

                Spacer(modifier = Modifier.height(8.dp))
            }


            item {
                CategoryGroup(
                    title = "Second-hand Clothing (Mitumba)",
                    categories = listOf(
                        ServiceCategory(id = "401", name = "Women's Clothing", icon = "", description = "Women's clothing"),
                        ServiceCategory(id = "402", name = "Men's Clothing", icon = "", description = "Men's clothing"),
                        ServiceCategory(id = "403", name = "Children's Clothing", icon = "", description = "Children's clothing"),
                        ServiceCategory(id = "404", name = "Shoes", icon = "", description = "Shoes"),
                        ServiceCategory(id = "405", name = "Bags", icon = "", description = "Bags")
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Other Wholesale Categories
            item {
                CategoryGroup(
                    title = "Other Wholesale Products",
                    categories = listOf(
                        ServiceCategory(id = "501", name = "Kitchen Supplies", icon = "", description = "Kitchen supplies"),
                        ServiceCategory(id = "502", name = "Beauty Products", icon = "", description = "Beauty products"),
                        ServiceCategory(id = "503", name = "Home Goods", icon = "", description = "Home goods"),
                        ServiceCategory(id = "504", name = "Food Products", icon = "", description = "Food products"),
                        ServiceCategory(id = "505", name = "Electronics", icon = "", description = "Electronics")
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // See All button
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* TODO: Navigate to all categories */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "View All Services and Products")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        )
    }
}

@Composable
fun CategoryGroup(
    title: String,
    categories: List<ServiceCategory>,
    navController: NavHostController
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryCard(category = category) {
                    // Navigate to category details
                    navController.navigate(Screen.CategoryDetails.createRoute(category.id))  // Update this line
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedCategoryCard(category: ServiceCategory, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(140.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // You would use an Icon or Image here
            // Icon(imageVector = Icons.Default.Home, contentDescription = null)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(category: ServiceCategory, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(110.dp)
            .height(110.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // You would use an Icon or Image here
            // Icon(imageVector = Icons.Default.Home, contentDescription = null)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
private fun homeprev() {
    HomeScreen(navController = NavHostController(LocalContext.current))



}