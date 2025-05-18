package com.dluong.pet.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
)

val navigationItems = listOf(
    NavigationItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = MainDestinations.SEARCH_ROUTE
    ),
    NavigationItem(
        title = "Search",
        icon = Icons.Default.Search,
        route = MainDestinations.SEARCH_ROUTE
    ),
    NavigationItem(
        title = "Favorite",
        icon = Icons.Default.Favorite,
        route = MainDestinations.FAVORITE_ROUTE
    )
)