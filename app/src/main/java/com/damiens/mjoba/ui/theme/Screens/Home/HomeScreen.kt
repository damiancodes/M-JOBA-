//
//package com.damiens.mjoba.ui.theme.Screens.Home
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.damiens.mjoba.Model.ServiceCategory
//import com.damiens.mjoba.Navigation.Screen
//import com.damiens.mjoba.ui.theme.SafaricomBackground
//import com.damiens.mjoba.ui.theme.SafaricomGreen
//import com.damiens.mjoba.ui.theme.SafaricomLightGreen
//import com.damiens.mjoba.util.SampleData
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(navController: NavHostController) {
//    Scaffold(
//        containerColor = SafaricomBackground,
//        topBar = {
//            TopAppBar(
//                title = {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        // M-Joba Logo
//                        Text(
//                            text = "M-Joba",
//                            style = MaterialTheme.typography.titleLarge,
//                            color = SafaricomGreen,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                },
//                actions = {
//                    // Notification Icon
//                    IconButton(onClick = { /* TODO: Notifications */ }) {
//                        Icon(
//                            imageVector = Icons.Default.Notifications,
//                            contentDescription = "Notifications",
//                            tint = Color.Gray
//                        )
//                    }
//
//                    // Profile/Account Icon
//                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
//                        Box(
//                            modifier = Modifier
//                                .size(32.dp)
//                                .clip(CircleShape)
//                                .background(Color.LightGray),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Person,
//                                contentDescription = "Account",
//                                tint = Color.White
//                            )
//                        }
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(horizontal = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // Greeting Section
//            item {
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Greeting with user's name
//                Column {
//                    Text(
//                        text = "Good afternoon,",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = Color.Gray
//                    )
//
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Text(
//                            text = "Damian",
//                            style = MaterialTheme.typography.headlineSmall,
//                            fontWeight = FontWeight.Bold
//                        )
//
//                        Spacer(modifier = Modifier.width(4.dp))
//
//                        Text(
//                            text = "👋",
//                            style = MaterialTheme.typography.headlineSmall
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//            }
//
//            // Search Bar
//            item {
//                OutlinedCard(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(8.dp),
//                    colors = CardDefaults.outlinedCardColors(
//                        containerColor = MaterialTheme.colorScheme.surface
//                    )
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Search,
//                            contentDescription = "Search",
//                            tint = Color.Gray
//                        )
//
//                        Spacer(modifier = Modifier.width(12.dp))
//
//                        Text(
//                            text = "Search for services or products",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = Color.Gray
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//
//            // Home Services Section
//            item {
//                CategorySection(
//                    title = "Home Services",
//                    categories = listOf(
//                        CategoryItem("101", "House Cleaning", Icons.Default.CleaningServices),
//                        CategoryItem("102", "Laundry", Icons.Default.LocalLaundryService),
//                        CategoryItem("103", "Cooking", Icons.Default.Restaurant),
//                        CategoryItem("104", "House Help", Icons.Default.Person)
//                    ),
//                    navController = navController
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//
//            // Beauty Services Section
//            item {
//                CategorySection(
//                    title = "Beauty Services",
//                    categories = listOf(
//                        CategoryItem("201", "Women's Hair", Icons.Default.Spa),
//                        CategoryItem("202", "Men's Grooming", Icons.Default.ContentCut),
//                        CategoryItem("203", "Nail Care", Icons.Default.Spa),
//                        CategoryItem("204", "Makeup", Icons.Default.Face)
//                    ),
//                    navController = navController
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//
//            // Repair Services Section
//            item {
//                CategorySection(
//                    title = "Repair Services",
//                    categories = listOf(
//                        CategoryItem("301", "Plumbing", Icons.Default.Plumbing),
//                        CategoryItem("302", "Electrical", Icons.Default.ElectricBolt),
//                        CategoryItem("303", "Carpentry", Icons.Default.Handyman),
//                        CategoryItem("304", "Phone Repair", Icons.Default.PhoneAndroid)
//                    ),
//                    navController = navController
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//
//            // Wholesale Products Section
//            item {
//                CategorySection(
//                    title = "Wholesale Products",
//                    categories = listOf(
//                        CategoryItem("401", "Clothing", Icons.Default.Stars),
//                        CategoryItem("402", "Home Goods", Icons.Default.Home),
//                        CategoryItem("403", "Food Products", Icons.Default.LocalGroceryStore),
//                        CategoryItem("404", "Electronics", Icons.Default.Devices)
//                    ),
//                    navController = navController
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//
//            // Entertainment Section (like in Safaricom app)
//            item {
//                CategorySection(
//                    title = "Entertainment",
//                    categories = listOf(
//                        CategoryItem("601", "Music Events", Icons.Default.MusicNote),
//                        CategoryItem("602", "Movies", Icons.Default.Movie),
//                        CategoryItem("603", "Sports", Icons.Default.SportsBasketball),
//                        CategoryItem("604", "Concerts", Icons.Default.Celebration)
//                    ),
//                    navController = navController
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Quick Access (Popular Services)
//            item {
//                Text(
//                    text = "Popular Services",
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(vertical = 8.dp)
//                )
//
//                LazyRow(
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    items(
//                        listOf(
//                            QuickAccessItem("101", "House Cleaning", "From Ksh 1500"),
//                            QuickAccessItem("201", "Hair Styling", "From Ksh 1000"),
//                            QuickAccessItem("301", "Plumbing", "From Ksh 2000"),
//                            QuickAccessItem("302", "Electrical", "From Ksh 1800")
//                        )
//                    ) { item ->
//                        QuickAccessCard(
//                            title = item.title,
//                            price = item.subtitle,
//                            onClick = {
//                                navController.navigate(
//                                    Screen.CategoryDetails.createRoute(item.id)
//                                )
//                            }
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(32.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun CategorySection(
//    title: String,
//    categories: List<CategoryItem>,
//    navController: NavHostController
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        )
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = title,
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold
//                )
//
//                TextButton(onClick = { /* TODO: Navigate to category view */ }) {
//                    Text(
//                        text = "View all",
//                        color = SafaricomGreen
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            LazyHorizontalGrid(
//                rows = GridCells.Fixed(1),
//                horizontalArrangement = Arrangement.spacedBy(16.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                modifier = Modifier.height(80.dp)
//            ) {
//                items(categories) { category ->
//                    CategoryIcon(
//                        icon = category.icon,
//                        label = category.name,
//                        onClick = {
//                            navController.navigate(
//                                Screen.CategoryDetails.createRoute(category.id)
//                            )
//                        }
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CategoryIcon(
//    icon: ImageVector,
//    label: String,
//    onClick: () -> Unit
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .width(80.dp)
//            .wrapContentHeight()
//    ) {
//        // Circular icon background similar to Safaricom app
//        Card(
//            onClick = onClick,
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = SafaricomLightGreen.copy(alpha = 0.3f)
//            ),
//            modifier = Modifier
//                .size(48.dp)
//                .align(Alignment.CenterHorizontally)
//        ) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = icon,
//                    contentDescription = label,
//                    tint = SafaricomGreen,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodySmall,
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1,
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun QuickAccessCard(
//    title: String,
//    price: String,
//    onClick: () -> Unit
//) {
//    Card(
//        onClick = onClick,
//        modifier = Modifier
//            .width(180.dp)
//            .height(120.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = SafaricomLightGreen.copy(alpha = 0.15f)
//        )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = title,
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = price,
//                style = MaterialTheme.typography.bodyMedium,
//                color = SafaricomGreen
//            )
//        }
//    }
//}
//
//// Data classes for the UI
//data class CategoryItem(
//    val id: String,
//    val name: String,
//    val icon: ImageVector
//)
//
//data class QuickAccessItem(
//    val id: String,
//    val title: String,
//    val subtitle: String
//)
//
//@Preview
//@Composable
//private fun HomeScreenPreview() {
//    HomeScreen(NavHostController(LocalContext.current))
//}
//




package com.damiens.mjoba.ui.theme.Screens.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceCategory
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.ui.theme.SafaricomBackground
import com.damiens.mjoba.ui.theme.SafaricomGreen
import com.damiens.mjoba.ui.theme.SafaricomLightGreen
import com.damiens.mjoba.util.SampleData

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    // Add search state variables
    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        containerColor = SafaricomBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // M-Joba Logo
                        Text(
                            text = "M-Joba",
                            style = MaterialTheme.typography.titleLarge,
                            color = SafaricomGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    // Notification Icon
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.Gray
                        )
                    }

                    // Profile/Account Icon
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Account",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting Section
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Greeting with user's name
                Column {
                    Text(
                        text = "Good afternoon,",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Damian",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "👋",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Search Bar - Updated to be clickable
            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showSearchDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Search for services or products",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Home Services Section
            item {
                CategorySection(
                    title = "Home Services",
                    categories = listOf(
                        CategoryItem("101", "House Cleaning", Icons.Default.CleaningServices),
                        CategoryItem("102", "Laundry", Icons.Default.LocalLaundryService),
                        CategoryItem("103", "Cooking", Icons.Default.Restaurant),
                        CategoryItem("104", "House Help", Icons.Default.Person)
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Beauty Services Section
            item {
                CategorySection(
                    title = "Beauty Services",
                    categories = listOf(
                        CategoryItem("201", "Women's Hair", Icons.Default.Spa),
                        CategoryItem("202", "Men's Grooming", Icons.Default.ContentCut),
                        CategoryItem("203", "Nail Care", Icons.Default.Spa),
                        CategoryItem("204", "Makeup", Icons.Default.Face)
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Repair Services Section
            item {
                CategorySection(
                    title = "Repair Services",
                    categories = listOf(
                        CategoryItem("301", "Plumbing", Icons.Default.Plumbing),
                        CategoryItem("302", "Electrical", Icons.Default.ElectricBolt),
                        CategoryItem("303", "Carpentry", Icons.Default.Handyman),
                        CategoryItem("304", "Phone Repair", Icons.Default.PhoneAndroid)
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Wholesale Products Section
            item {
                CategorySection(
                    title = "Wholesale Products",
                    categories = listOf(
                        CategoryItem("401", "Clothing", Icons.Default.Stars),
                        CategoryItem("402", "Home Goods", Icons.Default.Home),
                        CategoryItem("403", "Food Products", Icons.Default.LocalGroceryStore),
                        CategoryItem("404", "Electronics", Icons.Default.Devices)
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Entertainment Section (like in Safaricom app)
            item {
                CategorySection(
                    title = "Entertainment",
                    categories = listOf(
                        CategoryItem("601", "Music Events", Icons.Default.MusicNote),
                        CategoryItem("602", "Movies", Icons.Default.Movie),
                        CategoryItem("603", "Sports", Icons.Default.SportsBasketball),
                        CategoryItem("604", "Concerts", Icons.Default.Celebration)
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Quick Access (Popular Services)
            item {
                Text(
                    text = "Popular Services",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        listOf(
                            QuickAccessItem("101", "House Cleaning", "From Ksh 1500"),
                            QuickAccessItem("201", "Hair Styling", "From Ksh 1000"),
                            QuickAccessItem("301", "Plumbing", "From Ksh 2000"),
                            QuickAccessItem("302", "Electrical", "From Ksh 1800")
                        )
                    ) { item ->
                        QuickAccessCard(
                            title = item.title,
                            price = item.subtitle,
                            onClick = {
                                navController.navigate(
                                    Screen.CategoryDetails.createRoute(item.id)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Search Dialog
    if (showSearchDialog) {
        SearchDialog(
            initialQuery = searchQuery,
            onDismiss = { showSearchDialog = false },
            onSearch = { query, resultType, resultId ->
                showSearchDialog = false
                // Handle navigation based on search result type
                when (resultType) {
                    SearchResultType.CATEGORY -> {
                        navController.navigate(Screen.CategoryDetails.createRoute(resultId))
                    }
                    SearchResultType.SERVICE -> {
                        // Find the provider for this service
                        val service = SampleData.getService(resultId)
                        navController.navigate(Screen.ProviderDetails.createRoute(service.providerId))
                    }
                    SearchResultType.PROVIDER -> {
                        navController.navigate(Screen.ProviderDetails.createRoute(resultId))
                    }
                }
            }
        )
    }
}

@Composable
fun CategorySection(
    title: String,
    categories: List<CategoryItem>,
    navController: NavHostController
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = { /* TODO: Navigate to category view */ }) {
                    Text(
                        text = "View all",
                        color = SafaricomGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyHorizontalGrid(
                rows = GridCells.Fixed(1),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(80.dp)
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
        }
    }
}

@Composable
fun CategoryIcon(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .wrapContentHeight()
    ) {
        // Circular icon background similar to Safaricom app
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = SafaricomLightGreen.copy(alpha = 0.3f)
            ),
            modifier = Modifier
                .size(48.dp)
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
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAccessCard(
    title: String,
    price: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(180.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = SafaricomLightGreen.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = price,
                style = MaterialTheme.typography.bodyMedium,
                color = SafaricomGreen
            )
        }
    }
}

// Data classes for the UI
data class CategoryItem(
    val id: String,
    val name: String,
    val icon: ImageVector
)

data class QuickAccessItem(
    val id: String,
    val title: String,
    val subtitle: String
)

// Search functionality components
enum class SearchResultType {
    CATEGORY, SERVICE, PROVIDER
}

data class SearchResult(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: SearchResultType
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDialog(
    initialQuery: TextFieldValue,
    onDismiss: () -> Unit,
    onSearch: (TextFieldValue, SearchResultType, String) -> Unit
) {
    var searchQuery by remember { mutableStateOf(initialQuery) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Perform search
    val searchResults = remember(searchQuery.text) {
        if (searchQuery.text.length < 2) {
            emptyList()
        } else {
            val query = searchQuery.text.lowercase()

            // Search categories
            val categoryResults = listOf(
                "101" to "House Cleaning",
                "102" to "Laundry",
                "103" to "Cooking",
                "104" to "House Help",
                "201" to "Women's Hair",
                "202" to "Men's Grooming",
                "203" to "Nail Care",
                "204" to "Makeup",
                "301" to "Plumbing",
                "302" to "Electrical",
                "303" to "Carpentry",
                "304" to "Phone Repair"
            ).filter { (_, name) ->
                name.lowercase().contains(query)
            }.map { (id, name) ->
                SearchResult(
                    id = id,
                    title = name,
                    subtitle = "Category",
                    type = SearchResultType.CATEGORY
                )
            }

            // In a real app, you'd search your database
            // For now, we'll use sample data

            // Search services
            val serviceResults = listOf(
                Service(id = "1001", categoryId = "101", providerId = "2001", name = "Basic House Cleaning", description = "General cleaning of your home", price = 1500.0),
                Service(id = "1002", categoryId = "101", providerId = "2001", name = "Deep Cleaning", description = "Thorough cleaning of all surfaces", price = 3000.0),
                Service(id = "2001", categoryId = "201", providerId = "3001", name = "Hair Styling", description = "Professional styling for all occasions", price = 1000.0),
                Service(id = "2002", categoryId = "201", providerId = "3001", name = "Hair Coloring", description = "Full coloring and highlights", price = 2500.0)
            ).filter { service ->
                service.name.lowercase().contains(query) || service.description.lowercase().contains(query)
            }.map { service ->
                SearchResult(
                    id = service.id,
                    title = service.name,
                    subtitle = "Service • ${service.description}",
                    type = SearchResultType.SERVICE
                )
            }

            // Search providers
            val providerResults = listOf(
                ServiceProvider(userId = "2001", businessName = "CleanHome Services", description = "Professional cleaning services"),
                ServiceProvider(userId = "3001", businessName = "Glamour Hair Salon", description = "Professional hair styling")
            ).filter { provider ->
                provider.businessName.lowercase().contains(query) || provider.description.lowercase().contains(query)
            }.map { provider ->
                SearchResult(
                    id = provider.userId,
                    title = provider.businessName,
                    subtitle = "Service Provider • ${provider.description}",
                    type = SearchResultType.PROVIDER
                )
            }

            // Combine and limit results
            (categoryResults + serviceResults + providerResults).take(10)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Search") },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Type to search...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (searchResults.isEmpty() && searchQuery.text.length >= 2) {
                    Text(
                        text = "No results found for '${searchQuery.text}'",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else if (searchQuery.text.length < 2) {
                    Text(
                        text = "Type at least 2 characters to search",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp)
                    ) {
                        items(searchResults) { result ->
                            SearchResultItem(
                                result = result,
                                onClick = {
                                    keyboardController?.hide()
                                    onSearch(searchQuery, result.type, result.id)
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SearchResultItem(
    result: SearchResult,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = result.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = result.subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Divider()
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(NavHostController(LocalContext.current))
}