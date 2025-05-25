package com.dluong.pet

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetsApplication : Application() {
    /**
     * Singleton instance of the application class.
     * This is used to access application context and other application-level resources
     * from anywhere in the app.
     */
    companion object {
        lateinit var instance: PetsApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
