package com.dluong.pet.initializer

import android.content.Context
import androidx.startup.Initializer
import com.dluong.pet.BuildConfig
import com.dluong.pet.crashlytics.CrashlyticsLoggerTree
import timber.log.Timber

/**
 * Initializes Timber for logging.
 *
 * In debug mode, it uses [Timber.DebugTree] for detailed logs.
 * In release mode, it uses [CrashlyticsLoggerTree] to log messages to Crashlytics.
 */
class TimberInitializer : Initializer<Unit> {
    /**
     * Creates the Timber instance based on the build configuration.
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        val value = if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsLoggerTree())
        }
    }

    /**
     * Specifies that this initializer has no dependencies on other initializers.
     *
     * @return An empty list of dependencies.
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}