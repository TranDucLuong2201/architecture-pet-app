package com.dluong.pet.presentation.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import timber.log.Timber


@Composable
fun rememberPetsNavController(
    navController: NavHostController = rememberNavController()
): PetsNavController = remember(navController) {
    PetsNavController(navController)
}

@Stable
class PetsNavController(
    val navController: NavHostController,
) {
    // ----------------------------------------------------------
    // Navigation state source of truth
    // ----------------------------------------------------------

    /**
     * Returns `true` if the [NavBackStackEntry] is resumed.
     *
     * This is useful for determining if the [NavBackStackEntry] has a start destination.
     */
    fun upPress() {
        if (navController.isGraphInitialized) {
            navController.navigateUp()
        } else {
            Timber.e("NavController graph is not initialized. Can not navigate up.")
        }
    }

    /**
     * Navigates to the given route if the current destination is not the same as the given route.
     * This is useful for navigating to bottom bar destinations.
     */
    fun navigateToBottomBarRoute(route: String) {
        if (navController.isGraphNotInitialized) {
            Timber.e("NavController graph is not initialized. Can not navigate to $route.")
            return
        } else {
            if (route != navController.currentDestination?.route) {
                navController.navigate(route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(findStartDestination(navController.graph).id) {
                        saveState = true
                    }
                }
            }
        }
    }
}

/**
 * Returns `true` if the [NavBackStackEntry] is resumed.
 *
 * This is useful for determining if the [NavBackStackEntry] is in the resumed state.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

/**
 * Returns the start destination of the [NavGraph].
 *
 * This is useful for determining if the [NavGraph] has a start destination.
 */
private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Finds the start destination of the [NavDestination].
 *
 * This is useful for determining if the [NavDestination] has a start destination.
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}
