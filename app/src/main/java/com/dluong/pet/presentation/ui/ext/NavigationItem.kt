package com.dluong.pet.presentation.ui.ext

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val FAVORITE_ROUTE = "favorite"
    const val SEARCH_ROUTE = "search"
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
)

val navigationItems = listOf(
    NavigationItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = MainDestinations.HOME_ROUTE
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