package com.dluong.pet.ui

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dluong.pet.data.utils.NetworkMonitor
import com.dluong.pet.presentation.home.HomeScreen
import com.dluong.pet.presentation.favorite.FavoriteScreen
import com.dluong.pet.presentation.search.SearchScreen
import com.dluong.pet.ui.navigation.MainDestinations
import com.dluong.pet.ui.navigation.PetsNavController
import com.dluong.pet.ui.navigation.navigationItems
import com.dluong.pet.ui.navigation.rememberPetsNavController
import com.dluong.pet.ui.theme.PetTheme
import timber.log.Timber


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CheckResult")
@Composable
fun PetApp(networkMonitor: NetworkMonitor) {
    PetTheme {
        val context = LocalContext.current
        val isOnline = remember { networkMonitor.isConnected }
        val isConnectedState = remember { mutableStateOf(true) } // Theo dõi trạng thái kết nối mạng

        DisposableEffect(isOnline) {
            val disposable = isOnline.subscribe({ online ->
                isConnectedState.value = online
                if (!online) {
                    Toast.makeText(context, "Network is disconnected...", Toast.LENGTH_SHORT).show()
                }
            }, { throwable ->
                Timber.tag("NetworkMonitor").e(throwable, "Error observing network state")
                try {
                    isOnline.doOnDispose({
                        Timber.tag("NetworkMonitor").d("Network state observation disposed")
                    })
                } catch (e: Exception) {
                    Timber.tag("NetworkMonitor").e(e, "Error disposing network state observation")
                }
            }
            )
            onDispose {
                disposable.dispose()
            }
        }

        if (isConnectedState.value) {
            // Nếu có mạng
            val navController = rememberPetsNavController()
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ) {
                NavHost(
                    navController = navController.navController,
                    startDestination = MainDestinations.HOME_ROUTE,
                ) {
                    composable(MainDestinations.HOME_ROUTE) {
                        HomeScreen(navController)
                    }
                    composable(MainDestinations.SEARCH_ROUTE) {
                        SearchScreen(navController)
                    }
                    composable(MainDestinations.FAVORITE_ROUTE) {
                        FavoriteScreen(navController)
                    }
                }
            }
        } else {
            // Màn hình thông báo mất kết nối mạng
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Please reconnect to the internet", color = Color.Red)
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: PetsNavController) {
    val selectedNavigationIndex = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        containerColor = Color.White
    ) {
        // Set the selected index based on the current route
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNavigationIndex.intValue == index,
                onClick = {
                    selectedNavigationIndex.intValue = index
                    navController.navigateToBottomBarRoute(item.route)
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(
                        item.title,
                        color = if (index == selectedNavigationIndex.intValue)
                            Color.Black
                        else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )

            )
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }