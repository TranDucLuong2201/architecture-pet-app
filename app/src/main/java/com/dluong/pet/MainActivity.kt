package com.dluong.pet

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dluong.core.presentation.ObserveAsEvents
import com.dluong.core.presentation.toString
import com.dluong.pet.presentation.ui.components.BottomNavigationBar
import com.dluong.pet.presentation.favorite.FavoriteScreen
import com.dluong.pet.presentation.home.HomeScreen
import com.dluong.pet.presentation.my_profile.ProfileScreen
import com.dluong.pet.presentation.ui.NetworkStatusViewModel
import com.dluong.pet.presentation.ui.PetsEvent
import com.dluong.pet.presentation.ui.ext.MainDestinations
import com.dluong.pet.presentation.ui.ext.PetsNavController
import com.dluong.pet.presentation.ui.ext.rememberPetsNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val appState = rememberPetsNavController()
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(appState)
                }
            ) {
                val events = hiltViewModel<NetworkStatusViewModel>().events
                // Observe network status events
                ObserveAsEvents(events) { event ->
                    when (event) {
                        is PetsEvent.Error -> {
                            // Handle showing toast messages
                            Toast.makeText(
                                this,
                                event.error.toString(this),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is PetsEvent.Success -> {
                            // Handle success events if needed
                            Toast.makeText(
                                this,
                                "Success: ${event.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                NavHost(
                    navController = appState.navController,
                    startDestination = MainDestinations.HOME_ROUTE,
                ) {
                    setUpNavGraph(appState = appState)
                }
            }
        }
    }

    fun NavGraphBuilder.setUpNavGraph(
        appState: PetsNavController,
    ) {
        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen()
        }
        composable(MainDestinations.FAVORITE_ROUTE) {
            FavoriteScreen(openAndPopUp = { route, popUp ->
                appState.navigateAndPopUp(
                    route,
                    popUp
                )
            })
        }
        composable(MainDestinations.MY_PROFILE) {
            ProfileScreen()
        }
    }
}
