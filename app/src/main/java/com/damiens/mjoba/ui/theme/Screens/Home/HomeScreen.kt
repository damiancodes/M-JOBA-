//
//
//package com.damiens.mjoba.ui.theme.Screens.Home
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.damiens.mjoba.Model.Service
//import com.damiens.mjoba.Model.ServiceProvider
//import com.damiens.mjoba.Navigation.Screen
//import com.damiens.mjoba.R
//import com.damiens.mjoba.ViewModel.AuthViewModel
//import com.damiens.mjoba.ViewModel.CategoriesUiState
//import com.damiens.mjoba.ViewModel.CategoryItem
//import com.damiens.mjoba.ViewModel.CategoryViewModel
//import com.damiens.mjoba.ui.theme.MJobaTheme
//import com.damiens.mjoba.ui.theme.SafaricomGreen
//
//// Define the missing SearchResult and SearchResultType
//data class SearchResult(
//    val id: String,
//    val title: String,
//    val subtitle: String,
//    val type: SearchResultType
//)
//
//enum class SearchResultType {
//    CATEGORY,
//    SERVICE,
//    PROVIDER
//}
//
//// Enhanced color pairs for category icons (background, icon)
//val enhancedCategoryColors = listOf(
//    Color(0xFFF0F8FF) to Color(0xFF4285F4),  // Alice blue - Google blue
//    Color(0xFFFFF0F5) to Color(0xFFEA4335),  // Lavender blush - Google red
//    Color(0xFFF0FFF0) to SafaricomGreen,     // Honeydew - Safaricom green
//    Color(0xFFFFF8DC) to Color(0xFFFBBC05),  // Cornsilk - Google yellow
//    Color(0xFFF5F5F5) to Color(0xFF5F6AC4),  // White smoke - Indigo
//    Color(0xFFF0F2FF) to Color(0xFFA142F4),  // Light blue-ish - Purple
//    Color(0xFFF0FFFF) to Color(0xFF0ABAB5),  // Azure - Teal
//    Color(0xFFFFF5EE) to Color(0xFFFF7F50)   // Seashell - Coral
//)
//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Composable
//fun HomeScreen(
//    navController: NavHostController,
//    authViewModel: AuthViewModel = viewModel(),
//    categoryViewModel: CategoryViewModel = viewModel()
//) {
//    // Get user info
//    val currentUser by authViewModel.currentUser.collectAsState()
//    val username = currentUser?.name?.split(" ")?.firstOrNull() ?: "User"
//
//    // Get categories
//    val categoriesState by categoryViewModel.categoriesState.collectAsState()
//
//    // Search state variables
//    var searchQuery by remember { mutableStateOf("") }
//    var searchFocused by remember { mutableStateOf(false) }
//    val keyboardController = LocalSoftwareKeyboardController.current
//
//    // Load categories when screen is first displayed
//    LaunchedEffect(Unit) {
//        categoryViewModel.loadCategories()
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        // M-Joba Logo
//                        Image(
//                            painter = painterResource(id = R.drawable.mjoblogo),
//                            contentDescription = "M-Joba Logo",
//                            modifier = Modifier
//                                .size(40.dp)
//                                .padding(end = 8.dp)
//                        )
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
//                            tint = SafaricomGreen
//                        )
//                    }
//
//                    // Profile/Account Icon
//                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
//                        Icon(
//                            imageVector = Icons.Default.Person,
//                            contentDescription = "Account",
//                            tint = SafaricomGreen
//                        )
//                    }
//                }
//            )
//        },
//        bottomBar = {
//            NavigationBar(
//                containerColor = Color.White,
//                contentColor = SafaricomGreen
//            ) {
//                val navItems = listOf(
//                    Triple("Home", Icons.Default.Home, Screen.Home.route),
//                    Triple("Bookings", Icons.Default.DateRange, Screen.Bookings.route),
//                    Triple("Services", Icons.Default.Add, ""),
//                    Triple("Nearby", Icons.Default.Place, Screen.LocationSelection.route),
//                    Triple("Profile", Icons.Default.Person, Screen.Profile.route)
//                )
//
//                navItems.forEachIndexed { index, (title, icon, route) ->
//                    val selected = navController.currentDestination?.route == route
//
//                    if (index == 2) { // Center FAB for Services
//                        NavigationBarItem(
//                            selected = false,
//                            onClick = { /* Show service category popup */ },
//                            icon = {
//                                Box(
//                                    modifier = Modifier
//                                        .size(56.dp)
//                                        .clip(CircleShape)
//                                        .background(SafaricomGreen),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Icon(
//                                        imageVector = icon,
//                                        contentDescription = title,
//                                        tint = Color.White,
//                                        modifier = Modifier.size(24.dp)
//                                    )
//                                }
//                            },
//                            label = { Text(title) }
//                        )
//                    } else {
//                        NavigationBarItem(
//                            selected = selected,
//                            onClick = {
//                                if (route.isNotEmpty()) {
//                                    navController.navigate(route) {
//                                        popUpTo(navController.graph.startDestinationId) {
//                                            saveState = true
//                                        }
//                                        launchSingleTop = true
//                                        restoreState = true
//                                    }
//                                }
//                            },
//                            icon = {
//                                Icon(
//                                    imageVector = icon,
//                                    contentDescription = title
//                                )
//                            },
//                            label = { Text(title) }
//                        )
//                    }
//                }
//            }
//        }
//    ) { paddingValues ->
//        when (categoriesState) {
//            is CategoriesUiState.Loading -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(color = SafaricomGreen)
//                }
//            }
//
//            is CategoriesUiState.Error -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center,
//                        modifier = Modifier.padding(horizontal = 16.dp)
//                    ) {
//                        Text(
//                            text = "Failed to load categories",
//                            style = MaterialTheme.typography.bodyLarge,
//                            textAlign = TextAlign.Center
//                        )
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Button(
//                            onClick = { categoryViewModel.loadCategories() },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = SafaricomGreen
//                            )
//                        ) {
//                            Text("Retry")
//                        }
//
//                        // Add button to initialize categories if empty
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        Button(
//                            onClick = { categoryViewModel.initializeAllCategories() },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = SafaricomGreen
//                            )
//                        ) {
//                            Text("Initialize Categories")
//                        }
//                    }
//                }
//            }
//
//            is CategoriesUiState.Success -> {
//                val categoryGroups = (categoriesState as CategoriesUiState.Success).categoryGroups
//
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentPadding = PaddingValues(vertical = 16.dp)
//                ) {
//                    // Hero Section
//                    item {
//                        // Hero card for tracking or top feature
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp, vertical = 8.dp)
//                                .height(180.dp),
//                            shape = RoundedCornerShape(16.dp),
//                            colors = CardDefaults.cardColors(
//                                containerColor = SafaricomGreen.copy(alpha = 0.9f)
//                            )
//                        ) {
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(16.dp)
//                            ) {
//                                // Greeting and user name
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Column {
//                                        Text(
//                                            text = "Hello $username",
//                                            style = MaterialTheme.typography.titleLarge,
//                                            color = Color.White,
//                                            fontWeight = FontWeight.Bold
//                                        )
//
//                                        Text(
//                                            text = "Find services near you",
//                                            style = MaterialTheme.typography.bodyMedium,
//                                            color = Color.White.copy(alpha = 0.8f)
//                                        )
//                                    }
//
//                                    Icon(
//                                        imageVector = Icons.Default.LocationOn,
//                                        contentDescription = "Location",
//                                        tint = Color.White,
//                                        modifier = Modifier.size(24.dp)
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(16.dp))
//
//                                // Quick actions
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceEvenly
//                                ) {
//                                    QuickActionButton(
//                                        icon = Icons.Default.Search,
//                                        label = "Find Service",
//                                        onClick = { /* Handle click */ }
//                                    )
//
//                                    QuickActionButton(
//                                        icon = Icons.Default.DateRange,
//                                        label = "Bookings",
//                                        onClick = { navController.navigate(Screen.Bookings.route) }
//                                    )
//
//                                    QuickActionButton(
//                                        icon = Icons.Default.Place,
//                                        label = "Nearby",
//                                        onClick = { navController.navigate(Screen.LocationSelection.route) }
//                                    )
//                                }
//                            }
//                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }
//
//                    // Search Bar
//                    item {
//                        OutlinedTextField(
//                            value = searchQuery,
//                            onValueChange = { searchQuery = it },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                                .onFocusChanged { focusState ->
//                                    searchFocused = focusState.isFocused
//                                },
//                            placeholder = { Text("Search for services or products", color = Color.Gray) },
//                            leadingIcon = {
//                                Icon(
//                                    imageVector = Icons.Default.Search,
//                                    contentDescription = "Search",
//                                    tint = Color.Gray
//                                )
//                            },
//                            trailingIcon = {
//                                if (searchQuery.isNotEmpty()) {
//                                    IconButton(onClick = { searchQuery = "" }) {
//                                        Icon(
//                                            imageVector = Icons.Default.Clear,
//                                            contentDescription = "Clear search",
//                                            tint = Color.Gray
//                                        )
//                                    }
//                                }
//                            },
//                            singleLine = true,
//                            keyboardOptions = KeyboardOptions(
//                                imeAction = ImeAction.Search
//                            ),
//                            keyboardActions = KeyboardActions(
//                                onSearch = {
//                                    keyboardController?.hide()
//                                    performSearch(searchQuery, navController)
//                                    searchFocused = false
//                                }
//                            ),
//                            shape = RoundedCornerShape(12.dp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                cursorColor = SafaricomGreen,
//                                focusedBorderColor = SafaricomGreen
//                            )
//                        )
//
//                        // Search results
//                        if (searchFocused && searchQuery.isNotEmpty()) {
//                            Card(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(horizontal = 16.dp, vertical = 4.dp)
//                            ) {
//                                val results = generateSearchResults(searchQuery)
//                                if (results.isEmpty()) {
//                                    Text(
//                                        text = "No results found for '$searchQuery'",
//                                        modifier = Modifier.padding(16.dp),
//                                        style = MaterialTheme.typography.bodyMedium,
//                                        color = Color.Gray
//                                    )
//                                } else {
//                                    Column(
//                                        modifier = Modifier.heightIn(max = 200.dp)
//                                    ) {
//                                        results.forEach { result ->
//                                            SearchResultItem(
//                                                result = result,
//                                                onClick = {
//                                                    keyboardController?.hide()
//                                                    searchFocused = false
//                                                    handleSearchResult(result, navController)
//                                                }
//                                            )
//                                            Divider()
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }
//
//                    // Display category sections
//                    for ((groupId, group) in categoryGroups) {
//                        // Only show groups with categories
//                        if (group.categories.isNotEmpty()) {
//                            item {
//                                CategorySection(
//                                    title = group.title,
//                                    categories = group.categories,
//                                    navController = navController,
//                                    categoryGroupId = groupId
//                                )
//
//                                Spacer(modifier = Modifier.height(16.dp))
//                            }
//                        }
//                    }
//
//                    // Popular Services
//                    item {
//                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//                            Text(
//                                text = "Popular Services",
//                                style = MaterialTheme.typography.titleMedium,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier.padding(vertical = 8.dp)
//                            )
//
//                            // Responsive grid for popular services
//                            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
//                            val cardWidth = (screenWidth - 48.dp) / 2 // 2 columns with padding
//
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                // First row of popular services
//                                EnhancedQuickAccessCard(
//                                    title = "House Cleaning",
//                                    price = "From Ksh 1500",
//                                    onClick = {
//                                        navController.navigate(Screen.CategoryDetails.createRoute("101"))
//                                    },
//                                    modifier = Modifier
//                                        .width(cardWidth)
//                                        .height(120.dp)
//                                )
//
//                                EnhancedQuickAccessCard(
//                                    title = "Hair Styling",
//                                    price = "From Ksh 1000",
//                                    onClick = {
//                                        navController.navigate(Screen.CategoryDetails.createRoute("201"))
//                                    },
//                                    modifier = Modifier
//                                        .width(cardWidth)
//                                        .height(120.dp)
//                                )
//                            }
//
//                            Spacer(modifier = Modifier.height(16.dp))
//
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                // Second row of popular services
//                                EnhancedQuickAccessCard(
//                                    title = "Plumbing",
//                                    price = "From Ksh 2000",
//                                    onClick = {
//                                        navController.navigate(Screen.CategoryDetails.createRoute("301"))
//                                    },
//                                    modifier = Modifier
//                                        .width(cardWidth)
//                                        .height(120.dp)
//                                )
//
//                                EnhancedQuickAccessCard(
//                                    title = "Electrical",
//                                    price = "From Ksh 1800",
//                                    onClick = {
//                                        navController.navigate(Screen.CategoryDetails.createRoute("302"))
//                                    },
//                                    modifier = Modifier
//                                        .width(cardWidth)
//                                        .height(120.dp)
//                                )
//                            }
//                        }
//
//                        Spacer(modifier = Modifier.height(32.dp))
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CategorySection(
//    title: String,
//    categories: List<CategoryItem>,
//    navController: NavHostController,
//    categoryGroupId: String
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//        shape = RoundedCornerShape(16.dp)
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
//                TextButton(
//                    onClick = {
//                        navController.navigate(Screen.CategoryList.createRoute(categoryGroupId))
//                    }
//                ) {
//                    Text(
//                        text = "View all",
//                        color = SafaricomGreen
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Add inline grid of category icons
//            val rows = categories.chunked(3) // 3 icons per row
//            rows.forEach { rowCategories ->
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    rowCategories.forEach { category ->
//                        val colorIndex = category.id.toIntOrNull()?.rem(enhancedCategoryColors.size) ?: 0
//                        val colorPair = enhancedCategoryColors[colorIndex]
//
//                        EnhancedCategoryIcon(
//                            icon = category.icon,
//                            label = category.name,
//                            backgroundColor = colorPair.first,
//                            iconColor = colorPair.second,
//                            onClick = {
//                                navController.navigate(
//                                    Screen.CategoryDetails.createRoute(category.id)
//                                )
//                            },
//                            modifier = Modifier.weight(1f)
//                        )
//                    }
//
//                    // Fill remaining spaces with empty slots if needed
//                    repeat(3 - rowCategories.size) {
//                        Box(modifier = Modifier.weight(1f))
//                    }
//                }
//
//                if (rows.last() != rowCategories) {
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun EnhancedCategoryIcon(
//    icon: ImageVector,
//    label: String,
//    backgroundColor: Color,
//    iconColor: Color,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = modifier
//            .wrapContentHeight()
//            .clickable(onClick = onClick)
//            .padding(horizontal = 4.dp)
//    ) {
//        // Enhanced elevation and styling
//        Box(
//            modifier = Modifier
//                .size(60.dp)
//                .shadow(
//                    elevation = 2.dp,
//                    shape = RoundedCornerShape(16.dp)
//                )
//                .clip(RoundedCornerShape(16.dp))
//                .background(
//                    brush = Brush.radialGradient(
//                        colors = listOf(
//                            backgroundColor.copy(alpha = 0.9f),
//                            backgroundColor.copy(alpha = 0.7f)
//                        )
//                    )
//                )
//                .padding(12.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = label,
//                tint = iconColor,
//                modifier = Modifier.size(28.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(6.dp))
//
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodySmall,
//            fontWeight = FontWeight.Medium,
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1,
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EnhancedQuickAccessCard(
//    title: String,
//    price: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        onClick = onClick,
//        modifier = modifier,
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        )
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color.White,
//                            Color(0xFFF8F8F8)
//                        )
//                    )
//                )
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    text = title,
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = price,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = SafaricomGreen,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun SearchResultItem(
//    result: SearchResult,
//    onClick: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick)
//            .padding(vertical = 12.dp, horizontal = 16.dp)
//    ) {
//        Text(
//            text = result.title,
//            style = MaterialTheme.typography.bodyLarge,
//            fontWeight = FontWeight.Medium
//        )
//
//        Text(
//            text = result.subtitle,
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.Gray
//        )
//    }
//}
//
//@Composable
//fun QuickActionButton(
//    icon: ImageVector,
//    label: String,
//    onClick: () -> Unit
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.clickable(onClick = onClick)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(48.dp)
//                .clip(CircleShape)
//                .background(Color.White),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = label,
//                tint = SafaricomGreen,
//                modifier = Modifier.size(24.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.White,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
//
//private fun performSearch(query: String, navController: NavHostController) {
//    if (query.isNotEmpty()) {
//        // Find the first matching result and navigate to it
//        val results = generateSearchResults(query)
//        if (results.isNotEmpty()) {
//            handleSearchResult(results[0], navController)
//        }
//    }
//}
//
//private fun handleSearchResult(result: SearchResult, navController: NavHostController) {
//    when (result.type) {
//        SearchResultType.CATEGORY -> {
//            navController.navigate(Screen.CategoryDetails.createRoute(result.id))
//        }
//        SearchResultType.SERVICE -> {
//            // Navigate to a specific service detail page
//            navController.navigate(Screen.ServiceDetails.createRoute(result.id))
//        }
//        SearchResultType.PROVIDER -> {
//            navController.navigate(Screen.ProviderDetails.createRoute(result.id))
//        }
//    }
//}
//
//private fun generateSearchResults(query: String): List<SearchResult> {
//    val lowerQuery = query.lowercase()
//
//    // Search categories
//    val categoryResults = listOf(
//        "101" to "House Cleaning",
//        "102" to "Laundry",
//        "103" to "Cooking",
//        "104" to "House Help",
//        "201" to "Women's Hair",
//        "202" to "Men's Grooming",
//        "203" to "Nail Care",
//        "204" to "Makeup",
//        "301" to "Plumbing",
//        "302" to "Electrical",
//        "303" to "Carpentry",
//        "304" to "Phone Repair"
//    ).filter { (_, name) ->
//        name.lowercase().contains(lowerQuery)
//    }.map { (id, name) ->
//        SearchResult(
//            id = id,
//            title = name,
//            subtitle = "Category",
//            type = SearchResultType.CATEGORY
//        )
//    }
//
//    // Search services
//    val serviceResults = listOf(
//        Service(id = "1001", categoryId = "101", providerId = "2001", name = "Basic House Cleaning", description = "General cleaning of your home", price = 1500.0),
//        Service(id = "1002", categoryId = "101", providerId = "2001", name = "Deep Cleaning", description = "Thorough cleaning of all surfaces", price = 3000.0),
//        Service(id = "2001", categoryId = "201", providerId = "3001", name = "Hair Styling", description = "Professional styling for all occasions", price = 1000.0),
//        Service(id = "2002", categoryId = "201", providerId = "3001", name = "Hair Coloring", description = "Full coloring and highlights", price = 2500.0)
//    ).filter { service ->
//        service.name.lowercase().contains(lowerQuery) || service.description.lowercase().contains(lowerQuery)
//    }.map { service ->
//        SearchResult(
//            id = service.id,
//            title = service.name,
//            subtitle = "Service • ${service.description}",
//            type = SearchResultType.SERVICE
//        )
//    }
//
//    // Search providers
//    val providerResults = listOf(
//        ServiceProvider(userId = "2001", businessName = "CleanHome Services", description = "Professional cleaning services"),
//        ServiceProvider(userId = "3001", businessName = "Glamour Hair Salon", description = "Professional hair styling")
//    ).filter { provider ->
//        provider.businessName.lowercase().contains(lowerQuery) || provider.description.lowercase().contains(lowerQuery)
//    }.map { provider ->
//        SearchResult(
//            id = provider.userId,
//            title = provider.businessName,
//            subtitle = "Service Provider • ${provider.description}",
//            type = SearchResultType.PROVIDER
//        )
//    }
//
//    // Combine and limit results
//    return (categoryResults + serviceResults + providerResults).take(10)
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun HomeScreenPreview() {
//    MJobaTheme { // Should match your actual theme name
//        Surface(modifier = Modifier.fillMaxSize()) {
//            HomeScreen(
//                navController = rememberNavController()
//            )
//        }
//    }
//}



package com.damiens.mjoba.ui.theme.Screens.Home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.Navigation.Screen
import com.damiens.mjoba.R
import com.damiens.mjoba.ViewModel.AuthViewModel
import com.damiens.mjoba.ViewModel.CategoriesUiState
import com.damiens.mjoba.ViewModel.CategoryItem
import com.damiens.mjoba.ViewModel.CategoryViewModel
import com.damiens.mjoba.ui.theme.MJobaTheme
import com.damiens.mjoba.ui.theme.SafaricomGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

// Define the missing SearchResult and SearchResultType
data class SearchResult(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: SearchResultType
)

enum class SearchResultType {
    CATEGORY,
    SERVICE,
    PROVIDER
}

// Enhanced color pairs for category icons (background, icon)
val enhancedCategoryColors = listOf(
    Color(0xFFF0F8FF) to Color(0xFF4285F4),  // Alice blue - Google blue
    Color(0xFFFFF0F5) to Color(0xFFEA4335),  // Lavender blush - Google red
    Color(0xFFF0FFF0) to SafaricomGreen,     // Honeydew - Safaricom green
    Color(0xFFFFF8DC) to Color(0xFFFBBC05),  // Cornsilk - Google yellow
    Color(0xFFF5F5F5) to Color(0xFF5F6AC4),  // White smoke - Indigo
    Color(0xFFF0F2FF) to Color(0xFFA142F4),  // Light blue-ish - Purple
    Color(0xFFF0FFFF) to Color(0xFF0ABAB5),  // Azure - Teal
    Color(0xFFFFF5EE) to Color(0xFFFF7F50)   // Seashell - Coral
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel()
) {
    // Get user info
    val currentUser by authViewModel.currentUser.collectAsState()
    val username = currentUser?.name?.split(" ")?.firstOrNull() ?: "User"

    // Get categories
    val categoriesState by categoryViewModel.categoriesState.collectAsState()

    // Search state variables
    var searchQuery by remember { mutableStateOf("") }
    var searchFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Animation states
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    var isSearchExpanded by remember { mutableStateOf(false) }
    val searchBarWidth = if (isSearchExpanded) 1f else 0.85f
    val searchWidthAnim by animateFloatAsState(
        targetValue = searchBarWidth,
        animationSpec = tween(durationMillis = 300, easing = EaseOutQuart)
    )

    // Staggered reveal animation
    var startRevealAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(150)
        startRevealAnimation = true
    }

    // Load categories when screen is first displayed
    LaunchedEffect(Unit) {
        categoryViewModel.loadCategories()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        // App logo with subtle animation
                        val scale by animateFloatAsState(
                            targetValue = if (startRevealAnimation) 1f else 0.8f,
                            animationSpec = tween(durationMillis = 500, easing = EaseOutBack)
                        )
                        Box(
                            modifier = Modifier
                                .scale(scale)
                                .size(40.dp)
                                .padding(end = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.mjoblogo),
                                contentDescription = "M-Joba Logo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // App name with animated reveal
                        val titleAlpha by animateFloatAsState(
                            targetValue = if (startRevealAnimation) 1f else 0f,
                            animationSpec = tween(durationMillis = 600, delayMillis = 150)
                        )
                        Text(
                            text = "M-Joba",
                            style = MaterialTheme.typography.titleLarge,
                            color = SafaricomGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.alpha(titleAlpha)
                        )
                    }
                },
                actions = {
                    // Notification Icon with badge and animation
                    IconButton(
                        onClick = { /* TODO: Notifications */ },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        val pulseAnim = remember { Animatable(1f) }
                        LaunchedEffect(Unit) {
                            pulseAnim.animateTo(
                                targetValue = 1.15f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(800, easing = EaseInOutQuad),
                                    repeatMode = RepeatMode.Reverse
                                )
                            )
                        }

                        Box(contentAlignment = Alignment.TopEnd) {
                            Icon(
                                imageVector = Icons.Rounded.Notifications,
                                contentDescription = "Notifications",
                                tint = SafaricomGreen,
                                modifier = Modifier.size(26.dp)
                            )

                            // Notification badge with pulse animation
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .scale(pulseAnim.value)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }

                    // Profile/Account Icon with touch feedback (mobile-friendly)
                    var isProfilePressed by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = {
                            isProfilePressed = true
                            navController.navigate(Screen.Profile.route)
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        val profileScale by animateFloatAsState(
                            targetValue = if (isProfilePressed) 0.9f else 1f,
                            animationSpec = tween(150)
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .scale(profileScale)
                                .clip(CircleShape)
                                .background(SafaricomGreen.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "Account",
                                tint = SafaricomGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Reset pressed state after delay
                    LaunchedEffect(isProfilePressed) {
                        if (isProfilePressed) {
                            delay(200)
                            isProfilePressed = false
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = SafaricomGreen
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = SafaricomGreen,
                tonalElevation = 8.dp,
                modifier = Modifier.shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
            ) {
                val navItems = listOf(
                    Triple("Home", Icons.Rounded.Home, Screen.Home.route),
                    Triple("Bookings", Icons.Rounded.DateRange, Screen.Bookings.route),
                    Triple("Services", Icons.Rounded.Add, ""),
                    Triple("Nearby", Icons.Rounded.Place, Screen.LocationSelection.route),
                    Triple("Profile", Icons.Rounded.Person, Screen.Profile.route)
                )

                navItems.forEachIndexed { index, (title, icon, route) ->
                    val selected = navController.currentDestination?.route == route
                    var itemInteraction by remember { mutableStateOf(false) }

                    if (index == 2) { // Center FAB for Services
                        NavigationBarItem(
                            selected = false,
                            onClick = {
                                scope.launch {
                                    // Animate fab press and release
                                    itemInteraction = true
                                    delay(200)
                                    itemInteraction = false
                                }
                            },
                            icon = {
                                val fabScale by animateFloatAsState(
                                    targetValue = if (itemInteraction) 0.9f else 1f,
                                    animationSpec = tween(150, easing = EaseInOutQuad)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .scale(fabScale)
                                        .shadow(elevation = 8.dp, shape = CircleShape)
                                        .clip(CircleShape)
                                        .background(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    SafaricomGreen,
                                                    SafaricomGreen.copy(alpha = 0.8f)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = title,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        )
                    } else {
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (route.isNotEmpty()) {
                                    scope.launch {
                                        itemInteraction = true
                                        delay(200)
                                        itemInteraction = false

                                        navController.navigate(route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            },
                            icon = {
                                val itemScale by animateFloatAsState(
                                    targetValue = if (itemInteraction) 0.8f else 1f,
                                    animationSpec = tween(150)
                                )
                                val iconSize = if (selected) 26.dp else 22.dp
                                Box(
                                    modifier = Modifier.scale(itemScale),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = title,
                                        modifier = Modifier.size(iconSize)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SafaricomGreen,
                                selectedTextColor = SafaricomGreen,
                                indicatorColor = SafaricomGreen.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
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
                    LoadingAnimation()
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
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { categoryViewModel.loadCategories() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SafaricomGreen
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Retry")
                        }

                        // Add button to initialize categories if empty
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { categoryViewModel.initializeAllCategories() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SafaricomGreen
                            ),
                            shape = RoundedCornerShape(12.dp)
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
                        .padding(paddingValues),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    // Hero Section with enhanced animations
                    item {
                        // Animated entrance
                        AnimatedVisibility(
                            visible = startRevealAnimation,
                            enter = fadeIn(tween(500)) + expandVertically(
                                animationSpec = tween(500, easing = EaseOutQuart),
                                expandFrom = Alignment.Top
                            )
                        ) {
                            // Hero card for tracking or top feature
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .height(180.dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(24.dp),
                                        spotColor = SafaricomGreen.copy(alpha = 0.2f)
                                    ),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    SafaricomGreen,
                                                    SafaricomGreen.copy(alpha = 0.8f),
                                                    Color(0xFF039855)
                                                )
                                            )
                                        )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(20.dp)
                                    ) {
                                        // Greeting and user name
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Hello $username",
                                                    style = MaterialTheme.typography.titleLarge,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Text(
                                                    text = "Find services near you",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color.White.copy(alpha = 0.8f)
                                                )
                                            }

                                            // Pulsating location icon
                                            val locationPulse = remember { Animatable(1f) }
                                            LaunchedEffect(Unit) {
                                                locationPulse.animateTo(
                                                    targetValue = 1.2f,
                                                    animationSpec = infiniteRepeatable(
                                                        animation = tween(1000, easing = EaseInOutQuad),
                                                        repeatMode = RepeatMode.Reverse
                                                    )
                                                )
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.White.copy(alpha = 0.2f))
                                                    .padding(8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.LocationOn,
                                                    contentDescription = "Location",
                                                    tint = Color.White,
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .scale(locationPulse.value)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Quick actions with improved design
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            EnhancedQuickActionButton(
                                                icon = Icons.Rounded.Search,
                                                label = "Find Service",
                                                onClick = {
                                                    isSearchExpanded = true
                                                }
                                            )

                                            EnhancedQuickActionButton(
                                                icon = Icons.Rounded.DateRange,
                                                label = "Bookings",
                                                onClick = { navController.navigate(Screen.Bookings.route) }
                                            )

                                            EnhancedQuickActionButton(
                                                icon = Icons.Rounded.Place,
                                                label = "Nearby",
                                                onClick = { navController.navigate(Screen.LocationSelection.route) }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Enhanced Modern Search Bar with animations
                    item {
                        // Animated entrance for search bar
                        AnimatedVisibility(
                            visible = startRevealAnimation,
                            enter = fadeIn(tween(600)) + expandHorizontally(
                                animationSpec = tween(600, 150, EaseOutQuart)
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(searchWidthAnim)
                                        .align(Alignment.Center)
                                        .shadow(
                                            elevation = if (searchFocused) 8.dp else 4.dp,
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    OutlinedTextField(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 56.dp)
                                            .onFocusChanged { focusState ->
                                                searchFocused = focusState.isFocused
                                                if (focusState.isFocused) {
                                                    isSearchExpanded = true
                                                }
                                            },
                                        placeholder = {
                                            Text(
                                                "Search for services or products",
                                                color = Color.Gray.copy(alpha = 0.7f)
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Rounded.Search,
                                                contentDescription = "Search",
                                                tint = if (searchFocused) SafaricomGreen else Color.Gray,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        },
                                        trailingIcon = {
                                            AnimatedVisibility(
                                                visible = searchQuery.isNotEmpty(),
                                                enter = fadeIn() + scaleIn(),
                                                exit = fadeOut() + scaleOut()
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        searchQuery = ""
                                                        isSearchExpanded = false
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Clear,
                                                        contentDescription = "Clear search",
                                                        tint = Color.Gray,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }
                                        },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Search
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onSearch = {
                                                keyboardController?.hide()
                                                performSearch(searchQuery, navController)
                                                searchFocused = false
                                                isSearchExpanded = false
                                            }
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            cursorColor = SafaricomGreen,
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            focusedContainerColor = Color.White,
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        // Search results with animations
                        AnimatedVisibility(
                            visible = searchFocused && searchQuery.isNotEmpty(),
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                val results = generateSearchResults(searchQuery)
                                if (results.isEmpty()) {
                                    Text(
                                        text = "No results found for '$searchQuery'",
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                } else {
                                    Column(
                                        modifier = Modifier.heightIn(max = 250.dp)
                                    ) {
                                        results.forEachIndexed { index, result ->
                                            SearchResultItem(
                                                result = result,
                                                onClick = {
                                                    keyboardController?.hide()
                                                    searchFocused = false
                                                    isSearchExpanded = false
                                                    handleSearchResult(result, navController)
                                                },
                                                animationDelay = index * 50L
                                            )
                                            if (index < results.size - 1) {
                                                Divider(
                                                    modifier = Modifier.padding(horizontal = 16.dp),
                                                    color = Color.LightGray.copy(alpha = 0.5f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Display category sections with staggered animation
                    for ((index, entry) in categoryGroups.entries.withIndex()) {
                        val (groupId, group) = entry
                        // Only show groups with categories
                        if (group.categories.isNotEmpty()) {
                            item {
                                // Staggered entrance animation
                                AnimatedVisibility(
                                    visible = startRevealAnimation,
                                    enter = fadeIn(
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            delayMillis = 200 + (index * 100)
                                        )
                                    ) + slideInVertically(
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            delayMillis = 200 + (index * 100),
                                            easing = EaseOutQuart
                                        ),
                                        initialOffsetY = { it / 5 }
                                    )
                                ) {
                                    EnhancedCategorySection(
                                        title = group.title,
                                        categories = group.categories,
                                        navController = navController,
                                        categoryGroupId = groupId
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }

                    // Popular Services with improved visuals
                    item {
                        AnimatedVisibility(
                            visible = startRevealAnimation,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 500,
                                    delayMillis = 500 + (categoryGroups.size * 100)
                                )
                            ) + slideInVertically(
                                animationSpec = tween(
                                    durationMillis = 500,
                                    delayMillis = 500 + (categoryGroups.size * 100)
                                ),
                                initialOffsetY = { it / 5 }
                            )
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Popular Services",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    TextButton(onClick = { /* View all popular services */ }) {
                                        Text(
                                            text = "View all",
                                            color = SafaricomGreen,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 14.sp
                                        )
                                    }
                                }

                                // Responsive grid for popular services
                                val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                                val cardWidth = (screenWidth - 48.dp) / 2 // 2 columns with padding

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {// First row of popular services with animations
                                    PopularServiceCard(
                                        title = "House Cleaning",
                                        price = "From Ksh 1500",
                                        iconVector = Icons.Rounded.CleaningServices,
                                        onClick = {
                                            navController.navigate(Screen.CategoryDetails.createRoute("101"))
                                        },
                                        modifier = Modifier
                                            .width(cardWidth)
                                            .height(130.dp),
                                        animDelay = 0
                                    )

                                    PopularServiceCard(
                                        title = "Hair Styling",
                                        price = "From Ksh 1000",
                                        iconVector = Icons.Rounded.ContentCut,
                                        onClick = {
                                            navController.navigate(Screen.CategoryDetails.createRoute("201"))
                                        },
                                        modifier = Modifier
                                            .width(cardWidth)
                                            .height(130.dp),
                                        animDelay = 100
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Second row of popular services
                                    PopularServiceCard(
                                        title = "Plumbing",
                                        price = "From Ksh 2000",
                                        iconVector = Icons.Rounded.Plumbing,
                                        onClick = {
                                            navController.navigate(Screen.CategoryDetails.createRoute("301"))
                                        },
                                        modifier = Modifier
                                            .width(cardWidth)
                                            .height(130.dp),
                                        animDelay = 200
                                    )

                                    PopularServiceCard(
                                        title = "Electrical",
                                        price = "From Ksh 1800",
                                        iconVector = Icons.Rounded.ElectricBolt,
                                        onClick = {
                                            navController.navigate(Screen.CategoryDetails.createRoute("302"))
                                        },
                                        modifier = Modifier
                                            .width(cardWidth)
                                            .height(130.dp),
                                        animDelay = 300
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedCategorySection(
    title: String,
    categories: List<CategoryItem>,
    navController: NavHostController,
    categoryGroupId: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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

                TextButton(
                    onClick = {
                        navController.navigate(Screen.CategoryList.createRoute(categoryGroupId))
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "View all",
                            color = SafaricomGreen,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "View all",
                            modifier = Modifier.size(14.dp),
                            tint = SafaricomGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Horizontal scrollable grid of category icons
            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
            ) {
                items(categories) { category ->
                    val colorIndex = category.id.toIntOrNull()?.rem(enhancedCategoryColors.size) ?: 0
                    val colorPair = enhancedCategoryColors[colorIndex]

                    EnhancedCategoryIcon(
                        icon = category.icon,
                        label = category.name,
                        backgroundColor = colorPair.first,
                        iconColor = colorPair.second,
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
fun EnhancedCategoryIcon(
    icon: ImageVector,
    label: String,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    // Animation states
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(150, easing = EaseOutQuad)
    )
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 1.dp else 4.dp,
        animationSpec = tween(150)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize()
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // Animate press effect
                isPressed = true
                onClick()
            }
            .padding(horizontal = 4.dp)
    ) {
        // Enhanced elevation and styling
        Box(
            modifier = Modifier
                .size(65.dp)
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(18.dp),
                    spotColor = iconColor.copy(alpha = 0.1f)
                )
                .clip(RoundedCornerShape(18.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            backgroundColor.copy(alpha = 0.95f),
                            backgroundColor.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }

    // Reset pressed state after delay
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(200)
            isPressed = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularServiceCard(
    title: String,
    price: String,
    iconVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    animDelay: Long = 0
) {
    // Card animation
    var startAnim by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(animDelay)
        startAnim = true
    }

    val cardScale by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.9f,
        animationSpec = tween(300, easing = EaseOutBack)
    )

    val cardAlpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(300)
    )

    Card(
        onClick = onClick,
        modifier = modifier
            .scale(cardScale)
            .alpha(cardAlpha),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 0.dp
        ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFF8F8F8)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Service icon with colored background
                val colorIndex = title.hashCode().rem(enhancedCategoryColors.size).absoluteValue
                val colorPair = enhancedCategoryColors[colorIndex]

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorPair.first)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = title,
                        tint = colorPair.second,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = price,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SafaricomGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(
    result: SearchResult,
    onClick: () -> Unit,
    animationDelay: Long = 0
) {
    // Animation for staggered appearance
    var startAnim by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(animationDelay)
        startAnim = true
    }

    val itemAlpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(300)
    )

    val slideOffset by animateDpAsState(
        targetValue = if (startAnim) 0.dp else 20.dp,
        animationSpec = tween(300, easing = EaseOutQuad)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = slideOffset)
            .alpha(itemAlpha)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        // Different icon based on result type
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon based on result type
            val icon = when (result.type) {
                SearchResultType.CATEGORY -> Icons.Rounded.Category
                SearchResultType.SERVICE -> Icons.Rounded.Handyman
                SearchResultType.PROVIDER -> Icons.Rounded.Store
            }

            val iconTint = when (result.type) {
                SearchResultType.CATEGORY -> Color(0xFF4285F4)
                SearchResultType.SERVICE -> SafaricomGreen
                SearchResultType.PROVIDER -> Color(0xFFEA4335)
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )

            Column {
                Text(
                    text = result.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = result.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun EnhancedQuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    // Button animation
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(150)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            }
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .shadow(6.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.2f))
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = SafaricomGreen,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }

    // Reset pressed state after delay
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(200)
            isPressed = false
        }
    }
}

@Composable
fun LoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    // Create animated values with different phases
    val circles = List(3) { index ->
        val delay = index * 150
        val scale by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1500
                    0f at 0 + delay
                    1f at 300 + delay
                    0f at 600 + delay
                    0f at 1500 + delay
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "scale$index"
        )
        scale
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        circles.forEachIndexed { index, animValue ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .scale(1f + animValue * 0.5f)
                    .clip(CircleShape)
                    .background(
                        SafaricomGreen.copy(
                            alpha = 0.3f + animValue * 0.7f
                        )
                    )
            )
        }
    }
}

private fun performSearch(query: String, navController: NavHostController) {
    if (query.isNotEmpty()) {
        // Find the first matching result and navigate to it
        val results = generateSearchResults(query)
        if (results.isNotEmpty()) {
            handleSearchResult(results[0], navController)
        }
    }
}

private fun handleSearchResult(result: SearchResult, navController: NavHostController) {
    when (result.type) {
        SearchResultType.CATEGORY -> {
            navController.navigate(Screen.CategoryDetails.createRoute(result.id))
        }
        SearchResultType.SERVICE -> {
            // Navigate to a specific service detail page
            navController.navigate(Screen.ServiceDetails.createRoute(result.id))
        }
        SearchResultType.PROVIDER -> {
            navController.navigate(Screen.ProviderDetails.createRoute(result.id))
        }
    }
}

private fun generateSearchResults(query: String): List<SearchResult> {
    val lowerQuery = query.lowercase()

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
        name.lowercase().contains(lowerQuery)
    }.map { (id, name) ->
        SearchResult(
            id = id,
            title = name,
            subtitle = "Category",
            type = SearchResultType.CATEGORY
        )
    }

    // Search services
    val serviceResults = listOf(
        Service(id = "1001", categoryId = "101", providerId = "2001", name = "Basic House Cleaning", description = "General cleaning of your home", price = 1500.0),
        Service(id = "1002", categoryId = "101", providerId = "2001", name = "Deep Cleaning", description = "Thorough cleaning of all surfaces", price = 3000.0),
        Service(id = "2001", categoryId = "201", providerId = "3001", name = "Hair Styling", description = "Professional styling for all occasions", price = 1000.0),
        Service(id = "2002", categoryId = "201", providerId = "3001", name = "Hair Coloring", description = "Full coloring and highlights", price = 2500.0)
    ).filter { service ->
        service.name.lowercase().contains(lowerQuery) || service.description.lowercase().contains(lowerQuery)
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
        provider.businessName.lowercase().contains(lowerQuery) || provider.description.lowercase().contains(lowerQuery)
    }.map { provider ->
        SearchResult(
            id = provider.userId,
            title = provider.businessName,
            subtitle = "Service Provider • ${provider.description}",
            type = SearchResultType.PROVIDER
        )
    }

    // Combine and limit results
    return (categoryResults + serviceResults + providerResults).take(10)
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MJobaTheme { // Should match your actual theme name
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(
                navController = rememberNavController()
            )
        }
    }
}