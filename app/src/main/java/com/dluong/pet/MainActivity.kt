package com.dluong.pet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dluong.pet.presentation.ui.theme.PetTheme
import com.dluong.presentation.auth.AuthScreen
import com.dluong.presentation.auth.AuthViewModel
import com.dluong.presentation.feed.FeedScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CatTokApp()
                }
            }
        }
    }
}

@Composable
fun CatTokApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (authState.isLoggedIn) "feed" else "auth"
    ) {
        composable("auth") {
            AuthScreen(
                onNavigateToFeed = {
                    navController.navigate("feed") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("feed") {
            FeedScreen(
                onNavigateToComments = { postId ->
                    navController.navigate("comments/$postId")
                },
                onNavigateToProfile = { userId ->
                    navController.navigate("profile/$userId")
                }
            )
        }
    }
}
//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//
//    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
//        super.onCreate(savedInstanceState)
//        setContent {
//            val appState = rememberPetsNavController()
//            Scaffold(
//                bottomBar = {
//                    BottomNavigationBar(appState)
//                }
//            ) {
//                val events = hiltViewModel<NetworkStatusViewModel>().events
//                // Observe network status events
//                ObserveAsEvents(events) { event ->
//                    when (event) {
//                        is PetsEvent.Error -> {
//                            // Handle showing toast messages
//                            Toast.makeText(
//                                this,
//                                event.error.toString(this),
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//                        is PetsEvent.Success -> {
//                            // Handle success events if needed
//                            Toast.makeText(
//                                this,
//                                "Success: ${event.message}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//                NavHost(
//                    navController = appState.navController,
//                    startDestination = MainDestinations.HOME_ROUTE,
//                ) {
//                    setUpNavGraph(appState = appState)
//                }
//            }
//        }
//    }
//
//    private fun NavGraphBuilder.setUpNavGraph(
//        appState: PetsNavController,
//    ) {
//        composable(MainDestinations.HOME_ROUTE) {
//            HomeScreen()
//        }
//        composable(MainDestinations.FAVORITE_ROUTE) {
//            FavoriteScreen(openAndPopUp = { route, popUp ->
//                appState.navigateAndPopUp(
//                    route,
//                    popUp
//                )
//            })
//        }
//        composable(MainDestinations.MY_PROFILE) {
//            ProfileScreen()
//        }
//    }
//}
