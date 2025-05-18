package com.dluong.pet.presentation.ext

import androidx.navigation.NavController
import timber.log.Timber

/**
 * Extension function to check if the NavController's graph is initialized.
 *
 * @return true if the graph is initialized, false otherwise.
 */
val NavController.isGraphInitialized: Boolean
    get() = try {
        graph
        true
    } catch (_: IllegalStateException) {
        false
    }

/**
 * Extension function to check if the NavController's graph is not initialized.
 *
 * @return true if the graph is not initialized, false otherwise.
 */
val NavController.isGraphNotInitialized: Boolean
    get() = !isGraphInitialized

/**
 * Extension function to perform a safe navigation on the NavController.
 *
 * This function catches any [IllegalArgumentException] that may occur during navigation,
 * which usually happens when users trigger navigation multiple times quickly on slower devices.
 */
inline fun NavController.safeNavigate(block: NavController.() -> Unit) {
    try {
        this.block()
    } catch (e: IllegalArgumentException) {
        // Handle the exception gracefully
        Timber.e(e, "Handled navigation destination not found issue gracefully.")
    }
}